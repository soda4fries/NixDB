package org.NixDB.DataStore;

import org.NixDB.Datastructures.MyHashTable;

import java.io.Serializable;
import java.util.HashMap;

public class Tables implements Serializable {
    private static Tables instance = null;

    HashMap<String, Table> tables;

    public Tables() {
        tables = new HashMap<>();
    }

    public static Tables getInstance() {
        if (instance == null) {
            instance = new Tables();
        }
        return instance;
    }

    public Object getValue(String tableName, Object key) {
        return tables.get(tableName).data.get(key);
    }

    public Object putValue(String tableName, Object key, Object value) {
        return tables.get(tableName).data.put(key, value);
    }

    public Object removeValue(String tableName, Object key) {
        return tables.get(tableName).data.remove(key);
    }

    public boolean containsKey(String tableName, Object key) {
        return tables.get(tableName).data.containsKey(key);
    }

    public int size(String tableName) {
        return tables.get(tableName).data.size();
    }

    public boolean isEmpty(String tableName) {
        return tables.get(tableName).data.isEmpty();
    }

    public void clear(String tableName) {
        tables.get(tableName).data.clear();
    }


    public void createTable(String tableName, Class keyType, Class valueType) {
        Table table = new Table();
        table.name = tableName;
        table.keyType = keyType;
        table.valueType = valueType;
        table.data = new MyHashTable<>();
        tables.put(tableName, table);
    }
}

 class Table implements Serializable {
    String name;
    Class keyType;
    Class valueType;
    MyHashTable<Object, Object> data;
}
