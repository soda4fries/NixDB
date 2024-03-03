package org.NixDB.Client;

import org.NixDB.Node.DHTNode;
import org.NixDB.PeerCommunication.Promise;
import org.NixDB.Zookeeper.ZookeeperTask;

public class GetData implements ZookeeperTask {
    String designatedNodeIp;
    int designatedNodePort;
    Object key;
    String tableName;
    GetData(String designatedNodeIp, int designatedNodePort, String tableName, Object key) {
        this.designatedNodeIp = designatedNodeIp;
        this.designatedNodePort = designatedNodePort;
        this.key = key;
        this.tableName = tableName;
    }

    @Override
    public void performOnSuccess(Promise returnedPromise) {

    }

    @Override
    public Promise perform() {
        Object obj = DHTNode.getNodeSingleTon().get(tableName, key);
        if (obj == null) {
            System.out.println("Failed to get data from Node" + designatedNodeIp + ":" + designatedNodePort);
            return () -> false;
        }
        return new Value(obj);
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


