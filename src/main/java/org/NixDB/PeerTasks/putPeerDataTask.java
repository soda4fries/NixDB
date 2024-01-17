package org.NixDB.PeerTasks;

import org.NixDB.DataStore.Tables;
import org.NixDB.PeerCommunication.Promise;

public class putPeerDataTask implements PeerTask{
    String peerUUID;
    String tableName;
    Object key;
    Object value;

    public putPeerDataTask(String peerUUID, String tableName, Object key, Object value) {
        this.peerUUID = peerUUID;
        this.tableName = tableName;
        this.key = key;
        this.value = value;
    }

    @Override
    public void performOnSuccess(Promise returnedPromise) {

    }

    @Override
    public Promise perform() {
        Tables.getInstance().putValue(tableName, key, value);
        System.out.println("Put value successfully on Node" + peerUUID);
        return () -> false;
    }

    @Override
    public String getReceiverPeerUUID() {
        return peerUUID;
    }
}
