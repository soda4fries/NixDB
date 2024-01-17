package org.NixDB.PeerCommunication;

import java.io.Serializable;

public interface Task extends Serializable {
    void performOnSuccess(Promise returnedPromise);
    Promise perform();
}
