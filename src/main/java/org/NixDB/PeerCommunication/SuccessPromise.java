package org.NixDB.PeerCommunication;

class SuccessPromise implements Promise {

    @Override
    public boolean isSuccess() {
        return true;
    }
}
