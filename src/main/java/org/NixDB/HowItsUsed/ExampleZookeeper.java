package org.NixDB.HowItsUsed;

import org.NixDB.Node.DHTNode;
import org.NixDB.PeerCommunication.PeerCommunication;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

public class ExampleZookeeper {
    public static void main(String[] args) throws UnknownHostException {
        PeerCommunication peerCommunication = PeerCommunication.getInstance();
        Random rand = new Random();
        int randomPort = 2181;
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

    }
}
