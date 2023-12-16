package org.NixDB.PeerCommunication;

// AddMessage class implementing the Message interface for adding numbers
public class addNumberTask implements Task {
    private int num1;
    private int num2;

    public addNumberTask(int num1, int num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    @Override
    public Promise perform() {
        int result = num1 + num2;
        System.out.println("Result of adding numbers: " + result);
        return new AddedNumberResult(result);
    }

    class AddedNumberResult implements Promise {
        int result;
        AddedNumberResult(int result) {
            this.result = result;
        }
    }

    @Override
    public void Success(Promise returnedPromise) {
        if (returnedPromise instanceof AddedNumberResult x) {
            System.out.println("Result from remote: " + x.result);
        } else System.out.println("error");
    }
}
