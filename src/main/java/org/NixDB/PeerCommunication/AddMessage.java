package org.NixDB.PeerCommunication;

// AddMessage class implementing the Message interface for adding numbers
public class AddMessage implements Message {
    private int num1;
    private int num2;

    public AddMessage(int num1, int num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    @Override
    public void perform() {
        int result = num1 + num2;
        System.out.println("Result of adding numbers: " + result);
    }
}
