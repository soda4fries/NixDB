package org.NixDB.PeerTasks;

import org.NixDB.PeerCommunication.Task;



public interface PeerTask extends Task {
    String getReceiverPeerUUID();
}

