package org.NixDB.Zookeeper;

public class Clients {
    private String id;
    private String ip;
    private int port;

    Clients(String id, String ip, int port) {
        this.setId(id);
        this.setIp(ip);
        this.setPort(port);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
