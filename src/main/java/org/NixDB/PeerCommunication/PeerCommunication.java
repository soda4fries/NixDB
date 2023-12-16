package org.NixDB.PeerCommunication;

import org.NixDB.Datastructures.MyHashTable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class PeerCommunication {

    private static PeerCommunication peerCommunicationSingleton;
    private final UUID uuid;

    private MyHashTable<String, Peer> peers;


    public static PeerCommunication getInstance() {
        if (peerCommunicationSingleton == null) {
            peerCommunicationSingleton = new PeerCommunication();
        }
        return peerCommunicationSingleton;
    }

    public UUID getUuid() {
        return uuid;
    }

    private PeerCommunication() {
        this.peers = new MyHashTable<>();
        uuid = UUID.randomUUID();
    }

    public void addPeerToList(String name, String ipAddress, int port) {
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
                System.out.println("Server started...");

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

    public void connectNewPeer(String ipAddress, int port) {
        String peerUUID;
            try {
                Socket socket = new Socket(ipAddress, port);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                Task connectionTask = new ConnectToPeer(ipAddress,port);
                // Send the message to the peer
                objectOutputStream.writeObject(connectionTask);
                // Wait for promise and handle it
                connectionTask.Success(waitForAcknowledgment(socket));
                objectOutputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


    private Promise waitForAcknowledgment(Socket socket) {
        try {
            // Set a timeout for acknowledgment
            socket.setSoTimeout(5000); // 5 seconds timeout

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            return (Promise) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Timeout or other IO or deserialization exception
            return new TimeoutPromise();
        }
    }

    public static void main(String[] args) {

    }
}

