package org.NixDB.Zookeeper;


import org.NixDB.DataStore.Tables;
import org.NixDB.Datastructures.MyLinkedList;
import org.NixDB.Node.CreateTableTask;
import org.NixDB.PeerCommunication.PeerCommunication;

public class Zookeeper {
    static Zookeeper instance;

    MyLinkedList<Nodes> Nodes;

    MyLinkedList<TablesEntry> tables;

    public Zookeeper() {
        Nodes = new MyLinkedList<>();

        tables = new MyLinkedList<>();
    }

    public static Zookeeper getInstance() {
        if (instance == null) {
            instance = new Zookeeper();
        }
        return instance;
    }

public void addNode(String uuid, String ip, int port) {
        for (Nodes node : Nodes) {
            PeerCommunication.getInstance().sendTask(new OrderPeerConnectionTask(node.getIp(), node.getPort(), ip, port));
            PeerCommunication.getInstance().sendTask(new OrderPeerConnectionTask(ip, port, node.getIp(), node.getPort()));
        }
        Nodes.add(new Nodes(uuid, ip, port));
    }

    public MyLinkedList<TablesEntry> getTables() {
        return tables;
    }

    private void addTable(TablesEntry table) {
        tables.add(table);
    }

    public void addTablesToNodes(String table, Class keyType, Class valueType) {
        for (Nodes node : Nodes) {
            PeerCommunication.getInstance().sendTask(new CreateTableTask( node.getIp(), node.getPort(),table, keyType, valueType));
        }
        addTable(new TablesEntry(table, keyType, valueType));
    }

   public Nodes getRandomNode() {
        return Nodes.get((int) (Math.random() * Nodes.size()));
    }

}
