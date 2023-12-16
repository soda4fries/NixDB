package org.NixDB.PeerCommunication;

// PrintMessage class implementing the Message interface for printing a string
public class PrintMessage implements Message {
    private String str;

    public PrintMessage(String str) {
        this.str = str;
    }

    @Override
    public void perform() {
        System.out.println("Received string: " + str);
    }
}
