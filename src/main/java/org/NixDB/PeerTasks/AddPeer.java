package org.NixDB.PeerTasks;

import org.NixDB.ZooKeeperTask.ConnectToPeer;
import org.NixDB.PeerCommunication.PeerCommunication;
import org.NixDB.PeerCommunication.Promise;
import org.NixDB.PeerCommunication.SuccessPromise;

public class AddPeer implements PeerTask {

    String ReceiverPeerName;

    private final String ipAddress;
    private final int port;

    public AddPeer(String ReceiverPeerName,String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.ReceiverPeerName = ReceiverPeerName;
    }

    @Override
    public Promise perform() {
        PeerCommunication peerCommunication = PeerCommunication.getInstance();
        peerCommunication.sendTask(new ConnectToPeer(ipAddress, port));
        return new SuccessPromise();
    }

    @Override
    public String getReceiverPeerUUID() {
        return ReceiverPeerName;
    }

    @Override
    public void Success(Promise returnedPromise) {
        if (returnedPromise instanceof ConnectToPeer.UUIDpromise x) {
            PeerCommunication peerCommunication = PeerCommunication.getInstance();
            peerCommunication.addPeerToList(x.uuid.toString(), ipAddress, port);
            System.out.println("Connected to Peer with UUID " + x.uuid.toString());
        }
    }
}
