package org.NixDB.PeerTasks;

import org.NixDB.PeerCommunication.Promise;

public class addNumberPeerTask implements PeerTask {
    private final int num1;
    private final int num2;

    String ReceiverPeerName;

    public addNumberPeerTask(String ReceiverPeerName, int num1, int num2) {
        this.num1 = num1;
        this.num2 = num2;
        this.ReceiverPeerName = ReceiverPeerName;
    }

    @Override
    public Promise perform() {
        int result = num1 + num2;
        System.out.println("Result of adding numbers: " + result);
        return new AddedNumberResult(result);
    }

    @Override
    public String getReceiverPeerUUID() {
        return ReceiverPeerName;
    }

    class AddedNumberResult implements Promise {
        int result;
        AddedNumberResult(int result) {
            this.result = result;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }
    }

    @Override
    public void Success(Promise returnedPromise) {
        if (returnedPromise instanceof AddedNumberResult x) {
            System.out.println("Result from remote: " + x.result);
        } else System.out.println("error");
    }
}
