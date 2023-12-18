package org.NixDB.PeerTasks;

import org.NixDB.DistributedHash.DHTNode;
import org.NixDB.PeerCommunication.Promise;
import org.NixDB.PeerCommunication.SuccessPromise;
import org.NixDB.PeerTasks.PeerTask;

public class ForwardData implements PeerTask {

    String ReceiverPeerUUID;

    String key;
    String value;


    public ForwardData(String ReceiverPeerUUID, String key, String value) {
        this.ReceiverPeerUUID = ReceiverPeerUUID;
        this.key = key;
        this.value = value;
    }

    @Override
    public Promise perform() {
        DHTNode dhtNode = DHTNode.getNodeSingleTon();
        dhtNode.putData(key, value);
        return new SuccessPromise("Successfully sent Data");
    }

    @Override
    public String getReceiverPeerUUID() {
        return ReceiverPeerUUID;
    }

    @Override
    public void Success(Promise returnedPromise) {

    }
}
