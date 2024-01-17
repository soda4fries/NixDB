package org.NixDB.Client;

import org.NixDB.PeerCommunication.Promise;

public class Value implements Promise {
    Object value;

    public Value(Object value) {
        this.value = value;
    }
    @Override
    public boolean isSuccess() {
        return value != null;
    }

    public Object getValue() {
        return value;
    }
}
