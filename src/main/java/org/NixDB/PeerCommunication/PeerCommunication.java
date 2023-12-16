package org.NixDB.PeerCommunication;

import org.NixDB.Datastructures.MyHashTable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PeerCommunication {

    static PeerCommunication peerCommunicationSingleton;


    public static PeerCommunication getInstance() {
        if (peerCommunicationSingleton == null) {
            peerCommunicationSingleton = new PeerCommunication();
        }
        return peerCommunicationSingleton;
    }

    private MyHashTable<String, Peer> peers;

    private PeerCommunication() {
        this.peers = new MyHashTable<String, Peer>();
    }

    public void addPeer(String name, String ipAddress, int port) {
        Peer peer = new Peer(name, ipAddress, port);
        peers.put(name, peer);
    }

    public MyHashTable<String, Peer> getPeers() {
        return peers;
    }

    public void printPeers() {
        System.out.println("List of Peers:");
        for (Peer peer : peers.values()) {
            System.out.println(peer.getName() + " - " + peer.getIpAddress() + ":" + peer.getPort());
        }
    }

    public void startServer(int port) {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Server started on port " + port);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(() -> handleClient(clientSocket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleClient(Socket clientSocket) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            Task task = (Task) objectInputStream.readObject();

            // Perform the action based on the message
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectOutputStream.writeObject(task.perform());
            objectOutputStream.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendTask(String peerName, Task task) {
        Peer peer = peers.get(peerName);
        if (peer != null) {
            try {
                Socket socket = new Socket(peer.getIpAddress(), peer.getPort());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                // Send the message to the peer
                objectOutputStream.writeObject(task);

                // Wait for promise and handle it
                task.Success(waitForAcknowledgment(socket));
                objectOutputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Peer not found: " + peerName);
        }
    }

    private Promise waitForAcknowledgment(Socket socket) {
        try {
            // Set a timeout for acknowledgment
            socket.setSoTimeout(5000); // 5 seconds timeout

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Promise promise = (Promise) objectInputStream.readObject();

            return promise;
        } catch (IOException | ClassNotFoundException e) {
            // Timeout or other IO or deserialization exception
            return new TimeoutPromise();
        }
    }

    public static void main(String[] args) {

    }
}

