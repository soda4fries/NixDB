package org.NixDB.Zookeeper;

public class Clients {
    String id;
    String ip;
    int port;

    Clients(String id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }
}
