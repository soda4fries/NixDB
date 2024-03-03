package org.NixDB.Client;


import org.NixDB.DataStore.Table;
import org.NixDB.Datastructures.MyHashTable;
import org.NixDB.Datastructures.MyLinkedList;
import org.NixDB.PeerCommunication.PeerCommunication;
import org.NixDB.PeerCommunication.Promise;
import org.NixDB.Zookeeper.TablesEntry;


public class NixDBConnection<K, V> {

    String tableName;
    String ZooKeeperIpAddress;
    int ZooKeeperPort;
    private final Class<K> keyType;
    private final Class<V> valueType;

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
    public static MyLinkedList<TablesEntry> getTableData(String ZooKeeperIpAddress, int ZooKeeperPort) {
        Promise promise = PeerCommunication.getInstance().sendTask(new GetTableNamesTask(ZooKeeperIpAddress, ZooKeeperPort));
        if (promise instanceof Value x) {
            return (MyLinkedList<TablesEntry>) x.getValue();
        } else {
            return null;
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

    public MyHashTable<K,V> getAll() {
        MyHashTable<K, V> data = new MyHashTable<>();
        Promise promise = PeerCommunication.getInstance().sendTask(new GetAllData(designatedNodeIpAddress, designatedNodePort, tableName));
        if (promise instanceof Value x) {
            MyLinkedList<Table> tables = (MyLinkedList<Table>) x.getValue();
            for (Table table : tables) {
                for (Object key : table.getData().keys()) {
                    K key1 = (K) key;
                    V value = (V) table.getData().get(key1);
                    data.put(key1, value);
                }
            }
        }
        return data;
    }

    public void remove(K key) {
        PeerCommunication.getInstance().sendTask(new RemoveData(designatedNodeIpAddress, designatedNodePort, tableName, key));
    }

    public String getDesignatedNodeIpAddress() {
        return designatedNodeIpAddress;
    }

    public int getDesignatedNodePort() {
        return designatedNodePort;
    }

    public String getZooKeeperIpAddress() {
        return ZooKeeperIpAddress;
    }

    public int getZooKeeperPort() {
        return ZooKeeperPort;
    }
}
