package org.NixDB.HowItsUsed;

import org.NixDB.Node.DHTNode;
import org.NixDB.Node.JoinSwarmTask;
import org.NixDB.PeerCommunication.PeerCommunication;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

public class ExampleNode {
    public static void main(String[] args) throws UnknownHostException {
        PeerCommunication peerCommunication = PeerCommunication.getInstance();
        Random rand = new Random();

        int randomPort = (int) (Math.random() * 1000) + 8000;
        peerCommunication.startServer(randomPort);

        System.out.printf("""
                *--------------------------------------*
                 This is a node
                 It will join a swarm
                    It will then be able to communicate with other nodes
                    It will also a part of the data, about 1/n of the data, where n is the number of nodes
                    
                *--------------------------------------*
                | Service Initialized on Port: %d      |
                | Local IP Address: %s                  |
                 | UUID : %s        w                   |
                *--------------------------------------*
                %n""", randomPort, InetAddress.getLocalHost(), peerCommunication.getUuid().toString());



        Scanner scanner = new Scanner(System.in);
        DHTNode node = DHTNode.getNodeSingleTon();

        peerCommunication.sendTask(new JoinSwarmTask("localhost", 2181, peerCommunication.getUuid().toString(), InetAddress.getLocalHost().getHostAddress(), randomPort));

    }
}
