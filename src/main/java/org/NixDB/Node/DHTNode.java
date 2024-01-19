package org.NixDB.Node;

import org.NixDB.DataStore.Table;
import org.NixDB.Datastructures.MyLinkedList;
import org.NixDB.PeerCommunication.PeerCommunication;
import org.NixDB.PeerCommunication.Promise;
import org.NixDB.PeerTasks.*;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


public class DHTNode {

    static DHTNode NodeSingleTon;

    private final PeerCommunication peerCommunication;

    private final String uuid;

    public static DHTNode getNodeSingleTon() {
        if (NodeSingleTon==null) {
            NodeSingleTon = new DHTNode();
        }
        return NodeSingleTon;
    }

    private DHTNode() {
        peerCommunication = PeerCommunication.getInstance();
        this.uuid = peerCommunication.getUuid().toString();
    }

    public String getUUID() {
        return peerCommunication.getUuid().toString();
    }

    public void put(String tableName, Object key, Object value) {
        List<String> responsibleNodes = findResponsibleNodes(key.toString());
        //for (String nodeUUID : responsibleNodes) {
        //    peerCommunication.sendTask(new putPeerDataTask(nodeUUID, tableName, key, value));
        //}
        peerCommunication.sendTask(new putPeerDataTask(responsibleNodes.get(0), tableName, key, value));
    }

    public Object get(String tableName, Object key) {
        List<String> responsibleNodes = findResponsibleNodes(key.toString());
        Promise promise1 = peerCommunication.sendTask(new getPeerDataTask(responsibleNodes.get(0), tableName, key));
      //  Promise promise2 = peerCommunication.sendTask(new getPeerDataTask(responsibleNodes.get(1), tableName, key));
     //   if (promise1.isSuccess() && promise2.isSuccess()) {
     //       if (((getPeerDataResult) promise1).getValue().equals(((getPeerDataResult) promise2).getValue())) {
    //            return ((getPeerDataResult) promise1).getValue();
    //        } else {
    //            System.out.println("Data is inconsistent");
     //           System.out.println("one node has " + ((getPeerDataResult) promise1) + " has " + ((getPeerDataResult) promise1).getValue());
      //          System.out.println("other node has " + ((getPeerDataResult) promise2) + " has " + ((getPeerDataResult) promise2).getValue());
       //         return null;
       //     }
     //   }
     //   return null;
        return ((getPeerDataResult) promise1).getValue();
    }

    public MyLinkedList<Table> getAll(String tableName) {
        MyLinkedList<Table> tables = new MyLinkedList<>();
        for (String nodeUUID : peerCommunication.getPeers().keys()) {
            Promise promise = peerCommunication.sendTask(new getPeerAllDataTask(nodeUUID, tableName));
            if (promise.isSuccess()) {
                tables.add((Table) ((getPeerDataResult) promise).getValue());
            }
        }
        return tables;
    }

    public void remove(String tableName, Object key) {
        List<String> responsibleNodes = findResponsibleNodes(key.toString());
    //    System.out.println("responsible nodes are " + responsibleNodes);
    //    for (String nodeUUID : responsibleNodes) {
    //        peerCommunication.sendTask(new removePeerDataTask(nodeUUID, tableName, key));
    //    }
        peerCommunication.sendTask(new removePeerDataTask(responsibleNodes.get(0), tableName, key));
    }



    private List<String> findResponsibleNodes(String key) {
        List<String> responsibleNodes = new LinkedList<>();

        peerCommunication.getPeers().keys().add(uuid);
        List<String> uuidList = new LinkedList<>(peerCommunication.getPeers().keys());
        uuidList.add(uuid);

        for (String UUID : uuidList) {
            long hash = calculateHash(UUID, key);

            if (responsibleNodes.size() < 3) {
                responsibleNodes.add(UUID);
                responsibleNodes.sort(Comparator.comparingLong(u -> calculateHash(u, key)));
            } else if (hash > calculateHash(responsibleNodes.get(0), key)) {
                responsibleNodes.remove(0);
                responsibleNodes.add(UUID);
                responsibleNodes.sort(Comparator.comparingLong(u -> calculateHash(u, key)));
            }
        }

        return responsibleNodes;
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





}
