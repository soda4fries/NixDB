package org.NixDB.HowItsUsed;

import org.NixDB.Client.NixDBConnection;

import org.NixDB.Datastructures.MyHashTable;
import org.NixDB.Datastructures.MyLinkedList;
import org.NixDB.Zookeeper.TablesEntry;

import java.util.Scanner;


public class ExampleCalculation {
    public static void main(String[] args) {


        //Print all the tables in the database
        MyLinkedList<TablesEntry> tables = NixDBConnection.getTableData("localhost", 2181);
        for (TablesEntry table : tables) {
            System.out.println(table);
        }

        // Creating connections for "Customer" and "Transaction" tables
        NixDBConnection<String, Customer> customerConnection = new NixDBConnection<>("Customer", "localhost", 2181, String.class, Customer.class);
        NixDBConnection<String, Transaction> transactionConnection = new NixDBConnection<>("Transaction", "localhost", 2181, String.class, Transaction.class);

        // Performing some calculations or operations on the data
        performCalculations(transactionConnection);


        // Printing all the data in the database
        MyHashTable<String, Customer> customers = customerConnection.getAll();
        for (Customer customer : customers.values()) {
            System.out.println(customer);
        }
        MyHashTable<String, Transaction> transactions = transactionConnection.getAll();
        for (Transaction transaction : transactions.values()) {
            System.out.println(transaction);
        }
        new Scanner(System.in).nextLine();

    }

    private static void performCalculations(NixDBConnection<String, Transaction> transactionConnection) {
        // Example: Calculate the total transaction amount for a specific customer
        String customerId = "C001";
        double totalTransactionAmount = calculateTotalTransactionAmount(customerId, transactionConnection);
        System.out.println("Total transaction amount for Customer " + customerId + ": " + totalTransactionAmount);
    }

    private static double calculateTotalTransactionAmount(String customerId,
                                                          NixDBConnection<String, Transaction> transactionConnection) {
        double totalAmount = 0.0;
        MyHashTable<String, Transaction> transactions = transactionConnection.getAll();

        for (Transaction transaction : transactions.values()) {
            if (transaction.getCustomerId().equals(customerId)) {
                System.out.println(transaction.getAmount());
                totalAmount += transaction.getAmount();
            }
        }

        System.out.println("Total Amount: " + totalAmount);

        return totalAmount;
    }
}
