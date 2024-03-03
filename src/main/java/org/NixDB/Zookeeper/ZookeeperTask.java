package org.NixDB.Zookeeper;

import org.NixDB.PeerCommunication.Task;

public interface ZookeeperTask extends Task {
    String getIpAddress();
    int getPort();

}
