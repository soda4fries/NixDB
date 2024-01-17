package org.NixDB.Client;


import org.NixDB.PeerCommunication.PeerCommunication;
import org.NixDB.PeerCommunication.Promise;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class NixDBConnection<K, V> {

    String tableName;
    String ZooKeeperIpAddress;
    int ZooKeeperPort;
    private Class<K> keyType;
    private Class<V> valueType;

    String clientId;
    String designatedNodeIpAddress;
    int designatedNodePort;



    public NixDBConnection(String tableName, String ZooKeeperIpAddress, int ZooKeeperPort, Class<K> keyType, Class<V> valueType) {
        this.tableName = tableName;
        this.ZooKeeperIpAddress = ZooKeeperIpAddress;
        this.ZooKeeperPort = ZooKeeperPort;
        clientId = "client-" + System.currentTimeMillis();
        this.keyType = keyType;
        this.valueType = valueType;
        connect();
    }

    private void connect() {
        Promise promise = PeerCommunication.getInstance().sendTask(new ConnectToDBTask(ZooKeeperIpAddress, ZooKeeperPort, tableName, keyType, valueType));
        if (promise instanceof ConnectToDBResult x) {
            designatedNodeIpAddress = x.designatedPeerIp;
            designatedNodePort = x.designatedPeerPort;
        }
    }

    public void put(K key, V value) {
        PeerCommunication.getInstance().sendTask(new PutData(designatedNodeIpAddress, designatedNodePort, tableName, key, value));
    }

    public V get(K key) {
        Promise promise = PeerCommunication.getInstance().sendTask(new GetData(designatedNodeIpAddress, designatedNodePort, tableName, key));
        if (promise instanceof Value x) {
            return (V) x.getValue();
        } else {
            return null;
        }
    }

    public void remove(K key) {
        PeerCommunication.getInstance().sendTask(new RemoveData(designatedNodeIpAddress, designatedNodePort, tableName, key));
    }




}
