package org.NixDB;

import org.NixDB.PeerCommunication.*;

import java.util.Scanner;

public class StartService {
    public static void main(String[] args) {
        PeerCommunication peerCommunication = PeerCommunication.getInstance();

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
        peerCommunication.printPeers();


        // Start the messaging function
        System.out.println("Messaging function started.");
        while (true) {
            System.out.print("Enter peer name to send a message (type 'exit' to finish): ");
            String peerName = scanner.nextLine().trim();

            if (peerName.equalsIgnoreCase("exit")) {
                break;
            }

            // Ask for function to execute
            System.out.print("""
                    Choose function to execute or 'printString' or 'addPeers' or 'printPeer' or 'addNumbers'):
                     """);
            String functionName = scanner.nextLine().trim();

            switch (functionName) {
                case "printString":
                    System.out.print("Enter string to print: ");
                    String str = scanner.nextLine().trim();

                    // Create and send PrintMessage to the selected peer
                    peerCommunication.sendTask(peerName, new PrintTask(str));
                    break;
                case "addPeers":
                    System.out.print("Enter peer Name: ");
                    String newPeerName = scanner.nextLine().trim();
                    System.out.print("Enter peer ip address: ");
                    String newPeerIp = scanner.nextLine().trim();
                    System.out.print("Enter peer port: ");
                    int newPeerPort = scanner.nextInt();
                    scanner.nextLine();

                    // Create and send AddMessage to the selected peer
                    peerCommunication.sendTask(peerName, new AddPeer(newPeerName, newPeerIp, newPeerPort));
                    break;
                case "printPeer":
                    // Create and send PrintMessage to the selected peer
                    peerCommunication.sendTask(peerName, new DisplayPeers());
                    break;
                case "addNumbers":
                    System.out.print("Enter first number: ");
                    int num1 = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    System.out.print("Enter second number: ");
                    int num2 = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    // Create and send AddMessage to the selected peer
                    peerCommunication.sendTask(peerName, new addNumberTask(num1, num2));
                    break;


                default:
                    System.out.println("Unknown function: " + functionName);
            }
        }

        // Close the scanner
        scanner.close();
    }
}
