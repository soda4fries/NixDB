package org.NixDB.PeerCommunication;

import org.NixDB.Datastructures.MyHashTable;
import org.NixDB.PeerTasks.PeerTask;
import org.NixDB.ZooKeeperTask.ZookeeperTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

public class PeerCommunication {

    private static PeerCommunication peerCommunicationSingleton;
    private final UUID uuid;

    private final ExecutorService executorService;

    private final MyHashTable<String, Peer> peers;



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
        executorService = Executors.newCachedThreadPool();
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
            try (ServerSocket serverSocket = new ServerSocket(port)){
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

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectOutputStream.writeObject(task.perform());
            objectOutputStream.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Promise sendTask(Task task) {
        String ipAddress;
        int port;

        if (task instanceof ZookeeperTask x) {
            ipAddress = x.getIpAddress();
            port = x.getPort();
        } else if (task instanceof PeerTask x) {
            Peer peer = peers.get(x.getReceiverPeerUUID());
            if (peer != null) {
                ipAddress = peer.getIpAddress();
                port = peer.getPort();
            } else {
                System.out.println("Peer not found: " + x.getReceiverPeerUUID());
                return new TimeoutPromise();
            }
        } else throw new RuntimeException();

        try {
            Socket socket = new Socket(ipAddress, port);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // Send the message to the peer
            objectOutputStream.writeObject(task);

            // Wait for promise and handle it
            Promise promise = waitForAcknowledgment(socket);
            task.Success(promise);
            objectOutputStream.close();
            socket.close();
            return promise;
        } catch (IOException e) {
            e.printStackTrace();
            task.Success(new TimeoutPromise());
            return new TimeoutPromise();
        }
    }




    public boolean sendTasks(List<Task> tasks, int k) {
        CountDownLatch latch = new CountDownLatch(tasks.size());
        List<Future<Boolean>> futures = new ArrayList<>();

        for (Task task : tasks) {
            Future<Boolean> future = executorService.submit(() -> {
                Promise promise = sendTask(task);
                latch.countDown();
                return promise.isSuccess();
            });

            futures.add(future);
        }

        try {

            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int successfulCount = 0;

        for (Future<Boolean> future : futures) {
            try {
                if (future.get()) {
                    successfulCount++;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }


        return successfulCount >= k;
    }


    private Promise waitForAcknowledgment(Socket socket) {
        try {

            socket.setSoTimeout(5000);

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            return (Promise) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {

            return new TimeoutPromise();
        }
    }

    public static void main(String[] args) {

    }
}

