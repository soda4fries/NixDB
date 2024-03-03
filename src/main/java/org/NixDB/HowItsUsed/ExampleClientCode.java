package org.NixDB.HowItsUsed;

import org.NixDB.Client.NixDBConnection;
import org.NixDB.Zookeeper.TablesEntry;

import java.util.Objects;
import java.util.Scanner;

public class ExampleClientCode {
    public static void main(String[] args) {
        System.out.println("Hello World! this is a interactive client for NixDB");
        String input = "localhost";
        int port = 2181;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a table name:");
        String tableName = scanner.nextLine();
        NixDBConnection<String, String> connection = new NixDBConnection<>(tableName, input, port, String.class, String.class);
        while (true) {
            System.out.println("Enter a command:");


            String command = scanner.nextLine();
            String[] commandParts = command.split(" ");
            switch (commandParts[0]) {
                case "put" -> connection.put(commandParts[1], commandParts[2]);
                case "get" -> System.out.println(connection.get(commandParts[1]));
                case "remove" -> connection.remove(commandParts[1]);
                case "exit" -> System.exit(0);
                case "getAll" -> System.out.println(connection.getAll());
                case "getTableNames" -> {
                    for (TablesEntry tableName1 : Objects.requireNonNull(NixDBConnection.getTableData(input, port))) {
                        System.out.println(tableName1);
                    }
                }
                case "help" -> System.out.println("""
                        put <key> <value> - Put a value in a table
                        get <key> - Get a value from a table
                        remove <key> - Remove a value from a table
                        getAll - Get all the values from a table
                        getTableNames - Get all the table names
                        exit - Exit the program
                        """);
            }
        }

    }

}
