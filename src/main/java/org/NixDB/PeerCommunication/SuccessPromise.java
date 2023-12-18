package org.NixDB.PeerCommunication;

public class SuccessPromise implements Promise {
    private String message;

    public SuccessPromise(String message) {
        this.message = message;
    }
    @Override
    public boolean isSuccess() {
        return true;
    }

    public String getMessage() {
        return message;
    }
}
