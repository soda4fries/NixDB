package org.NixDB.PeerTasks;


import org.NixDB.DistributedHash.DHTNode;
import org.NixDB.PeerCommunication.Promise;

public class ReceiveDataPeerTask implements PeerTask {
    private final String key;

    String receiverPeerUUID;

    public ReceiveDataPeerTask(String receiverPeerUUID, String key) {
        this.key = key;
        this.receiverPeerUUID = receiverPeerUUID;
    }

    @Override
    public Promise perform() {
        DHTNode node = DHTNode.getNodeSingleTon();
        String Data = node.getData(key);
        return new ReceivedDataResult(Data, node.getUUID());
    }

    @Override
    public String getReceiverPeerUUID() {
        return receiverPeerUUID;
    }

    public static class ReceivedDataResult implements Promise {
        String result;
        public String ownerUUID;
        ReceivedDataResult(String Data, String ownerUUID) {
            this.result = Data;
            this.ownerUUID = ownerUUID;
        }

        public String getResult() {
            return result;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }
    }

    @Override
    public void Success(Promise returnedPromise) {
        if (returnedPromise instanceof ReceivedDataResult x) {
        } else System.out.println("error");
    }
}

