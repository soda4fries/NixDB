package org.NixDB.Client;

import org.NixDB.Node.DHTNode;
import org.NixDB.PeerCommunication.Promise;
import org.NixDB.Zookeeper.ZookeeperTask;

public class RemoveData implements ZookeeperTask {
    String designatedNodeIp;
    int designatedNodePort;

    String tableName;
    Object key;

    RemoveData(String designatedNodeIp, int designatedNodePort, String tableName, Object key) {
        this.designatedNodeIp = designatedNodeIp;
        this.designatedNodePort = designatedNodePort;
        this.tableName = tableName;
        this.key = key;
    }

    @Override
    public void performOnSuccess(Promise returnedPromise) {

    }

    @Override
    public Promise perform() {
        DHTNode.getNodeSingleTon().remove(tableName, key);
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
