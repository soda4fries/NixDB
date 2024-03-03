package org.NixDB.Client;

import org.NixDB.PeerCommunication.Promise;

public class ConnectToDBResult implements Promise {
    boolean success;

    String designatedPeerIp;
    int designatedPeerPort;

    public ConnectToDBResult(boolean b, String designatedPeerIp, int designatedPeerPort) {
        success = b;
        this.designatedPeerIp = designatedPeerIp;
        this.designatedPeerPort = designatedPeerPort;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }
}
