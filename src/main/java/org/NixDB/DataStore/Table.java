package org.NixDB.DataStore;

import org.NixDB.Datastructures.MyHashTable;

import java.io.Serializable;

public class Table implements Serializable {
    String name;
    Class keyType;
    Class valueType;
    MyHashTable<Object, Object> data;

    public MyHashTable<Object, Object> getData() {
        return data;
    }


}
