package org.NixDB.Node;

import org.NixDB.DataStore.Tables;
import org.NixDB.PeerCommunication.PeerCommunication;
import org.NixDB.PeerCommunication.Promise;
import org.NixDB.PeerTasks.PeerTask;
import org.NixDB.Zookeeper.ZookeeperTask;

public class CreateTableTask implements ZookeeperTask {
    String tableName;
    String peerip;
    int peerPort;

    Class keyType;
    Class valueType;

    public CreateTableTask(String peerIp, int peerPort, String tableName, Class keyType, Class valueType) {
        this.tableName = tableName;
        this.keyType = keyType;
        this.valueType = valueType;
        this.peerip = peerIp;
        this.peerPort = peerPort;
    }

    @Override
    public void performOnSuccess(Promise returnedPromise) {
        if (returnedPromise instanceof CreateTableResult x) {
            System.out.println("Table created successfully on Node" + x.uuid);
        }
    }

    @Override
    public Promise perform() {
        Tables.getInstance().createTable(tableName, keyType, valueType);
        return new CreateTableResult(true, PeerCommunication.getInstance().getUuid().toString());
    }



    @Override
    public String getIpAddress() {
        return peerip;
    }

    @Override
    public int getPort() {
        return peerPort;
    }

    private static class CreateTableResult implements Promise {
        boolean success;
        String uuid;
        public CreateTableResult(boolean b, String NodeUuid) {
            success = b;
            uuid = NodeUuid;
        }

        @Override
        public boolean isSuccess() {
            return success;
        }
    }
}
