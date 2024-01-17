package org.NixDB.PeerTasks;

import org.NixDB.PeerCommunication.Promise;

public class getPeerDataResult implements Promise {
    Object value;
    String uuid;

    public getPeerDataResult(Object value, String NodeUuid) {
        this.value = value;
        uuid = NodeUuid;
    }

    @Override
    public boolean isSuccess() {
        return value != null;
    }

    public Object getValue() {
        return value;
    }
}
