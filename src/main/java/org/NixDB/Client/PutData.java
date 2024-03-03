package org.NixDB.Client;

import org.NixDB.Node.DHTNode;
import org.NixDB.PeerCommunication.Promise;
import org.NixDB.Zookeeper.ZookeeperTask;

public class PutData implements ZookeeperTask {
    String designatedNodeIp;
    int designatedNodePort;
    String tableName;
    Object key;
    Object value;

    PutData(String designatedNodeIp, int designatedNodePort, String tableName, Object key, Object value) {
        this.designatedNodeIp = designatedNodeIp;
        this.designatedNodePort = designatedNodePort;
        this.tableName = tableName;
        this.key = key;
        this.value = value;
    }


    @Override
    public void performOnSuccess(Promise returnedPromise) {
        System.out.println("Put data successfully on Node" + designatedNodeIp + ":" + designatedNodePort);
    }

    @Override
    public Promise perform() {
        DHTNode.getNodeSingleTon().put(tableName, key, value);
        System.out.println("Put data successfully on Node" + designatedNodeIp + ":" + designatedNodePort);
        return () -> true;
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
