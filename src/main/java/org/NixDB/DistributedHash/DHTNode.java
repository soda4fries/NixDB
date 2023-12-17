package org.NixDB.DistributedHash;

import org.NixDB.Datastructures.MyHashTable;
import org.NixDB.PeerCommunication.ForwardData;
import org.NixDB.PeerCommunication.PeerCommunication;
import org.NixDB.PeerCommunication.Promise;
import org.NixDB.PeerCommunication.ReceiveDataTask;

import java.util.List;


public class DHTNode {

    static DHTNode NodeSingleTon;

    private final PeerCommunication peerCommunication;

    private final String uuid;
    private final MyHashTable<String, String> dataStore;

    public static DHTNode getNodeSingleTon() {
        if (NodeSingleTon==null) {
            NodeSingleTon = new DHTNode();
        }
        return NodeSingleTon;
    }

    private DHTNode() {
        this.dataStore = new MyHashTable<>();
        peerCommunication = PeerCommunication.getInstance();
        this.uuid = peerCommunication.getUuid().toString();
    }

    public String getUUID() {
        return peerCommunication.getUuid().toString();
    }

    public String getDataStoreAsStr() {
        return dataStore.toString();
    }

    public String getData(String key) {
        String responsibleNode = findResponsibleNode(key);
        if (responsibleNode.equals(uuid)) {
            System.out.printf("%s -> %s Data returned from %s", key, dataStore.get(key), uuid);
            return dataStore.get(key);

        } else {
            // Forward the data to the responsible node
            return remoteDataReceiveHelper(responsibleNode, key);
        }
    }

    public void putData(String key, String data) {
        String responsibleNode = findResponsibleNode(key);
        if (responsibleNode.equals(uuid)) {
            // This node is responsible for the data
            dataStore.put(key, data);
            System.out.printf("<%s,%s> saved", key,data);
        } else {
            // Forward the data to the responsible node
            forwardData(responsibleNode, key, data);
        }
    }

    private String findResponsibleNode(String key) {
        String responsibleNode = null;
        double maxHash = Double.NEGATIVE_INFINITY;
        peerCommunication.getPeers().keys().add(uuid);
        List<String> uuidlist = peerCommunication.getPeers().keys();
        uuidlist.add(uuid);
        for (String UUID : uuidlist) {
            double hash = calculateHash(UUID, key);
            if (hash > maxHash) {
                maxHash = hash;
                responsibleNode = UUID;
            }
        }

        return responsibleNode;
    }

    private double calculateHash(String nodeUUID, String key) {
        // Implement your hash function logic here
        String concatenated = nodeUUID + key;
        return (double) concatenated.hashCode();
    }

    private void forwardData(String destinationNode, String key, String data) {
        System.out.printf("<%s,%s> forwarded", key,data);
        peerCommunication.sendTask(new ForwardData(destinationNode, key, data));
    }

    private String remoteDataReceiveHelper(String UUID, String key) {
        Promise promise = peerCommunication.sendTask(new ReceiveDataTask(UUID, key));
        if (promise instanceof ReceiveDataTask.ReceivedDataResult x) {
            System.out.printf("Data returned from ", x.ownerUUID);
            return x.getResult();
        } return "Data not Found";
    }

    public static void main(String[] args) {

    }
}
