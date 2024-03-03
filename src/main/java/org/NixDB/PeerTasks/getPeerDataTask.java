package org.NixDB.PeerTasks;

import org.NixDB.DataStore.Tables;
import org.NixDB.PeerCommunication.Promise;

public class getPeerDataTask implements PeerTask{
    String peerUUID;

    String tableName;
    Object key;

    public getPeerDataTask(String peerUUID, String tableName, Object key) {
        this.peerUUID = peerUUID;
        this.tableName = tableName;
        this.key = key;
    }
    @Override
    public void performOnSuccess(Promise returnedPromise) {
        System.out.println("Got value successfully on Node" + peerUUID);
    }

    @Override
    public Promise perform() {

        return new getPeerDataResult(Tables.getInstance().getValue(tableName, key), peerUUID);
    }

    @Override
    public String getReceiverPeerUUID() {
        return peerUUID;
    }


}

