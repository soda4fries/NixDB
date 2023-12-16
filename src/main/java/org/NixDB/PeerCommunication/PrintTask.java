package org.NixDB.PeerCommunication;

// PrintMessage class implementing the Message interface for printing a string
public class PrintTask implements Task {
    private String str;

    public PrintTask(String str) {
        this.str = str;
    }

    @Override
    public Promise perform() {
        System.out.println("Received string: " + str);
        return new SuccessPromise();
    }

    @Override
    public void Success(Promise returnedPromise) {
        System.out.println("Successfully Printed");
    }
}
