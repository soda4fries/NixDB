package org.NixDB.PeerTasks;

import org.NixDB.DataStore.Tables;
import org.NixDB.PeerCommunication.Promise;

public class getPeerAllDataTask implements PeerTask{
    String peerUUID;

    String tableName;

    public getPeerAllDataTask(String peerUUID, String tableName) {
        this.peerUUID = peerUUID;
        this.tableName = tableName;
    }
    @Override
    public void performOnSuccess(Promise returnedPromise) {
        System.out.println("Got value successfully on Node" + peerUUID);
    }

    @Override
    public Promise perform() {

        System.out.println(Tables.getInstance().getTable(tableName).getData().toString());
        System.out.println(Tables.getInstance().getTable(tableName).getData().size());
        return new getPeerDataResult(Tables.getInstance().getTable(tableName), peerUUID);

    }

    @Override
    public String getReceiverPeerUUID() {
        return peerUUID;
    }


}

