package org.NixDB.HowItsUsed;


public class Transaction implements java.io.Serializable {
    private final String transactionId;
    private final String customerId;
    private final double amount;
    private final String date;

    public Transaction(String transactionId, String customerId, double amount, String date) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.amount = amount;
        this.date = date;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    // Add getters and setters as needed

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                '}';
    }
}
