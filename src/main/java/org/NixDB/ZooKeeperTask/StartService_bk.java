package org.NixDB.ZooKeeperTask;

import org.NixDB.DistributedHash.DHTNode;
import org.NixDB.DistributedHash.Zookeeper;
import org.NixDB.PeerCommunication.PeerCommunication;
import org.NixDB.PeerTasks.AddPeer;
import org.NixDB.PeerTasks.addNumberPeerTask;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;


public class StartService_bk {
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

            String commandOrPeer = scanner.nextLine().trim();

            switch (commandOrPeer) {
                case "connect" -> {
                    System.out.print("Enter peer IP address: ");
                    String ipAddress = scanner.nextLine().trim();

                    System.out.print("Enter peer port: ");
                    int port = scanner.nextInt();
                    peerCommunication.sendTask(new ConnectToPeer(ipAddress, port));
                }
                case "list" -> {
                    peerCommunication.printPeers();
                }
                case "init" -> {
                    Zookeeper.initNodesConnections();
                }
                case "exit" -> {
                    return;
                }
                case "help" -> {
                    System.out.print("""
                            +---------------------------------------------+
                            |  Type 'exit' to finish,                     |
                            |  'connect' to add local peers,              |
                            |  'init' to connect many peers               |
                            |  'list' to list all peers,                  |
                            |  'operate' to operate the database          |
                            |  'help' to get instructions                 |
                            +---------------------------------------------+
                            Input:
                            """);
                }
                case "operate" -> {
                    // Ask for function to execute
                    System.out.print("""
                            Choose function to execute on peer 'addPeers' 'addNumbers' 'putData' 'getData' 'printNodeData' 'distributionTest'):
                             """);
                    String functionName = scanner.nextLine().trim();


                    switch (functionName) {
                        case "addPeers" -> {
                            System.out.print("Enter peer ip address: ");
                            String newPeerIp = scanner.nextLine().trim();
                            System.out.print("Enter peer port: ");
                            int newPeerPort = scanner.nextInt();
                            scanner.nextLine();

                            // Create and send AddMessage to the selected peer
                            peerCommunication.sendTask(new AddPeer(commandOrPeer, newPeerIp, newPeerPort));
                        }
                        case "putData" -> {
                            System.out.print("Enter Key: ");
                            String key = scanner.nextLine();
                            System.out.print("Enter Value: ");
                            String value = scanner.nextLine();
                            node.putData(key, value);
                        }
                        case "getData" -> {
                            System.out.print("Enter Key: ");
                            String searchKey = scanner.nextLine();
                            node.getData(searchKey);
                        }
                        case "printNodeData" -> {
                            System.out.println(node.getDataStoreAsStr());
                        }
                        case "distributionTest" -> {
                            List<String> randomStrings = generateRandomStrings(100);
                            // Assuming you have an instance of DHTNode called 'node'
                            for (int i = 0; i < randomStrings.size(); i++) {
                                String keyRandom = "key" + i * rand.nextInt();
                                String valueRandom = randomStrings.get(i);
                                node.putData(keyRandom, valueRandom);
                            }
                            System.out.println(node.getDataStoreAsStr());
                        }
                        default -> {
                            System.out.println("Unknown function: " + functionName);
                        }
                    }
                }

            }

        }
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
