package org.NixDB.Zookeeper;


import org.NixDB.PeerCommunication.PeerCommunication;
import org.NixDB.PeerCommunication.Promise;

import java.util.UUID;

public class ConnectToPeer implements ZookeeperTask {
    private final String ipAddress;
    private final int port;



    public ConnectToPeer(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void performOnSuccess(Promise returnedPromise) {
        if (returnedPromise instanceof UUIDpromise x) {
            PeerCommunication peerCommunication = PeerCommunication.getInstance();
            peerCommunication.addPeerToList(x.uuid.toString(), ipAddress, port);
            System.out.println("Connected to Peer with UUID " + x.uuid.toString());
        }
    }

    @Override
    public Promise perform() {
        PeerCommunication peerCommunication = PeerCommunication.getInstance();
        return new UUIDpromise(peerCommunication.getUuid());
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public int getPort() {
        return port;
    }

    static public class UUIDpromise implements Promise  {
        public UUID uuid;
        UUIDpromise(UUID uuid) {
            this.uuid = uuid;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }
    }




}