package org.NixDB.Zookeeper;

public class TablesEntry {
    String tableName;
    Class keyType;
    Class valueType;

    TablesEntry(String tableName, Class keyType, Class valueType) {
        this.tableName = tableName;
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public String getTableName() {
        return tableName;
    }

    public Class getKeyType() {
        return keyType;
    }

    public Class getValueType() {
        return valueType;
    }
}
