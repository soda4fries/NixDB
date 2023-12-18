package org.NixDB.DistributedHash;


import org.NixDB.Datastructures.MyLinkedList;
import org.NixDB.PeerCommunication.PeerCommunication;
import org.NixDB.ZooKeeperTask.ConnectToPeer;
import org.NixDB.ZooKeeperTask.OrderPeerConnectionTask;
import org.NixDB.ZooKeeperTask.ZookeeperTask;

import java.util.Scanner;

public class Zookeeper {

    public static void initNodesConnections() {
        PeerCommunication peerCommunication = PeerCommunication.getInstance();
        MyLinkedList<NodeInfo> nodes = new MyLinkedList<>();
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter node address:port or 'done'");
        while (true) {
            String info = scan.nextLine();
            if ("DONE".equals(info.toUpperCase())) break;
            String[] ipPort = info.split(":");
            nodes.add(new NodeInfo(ipPort[0], Integer.valueOf(ipPort[1])));
        }
        for (NodeInfo nodeX: nodes) {
            for (NodeInfo nodeY: nodes) {
                if (nodeX.equals(nodeY)) continue;
                    peerCommunication.sendTask(new OrderPeerConnectionTask(
                            nodeX.ipAddress, nodeX.port,
                            nodeX.ipAddress, nodeY.port));
            }
        }
    }

    record NodeInfo(String ipAddress, int port) {

    }

}
