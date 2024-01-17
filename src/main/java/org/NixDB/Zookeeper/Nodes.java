package org.NixDB.Zookeeper;

public class Nodes {
    private String uuid;
    private String ip;
    private int port;

    Nodes(String uuid, String ip, int port) {
        this.uuid = uuid;
        this.ip = ip;
        this.port = port;
    }

    public String getUuid() {
        return uuid;
    };

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }


}
