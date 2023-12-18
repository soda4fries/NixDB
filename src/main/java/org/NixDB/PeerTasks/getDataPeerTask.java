package org.NixDB.PeerTasks;


import org.NixDB.DistributedHash.DHTNode;
import org.NixDB.PeerCommunication.Promise;

public class getDataPeerTask implements PeerTask {
    private final String key;

    String receiverPeerUUID;

    public getDataPeerTask(String receiverPeerUUID, String key) {
        this.key = key;
        this.receiverPeerUUID = receiverPeerUUID;
    }

    @Override
    public Promise perform() {
        DHTNode node = DHTNode.getNodeSingleTon();
        String Data = node.getData(key);
        return new getDataResult(Data, node.getUUID());
    }

    @Override
    public String getReceiverPeerUUID() {
        return receiverPeerUUID;
    }

    public static class getDataResult implements Promise {
        String result;
        public String ownerUUID;
        getDataResult(String Data, String ownerUUID) {
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
        if (returnedPromise instanceof getDataResult x) {
        } else System.out.println("error");
    }
}

