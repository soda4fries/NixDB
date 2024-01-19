package org.NixDB;

import org.NixDB.Client.NixDBConnection;
import org.NixDB.Zookeeper.TablesEntry;

import java.util.Objects;
import java.util.Scanner;

public class ExampleClientCode {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Zoo Keeper Ip address: ");
        String input = scanner.nextLine();
        System.out.println("Zoo Keeper Port: ");
        int port = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Table Name: ");
        String tableName = scanner.nextLine();
        NixDBConnection<String, String> connection = new NixDBConnection<String, String>(tableName, input, port, String.class, String.class);
        while (true) {
            System.out.println("Enter a command:");
            String command = scanner.nextLine();
            String[] commandParts = command.split(" ");
            if (commandParts[0].equals("put")) {
                connection.put(commandParts[1], commandParts[2]);
            } else if (commandParts[0].equals("get")) {
                System.out.println(connection.get(commandParts[1]));
            } else if (commandParts[0].equals("remove")) {
                connection.remove(commandParts[1]);
            } else if (commandParts[0].equals("exit")) {
                System.exit(0);
            } else if (commandParts[0].equals("getAll")) {
                System.out.println(connection.getAll());
            } else if (commandParts[0].equals("getTableNames")) {
               for (TablesEntry tableName1 : Objects.requireNonNull(NixDBConnection.getTableData(input, port))) {
                     System.out.println(tableName1);
               }
            }
        }

    }

}
