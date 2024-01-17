package org.NixDB;

import org.NixDB.Client.NixDBConnection;
import org.NixDB.Node.DHTNode;
import org.NixDB.Node.JoinSwarmTask;
import org.NixDB.PeerCommunication.PeerCommunication;

import java.util.Random;
import java.util.Scanner;
import java.net.*;
public class Tester {
    public static void main(String[] args) throws UnknownHostException {
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
                 | UUID : %s        w                   |
                *--------------------------------------*
                %n""", randomPort, InetAddress.getLocalHost(), peerCommunication.getUuid().toString());



        Scanner scanner = new Scanner(System.in);
        DHTNode node = DHTNode.getNodeSingleTon();
        NixDBConnection<String,String> connection = null;

        while (true) {
            System.out.println("Enter a command:");
            String command = scanner.nextLine();
            String[] commandParts = command.split(" ");
            if (commandParts[0].equals("put") && connection != null) {
                connection.put(commandParts[1], commandParts[2]);
            } else if (commandParts[0].equals("get")) {
                System.out.println(connection.get(commandParts[1]));
            } else if (commandParts[0].equals("remove")) {
                node.remove(commandParts[1], commandParts[2]);
                connection.remove(commandParts[1]);
            } else if (commandParts[0].equals("exit")) {
                System.exit(0);
            } else if (commandParts[0].equals("join")) {
                String ip = commandParts[1];
                int port = Integer.parseInt(commandParts[2]);
                peerCommunication.sendTask(new JoinSwarmTask(ip, port, peerCommunication.getUuid().toString(), InetAddress.getLocalHost().getHostAddress(), randomPort));
            } else if (commandParts[0].equals("createTable")) {
                connection = new NixDBConnection<String, String>(commandParts[1], commandParts[2], Integer.parseInt(commandParts[3]), String.class, String.class);
            } else if (commandParts[0].equals("help")) {
                System.out.println("""
                        put <table> <key> <value> - Put a value in a table
                        get <table> <key> - Get a value from a table
                        remove <table> <key> - Remove a value from a table
                        createTable <table> <ZooKeeper IP> <ZooKeeper Port> - Create a table
                        join <IP> <Port> - Join a swarm
                        exit - Exit the program
                        """);
            } else {
                System.out.println("Invalid command");
            }
        }
        }

    }

