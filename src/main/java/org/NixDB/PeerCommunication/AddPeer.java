package org.NixDB.PeerCommunication;

public class AddPeer implements Message {
    private String name;
    private String ipAddress;
    private int port;

    public AddPeer(String name, String ipAddress, int port) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void perform() {
        PeerCommunication.getInstance().addPeer(this.name, this.ipAddress, this.port);
    }
}
