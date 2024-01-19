package org.NixDB.Zookeeper;
import java.io.Serializable;

public class TablesEntry implements Serializable {
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

    @Override
    public String toString() {
        String sb = "+--------------------------+\n" +
                String.format("| %-20s |\n", "Table Entry") +
                "+--------------------------+\n" +
                String.format("| %-20s |\n", "Table Name: " + tableName) +
                String.format("| %-20s |\n", "Key Type: " + keyType) +
                String.format("| %-20s |\n", "Value Type: " + valueType) +
                "+--------------------------+\n";
        return sb;
    }


}
