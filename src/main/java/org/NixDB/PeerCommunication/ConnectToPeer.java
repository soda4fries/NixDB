package org.NixDB.PeerCommunication;

import java.util.UUID;

public class ConnectToPeer implements Task {
    String ipAddress;
    int port;



    ConnectToPeer(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public Promise perform() {
        PeerCommunication peerCommunication = PeerCommunication.getInstance();
        return new UUIDpromise(peerCommunication.getUuid());
    }

    static class UUIDpromise implements Promise  {
        UUID uuid;
        UUIDpromise(UUID uuid) {
            this.uuid = uuid;
        }
    }

    @Override
    public void Success(Promise returnedPromise) {
        if (returnedPromise instanceof UUIDpromise x) {
            PeerCommunication peerCommunication = PeerCommunication.getInstance();
            peerCommunication.addPeerToList(x.uuid.toString(), ipAddress, port);
            System.out.println("Connected to Peer with UUID " + x.uuid.toString());
        }
    }


}
