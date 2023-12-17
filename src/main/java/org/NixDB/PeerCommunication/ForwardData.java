package org.NixDB.PeerCommunication;

import org.NixDB.DistributedHash.DHTNode;

public class ForwardData implements Task {

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
        return new SuccessPromise();
    }

    @Override
    public String getReceiverPeerUUID() {
        return ReceiverPeerUUID;
    }

    @Override
    public void Success(Promise returnedPromise) {

    }
}
