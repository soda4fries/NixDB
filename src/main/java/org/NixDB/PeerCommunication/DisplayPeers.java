package org.NixDB.PeerCommunication;

public class DisplayPeers implements Message{
    @Override
    public void perform() {
        PeerCommunication.getInstance().printPeers();
    }
}
