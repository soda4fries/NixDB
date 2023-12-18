package org.NixDB.PeerCommunication;

import java.io.Serializable;

public interface Task extends Serializable {
    void Success(Promise returnedPromise);
    Promise perform();
}
