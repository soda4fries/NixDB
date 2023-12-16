package org.NixDB.PeerCommunication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PeerCommunication {

    private Map<String, Peer> peers;

    public PeerCommunication() {
        this.peers = new HashMap<>();
    }

    public void addPeer(String name, String ipAddress, int port) {
        Peer peer = new Peer(name, ipAddress, port);
        peers.put(name, peer);
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
            Message message = (Message) objectInputStream.readObject();

            // Perform the action based on the message
            message.perform();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String peerName, Message message) {
        Peer peer = peers.get(peerName);
        if (peer != null) {
            try {
                Socket socket = new Socket(peer.getIpAddress(), peer.getPort());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(message);
                objectOutputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Peer not found: " + peerName);
        }
    }

    public static void main(String[] args) {
        PeerCommunication peerCommunication = new PeerCommunication();

        // Initialize the service with a random port
        int randomPort = (int) (Math.random() * 1000) + 8000;
        System.out.println("Service initialized on port " + randomPort);
        peerCommunication.startServer(randomPort);

        // Ask for peers
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter peer name (type 'exit' to finish): ");
            String peerName = scanner.nextLine().trim();

            if (peerName.equalsIgnoreCase("exit")) {
                break;
            }

            System.out.print("Enter peer IP address: ");
            String ipAddress = scanner.nextLine().trim();

            System.out.print("Enter peer port: ");
            int port = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            peerCommunication.addPeer(peerName, ipAddress, port);
        }

        // Display the list of peers
        System.out.println("List of Peers:");
        for (Peer peer : peerCommunication.peers.values()) {
            System.out.println(peer.getName() + " - " + peer.getIpAddress() + ":" + peer.getPort());
        }

        // Start the messaging function
        System.out.println("Messaging function started.");
        while (true) {
            System.out.print("Enter peer name to send a message (type 'exit' to finish): ");
            String peerName = scanner.nextLine().trim();

            if (peerName.equalsIgnoreCase("exit")) {
                break;
            }

            // Ask for function to execute
            System.out.print("Choose function to execute ('addNumbers' or 'printString'): ");
            String functionName = scanner.nextLine().trim();

            switch (functionName) {
                case "addNumbers":
                    System.out.print("Enter first number: ");
                    int num1 = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    System.out.print("Enter second number: ");
                    int num2 = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    // Create and send AddMessage to the selected peer
                    peerCommunication.sendMessage(peerName, new AddMessage(num1, num2));
                    break;
                case "printString":
                    System.out.print("Enter string to print: ");
                    String str = scanner.nextLine().trim();

                    // Create and send PrintMessage to the selected peer
                    peerCommunication.sendMessage(peerName, new PrintMessage(str));
                    break;
                default:
                    System.out.println("Unknown function: " + functionName);
            }
        }

        // Close the scanner
        scanner.close();
    }
}

