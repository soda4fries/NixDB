package org.NixDB.PeerCommunication;

import java.io.Serializable;

// Message interface with the perform method
interface Task extends Serializable {
    Promise perform();

    void Success(Promise returnedPromise);
}

