package org.NixDB.Client;

import org.NixDB.Datastructures.MyLinkedList;
import org.NixDB.PeerCommunication.Promise;
import org.NixDB.Zookeeper.Nodes;
import org.NixDB.Zookeeper.TablesEntry;
import org.NixDB.Zookeeper.Zookeeper;
import org.NixDB.Zookeeper.ZookeeperTask;

public class ConnectToDBTask implements ZookeeperTask {
    String ip;
    int port;
    String table;

    Class keyType;
    Class valueType;


    ConnectToDBTask(String ip, int port, String table, Class keyType, Class valueType) {
        this.ip = ip;
        this.port = port;
        this.table = table;
        this.keyType = keyType;
        this.valueType = valueType;
    }

    @Override
    public void performOnSuccess(Promise returnedPromise) {

    }

    @Override
    public Promise perform() {
        MyLinkedList<TablesEntry> tables = Zookeeper.getInstance().getTables();
        for (TablesEntry table : tables) {
            if (table.getTableName().equals(this.table) && table.getKeyType().equals(this.keyType) && table.getValueType().equals(this.valueType)) {
                Nodes node = Zookeeper.getInstance().getRandomNode();
                return new ConnectToDBResult(true, node.getIp(), node.getPort());
            }
        }
        Zookeeper.getInstance().addTablesToNodes(table, keyType, valueType);
        Nodes node = Zookeeper.getInstance().getRandomNode();
        return new ConnectToDBResult(true, node.getIp(), node.getPort());
    }

    @Override
    public String getIpAddress() {
        return ip;
    }

    @Override
    public int getPort() {
        return port;
    }
}
