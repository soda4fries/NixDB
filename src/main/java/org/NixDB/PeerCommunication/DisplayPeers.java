package org.NixDB.PeerCommunication;

public class DisplayPeers implements Task {
    @Override
    public Promise perform() {
        PeerCommunication.getInstance().printPeers();
        return new SuccessPromise();
    }

    @Override
    public void Success(Promise returnedPromise) {
        System.out.println("Successfully added Peer");
    }
}
