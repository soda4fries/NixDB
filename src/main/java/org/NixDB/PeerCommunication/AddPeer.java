package org.NixDB.PeerCommunication;

public class AddPeer implements Task {
    private final String ipAddress;
    private final int port;

    public AddPeer(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public Promise perform() {
        PeerCommunication peerCommunication = PeerCommunication.getInstance();
        peerCommunication.connectNewPeer(ipAddress, port);
        return new SuccessPromise();
    }

    @Override
    public void Success(Promise returnedPromise) {
        if (returnedPromise instanceof ConnectToPeer.UUIDpromise x) {
            PeerCommunication peerCommunication = PeerCommunication.getInstance();
            peerCommunication.addPeerToList(x.uuid.toString(), ipAddress, port);
            System.out.println("Connected to Peer with UUID " + x.uuid.toString());
        }
    }
}
