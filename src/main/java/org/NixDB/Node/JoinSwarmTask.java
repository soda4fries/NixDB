package org.NixDB.Node;

import org.NixDB.PeerCommunication.Promise;
import org.NixDB.Zookeeper.Zookeeper;
import org.NixDB.Zookeeper.ZookeeperTask;

public class JoinSwarmTask implements ZookeeperTask {
    String zookeeperIp;
    int zookeeperPort;

    String Nodeuuid;
    String Nodeip;
    int Nodeport;

    public JoinSwarmTask(String zookeeperIp, int zookeeperPort, String Nodeuuid, String Nodeip, int Nodeport) {
        this.zookeeperIp = zookeeperIp;
        this.zookeeperPort = zookeeperPort;
        this.Nodeuuid = Nodeuuid;
        this.Nodeip = Nodeip;
        this.Nodeport = Nodeport;
    }

    @Override
    public void performOnSuccess(Promise returnedPromise) {

    }

    @Override
    public Promise perform() {
        Zookeeper.getInstance().addNode(Nodeuuid, Nodeip, Nodeport);
        return () -> true;
    }

    @Override
    public String getIpAddress() {
        return zookeeperIp;
    }

    @Override
    public int getPort() {
        return zookeeperPort;
    }
}
