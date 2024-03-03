package org.NixDB.PeerTasks;

import org.NixDB.DataStore.Tables;
import org.NixDB.PeerCommunication.Promise;

public class removePeerDataTask implements PeerTask{
    String peerUUID;
    String tableName;
    Object key;

    public removePeerDataTask(String peerUUID, String tableName, Object key) {
        this.peerUUID = peerUUID;
        this.tableName = tableName;
        this.key = key;
    }

    @Override
    public void performOnSuccess(Promise returnedPromise) {

    }

    @Override
    public Promise perform() {
        Object obh = Tables.getInstance().removeValue(tableName, key);
        if (obh == null) {
            System.out.println("Failed to remove value on Node" + peerUUID);
            return () -> false;
        }
        System.out.println("Removed value successfully on Node" + peerUUID);
        return () -> true;
    }

    @Override
    public String getReceiverPeerUUID() {
        return peerUUID;
    }
}
