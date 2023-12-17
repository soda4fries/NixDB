package org.NixDB.PeerCommunication;


class TimeoutPromise implements Promise {

    @Override
    public boolean isSuccess() {
        return false;
    }
}
