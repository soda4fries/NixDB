package org.NixDB.PeerCommunication;

class Peer {
    private String name;
    private String ipAddress;
    private int port;

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
