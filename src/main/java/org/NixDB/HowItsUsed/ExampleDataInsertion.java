package org.NixDB.HowItsUsed;

import org.NixDB.Client.NixDBConnection;

import java.util.Scanner;

public class ExampleDataInsertion {
    public static void main(String[] args) {



        // Creating connections for "Customer" and "Transaction" tables
        NixDBConnection<String, Customer> customerConnection = new NixDBConnection<>("Customer", "localhost", 2181, String.class, Customer.class);
        NixDBConnection<String, Transaction> transactionConnection = new NixDBConnection<>("Transaction", "localhost", 2181, String.class, Transaction.class);
        NixDBConnection<Integer, Double> distributionTest = new NixDBConnection<>("DistributionTest1", "localhost", 2181, Integer.class, Double.class);
        NixDBConnection<Double, Integer> distributionTest2 = new NixDBConnection<>("DistributionTest2", "localhost", 2181, Double.class, Integer.class);
        System.out.println("Transaction Connection Designated Node IP Address: ");
        System.out.println(transactionConnection.getDesignatedNodeIpAddress());
        System.out.println("Transaction Connection Designated Node Port: ");
        System.out.println(transactionConnection.getDesignatedNodePort());
        System.out.println("Customer Connection Designated Node IP Address: ");
        System.out.println(customerConnection.getDesignatedNodeIpAddress());
        System.out.println("Customer Connection Designated Node Port: ");
        System.out.println(customerConnection.getDesignatedNodePort());

        System.out.println("Inserting Data");
        customerConnection.put("C001", new Customer("C001", "John Doe", 25, "john@example.com"));
        customerConnection.put("C002", new Customer("C002", "Alice Smith", 30, "alice@example.com"));
        customerConnection.put("C003", new Customer("C003", "AHHH Wrong", 30, "alice@example.com"));

        System.out.println("Removeing one data point");
        customerConnection.remove("C003");

        System.out.println("Inserting Data");
        transactionConnection.put("T001", new Transaction("T001", "C001", 100.50, "2022-01-01"));
        transactionConnection.put("T002", new Transaction("T002", "C001", 50.75, "2022-01-02"));
        transactionConnection.put("T003", new Transaction("T003", "C002", 75.20, "2022-01-03"));
        System.out.println("Data Inserted");
        new Scanner(System.in).nextLine();
    }





}
