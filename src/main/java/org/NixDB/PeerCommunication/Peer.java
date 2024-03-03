package org.NixDB.PeerCommunication;

public class Peer {
    private final String name;
    private final String ipAddress;
    private final int port;

    public Peer(String name, String ipAddress, int port) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }
}
