package org.NixDB.PeerCommunication;

import java.io.Serializable;


interface Task extends Serializable {
    Promise perform();
    String getReceiverPeerUUID();



    void Success(Promise returnedPromise);
}

