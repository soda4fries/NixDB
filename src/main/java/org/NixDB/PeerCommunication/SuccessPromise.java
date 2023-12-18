package org.NixDB.PeerCommunication;

public class SuccessPromise implements Promise {

    @Override
    public boolean isSuccess() {
        return true;
    }
}
