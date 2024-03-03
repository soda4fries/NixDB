package org.NixDB.HowItsUsed;


public class Customer implements java.io.Serializable {
    private final String customerId;
    private final String name;
    private final int age;
    private final String email;

    public Customer(String customerId, String name, int age, String email) {
        this.customerId = customerId;
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    // Add getters and setters as needed

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }
}

