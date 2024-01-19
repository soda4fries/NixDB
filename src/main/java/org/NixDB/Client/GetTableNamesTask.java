package org.NixDB.Client;

import org.NixDB.Datastructures.MyLinkedList;
import org.NixDB.PeerCommunication.Promise;
import org.NixDB.PeerCommunication.Task;
import org.NixDB.Zookeeper.TablesEntry;
import org.NixDB.Zookeeper.Zookeeper;
import org.NixDB.Zookeeper.ZookeeperTask;

public class GetTableNamesTask implements ZookeeperTask {
    String zooKeeperIpAddress;
    int zooKeeperPort;

    public GetTableNamesTask(String zooKeeperIpAddress, int zooKeeperPort) {
        this.zooKeeperIpAddress = zooKeeperIpAddress;
        this.zooKeeperPort = zooKeeperPort;
    }

    @Override
    public void performOnSuccess(Promise returnedPromise) {

    }

    @Override
    public Promise perform() {
        MyLinkedList<TablesEntry> tables = new MyLinkedList<>();
        for (TablesEntry table : Zookeeper.getInstance().getTables()) {
            tables.add(table);
        }
        return new Value(tables);
    }

    @Override
    public String getIpAddress() {
        return zooKeeperIpAddress;
    }

    @Override
    public int getPort() {
        return zooKeeperPort;
    }
}
