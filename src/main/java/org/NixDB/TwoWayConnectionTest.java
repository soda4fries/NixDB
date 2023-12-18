package org.NixDB;

import org.NixDB.PeerTasks.AddPeer;
import org.NixDB.PeerCommunication.Peer;
import org.NixDB.PeerCommunication.PeerCommunication;
import org.NixDB.ZooKeeperTask.ConnectToPeer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TwoWayConnectionTest {
    public static void main(String[] args) {

        PeerCommunication peerCommunication = PeerCommunication.getInstance();

        // Initialize the service with a random port
        int randomPort = (int) (Math.random() * 1000) + 8000;
        peerCommunication.startServer(randomPort);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter remote ip:");
        String ip = scanner.nextLine();
        System.out.println("Enter port");
        int port = scanner.nextInt();

        peerCommunication.sendTask(new ConnectToPeer(ip, port));

        for (String x: peerCommunication.getPeers().keys()) {
            Peer y = peerCommunication.getPeers().get(x);
            if (y.getIpAddress().equals(ip) && y.getPort()==port) {
                peerCommunication.sendTask(new AddPeer(x,getLocalIPAddress(), randomPort));
            }
        }

        scanner.nextInt();

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
