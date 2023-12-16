package org.NixDB.PeerCommunication;

public class AddPeer implements Task {
    private String name;
    private String ipAddress;
    private int port;

    public AddPeer(String name, String ipAddress, int port) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public Promise perform() {
        PeerCommunication.getInstance().addPeer(this.name, this.ipAddress, this.port);
        return new SuccessPromise();
    }

    @Override
    public void Success(Promise returnedPromise) {
        System.out.println("Successfully added Peer");
    }
}
