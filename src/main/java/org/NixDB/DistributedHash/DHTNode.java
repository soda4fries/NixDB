package org.NixDB.DistributedHash;

import org.NixDB.Datastructures.MyHashTable;
import org.NixDB.PeerTasks.ForwardData;
import org.NixDB.PeerCommunication.PeerCommunication;
import org.NixDB.PeerCommunication.Promise;
import org.NixDB.PeerTasks.ReceiveDataPeerTask;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
            System.out.printf("%s -> %s Data returned from %s\n", key, dataStore.get(key), uuid);
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
            System.out.printf("<%s,%s> saved\n", key,data);
        } else {
            // Forward the data to the responsible node
            forwardData(responsibleNode, key, data);
        }
    }

    private String findResponsibleNode(String key) {
        String responsibleNode = null;

        peerCommunication.getPeers().keys().add(uuid);
        List<String> uuidlist = peerCommunication.getPeers().keys();
        uuidlist.add(uuid);
        Long maxHash = null;
        for (String UUID : uuidlist) {
            if (maxHash==null) {
                maxHash= calculateHash(UUID, key);
                responsibleNode = UUID;
                continue;
            }
            long hash = calculateHash(UUID, key);
            if (hash > maxHash) {
                maxHash = hash;
                responsibleNode = UUID;
            }
        }

        return responsibleNode;
    }

    private long calculateHash(String nodeUUID, String key) {
        String concatenated = nodeUUID + key;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(concatenated.getBytes());


            long hashLong = 0;
            for (int i = 0; i < Math.min(hashBytes.length, 8); i++) {
                hashLong <<= 8;
                hashLong |= hashBytes[i] & 0xFF;
            }
            return hashLong;

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
            return 0L; //
        }
    }


    private void forwardData(String destinationNode, String key, String data) {
        System.out.printf("<%s,%s> forwarded\n", key,data);
        peerCommunication.sendTask(new ForwardData(destinationNode, key, data));
    }

    private String remoteDataReceiveHelper(String UUID, String key) {
        Promise promise = peerCommunication.sendTask(new ReceiveDataPeerTask(UUID, key));
        if (promise instanceof ReceiveDataPeerTask.ReceivedDataResult x) {
            System.out.printf("Data returned from %s\n", x.ownerUUID);
            return x.getResult();
        } return "Data not Found";
    }

    public static void main(String[] args) {

    }
}
