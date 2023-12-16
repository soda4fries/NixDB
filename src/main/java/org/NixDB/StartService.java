package org.NixDB;

import org.NixDB.PeerCommunication.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class StartService {
    public static void main(String[] args) {
        PeerCommunication peerCommunication = PeerCommunication.getInstance();

        // Initialize the service with a random port
        int randomPort = (int) (Math.random() * 1000) + 8000;
        peerCommunication.startServer(randomPort);

        System.out.println("""
        *--------------------------------------*
        | Peer to Peer Service Function Started |
        *--------------------------------------*
        | Service Initialized on Port: %d      |
        | Local IP Address: %s                  |
        *--------------------------------------*
        """.formatted(randomPort, getLocalIPAddress()));



        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("""
            +---------------------------------------------+
            |  Type 'exit' to finish,                      |
            |  'connect' to add local peers,               |
            |  'list' to list all peers,                   |
            |  or enter a peer name.                       |
            +---------------------------------------------+
            Input: 
            """);
            String command = scanner.nextLine().trim();

            if (command.equalsIgnoreCase("connect")) {
                System.out.print("Enter peer IP address: ");
                String ipAddress = scanner.nextLine().trim();

                System.out.print("Enter peer port: ");
                int port = scanner.nextInt();

                peerCommunication.connectNewPeer(ipAddress,port);
            } else if
            (command.equalsIgnoreCase("list")) {
                peerCommunication.printPeers();
            } else if (command.equalsIgnoreCase("exit")) {
                break;
            } else {

                // Ask for function to execute
                System.out.print("""
                        Choose function to execute on peer 'printString' 'addPeers' 'printPeer' 'addNumbers'):
                         """);
                String functionName = scanner.nextLine().trim();

                switch (functionName) {
                    case "printString":
                        System.out.print("Enter string to print: ");
                        String str = scanner.nextLine().trim();

                        // Create and send PrintMessage to the selected peer
                        peerCommunication.sendTask(command, new PrintTask(str));
                        break;
                    case "addPeers":
                        System.out.print("Enter peer ip address: ");
                        String newPeerIp = scanner.nextLine().trim();
                        System.out.print("Enter peer port: ");
                        int newPeerPort = scanner.nextInt();
                        scanner.nextLine();

                        // Create and send AddMessage to the selected peer
                        peerCommunication.sendTask(command, new AddPeer(newPeerIp, newPeerPort));
                        break;
                    case "printPeer":
                        // Create and send PrintMessage to the selected peer
                        peerCommunication.sendTask(command, new DisplayPeers());
                        break;
                    case "addNumbers":
                        System.out.print("Enter first number: ");
                        int num1 = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character

                        System.out.print("Enter second number: ");
                        int num2 = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character

                        // Create and send AddMessage to the selected peer
                        peerCommunication.sendTask(command, new addNumberTask(num1, num2));
                        break;


                    default:
                        System.out.println("Unknown function: " + functionName);
                }
            }
        }

        // Close the scanner
        scanner.close();
    }

    private static String getLocalIPAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "0.0.0.0";
        }
    }

}
