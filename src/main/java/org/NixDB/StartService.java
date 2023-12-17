package org.NixDB;

import org.NixDB.DistributedHash.DHTNode;
import org.NixDB.PeerCommunication.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;


public class StartService {
    public static void main(String[] args) {
        PeerCommunication peerCommunication = PeerCommunication.getInstance();
        Random rand = new Random();

        // Initialize the service with a random port
        int randomPort = (int) (Math.random() * 1000) + 8000;
        peerCommunication.startServer(randomPort);

        System.out.printf("""
                *--------------------------------------*
                | Peer to Peer Service Function Started |
                *--------------------------------------*
                | Service Initialized on Port: %d      |
                | Local IP Address: %s                  |
                *--------------------------------------*
                %n""", randomPort, getLocalIPAddress());



        Scanner scanner = new Scanner(System.in);
        DHTNode node = DHTNode.getNodeSingleTon();

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
            String commandOrPeer = scanner.nextLine().trim();

            if (commandOrPeer.equalsIgnoreCase("connect")) {
                System.out.print("Enter peer IP address: ");
                String ipAddress = scanner.nextLine().trim();

                System.out.print("Enter peer port: ");
                int port = scanner.nextInt();

                peerCommunication.connectNewPeer(ipAddress,port);
            } else if
            (commandOrPeer.equalsIgnoreCase("list")) {
                peerCommunication.printPeers();
            } else if (commandOrPeer.equalsIgnoreCase("exit")) {
                break;
            } else {

                // Ask for function to execute
                System.out.print("""
                        Choose function to execute on peer 'addPeers' 'addNumbers' 'putData' 'getData' 'printNodeData' 'distributionTest'):
                         """);
                String functionName = scanner.nextLine().trim();


                switch (functionName) {
                    case "addPeers":
                        System.out.print("Enter peer ip address: ");
                        String newPeerIp = scanner.nextLine().trim();
                        System.out.print("Enter peer port: ");
                        int newPeerPort = scanner.nextInt();
                        scanner.nextLine();

                        // Create and send AddMessage to the selected peer
                        peerCommunication.sendTask(new AddPeer(commandOrPeer,newPeerIp, newPeerPort));
                        break;
                    case "addNumbers":
                        System.out.print("Enter first number: ");
                        int num1 = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character

                        System.out.print("Enter second number: ");
                        int num2 = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character

                        // Create and send AddMessage to the selected peer
                        peerCommunication.sendTask(new addNumberTask(commandOrPeer,num1, num2));
                        break;
                    case "putData":
                        System.out.print("Enter Key: ");
                        String key = scanner.nextLine();
                        System.out.print("Enter Value: ");
                        String value = scanner.nextLine();
                        node.putData(key,value);
                        break;
                    case "getData":
                        System.out.print("Enter Key: ");
                        String searchKey = scanner.nextLine();
                        node.getData(searchKey);
                        break;
                    case "printNodeData":
                        System.out.println(node.getDataStoreAsStr());
                        break;

                    case "distributionTest":
                        List<String> randomStrings = generateRandomStrings(100);
                        // Assuming you have an instance of DHTNode called 'node'
                        for (int i = 0; i < randomStrings.size(); i++) {
                            String keyRandom = "key" + i*rand.nextInt();
                            String valueRandom = randomStrings.get(i);
                            node.putData(keyRandom, valueRandom);
                        }
                        System.out.println(node.getDataStoreAsStr());
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

    public static List<String> generateRandomStrings(int count) {
        List<String> randomStrings = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String randomString = UUID.randomUUID().toString();
            randomStrings.add(randomString);
        }

        return randomStrings;
    }

}
