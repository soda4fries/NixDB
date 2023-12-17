package org.NixDB.PeerCommunication;

// TimeoutAcknowledgment class implementing Acknowledgment for timeout scenarios
class TimeoutPromise implements Promise {

    @Override
    public boolean isSuccess() {
        return false;
    }
}
