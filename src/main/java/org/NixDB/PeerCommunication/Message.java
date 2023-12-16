package org.NixDB.PeerCommunication;

import java.io.Serializable;

// Message interface with the perform method
interface Message extends Serializable {
    void perform();
}
