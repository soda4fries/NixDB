package org.NixDB.Client;

import org.NixDB.DataStore.Table;
import org.NixDB.DataStore.Tables;
import org.NixDB.Datastructures.MyLinkedList;
import org.NixDB.Node.DHTNode;
import org.NixDB.PeerCommunication.Promise;
import org.NixDB.Zookeeper.ZookeeperTask;

import java.util.LinkedList;
import java.util.Map;

public class GetAllData implements ZookeeperTask {
    String designatedNodeIp;
    int designatedNodePort;
    String tableName;

    public GetAllData(String designatedNodeIp, int designatedNodePort, String tableName) {
        this.designatedNodeIp = designatedNodeIp;
        this.designatedNodePort = designatedNodePort;
        this.tableName = tableName;
    }

    @Override
    public void performOnSuccess(Promise returnedPromise) {
        // Perform any necessary actions on success
    }

    @Override
    public Promise perform() {
        MyLinkedList<Table> tables = DHTNode.getNodeSingleTon().getAll(tableName);
        if (tables == null) {
            System.out.println("Failed to get all data from Node" + designatedNodeIp + ":" + designatedNodePort);
            return () -> false;
        }
        return new Value(tables);
    }

    @Override
    public String getIpAddress() {
        return designatedNodeIp;
    }

    @Override
    public int getPort() {
        return designatedNodePort;
    }
}
