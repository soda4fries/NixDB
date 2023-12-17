package org.NixDB.PeerCommunication;

import java.io.Serializable;

public interface Promise extends Serializable {

    boolean isSuccess();

}
