package org.NixDB.ZooKeeperTask;

import org.NixDB.PeerCommunication.PeerCommunication;
import org.NixDB.PeerCommunication.Promise;
import org.NixDB.PeerCommunication.SuccessPromise;

public class OrderPeerConnectionTask implements ZookeeperTask {

    String receiverIpAddress;
    int receiverPort;
    String peerIpAddress;
    int peerPort;

    public OrderPeerConnectionTask(String receiverIpAddress, int receiverPort, String peerIpAddress, int peerPort) {
        this.receiverIpAddress = receiverIpAddress;
        this.receiverPort = receiverPort;
        this.peerIpAddress = peerIpAddress;
        this. peerPort = peerPort;
    }

    @Override
    public void Success(Promise returnedPromise) {
        if (returnedPromise instanceof SuccessPromise x) System.out.println(x.getMessage());
        else System.out.println("Error connecting");
    }

    @Override
    public Promise perform() {
        PeerCommunication.getInstance().sendTask(new ConnectToPeer(peerIpAddress, peerPort));
        return new SuccessPromise(PeerCommunication.getInstance().getUuid().toString() + "successfully connected to peer");
    }

    @Override
    public String getIpAddress() {
        return receiverIpAddress;
    }

    @Override
    public int getPort() {
        return receiverPort;
    }
}
