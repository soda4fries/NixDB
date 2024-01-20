package org.NixDB.HowItsUsed;

import org.NixDB.Client.NixDBConnection;

import java.io.Serializable;

public class NixDBExample {

    public static void main(String[] args) {
        // Creating a connection for the "User" table
        NixDBConnection<String, User> userConnection = new NixDBConnection<>("User", "localhost", 2181, String.class, User.class);
        // Inserting user data
        userConnection.put("U001", new User("U001", "John Doe", 25, "john@example.com"));
        userConnection.put("U002", new User("U002", "Alice Smith", 30, "alice@example.com"));

        // Retrieving and displaying user data
        User retrievedUser = userConnection.get("U001");
        System.out.println("Retrieved User: " + retrievedUser);

        // Updating user data
        userConnection.put("U001", new User("U001", "John Doe", 26, "doe@gmail.com"));

        // Retrieving and displaying updated user data
        retrievedUser = userConnection.get("U001");
        System.out.println("Updated User: " + retrievedUser);

        System.out.println("All Users:");
        System.out.println("All Users:");
        for (User user : userConnection.getAll().values()) {
            System.out.println(user);
        }
        System.out.println("Deleting User U002");
        // Deleting user data
        userConnection.remove("U002");
        // Retrieving all users and displaying them
        System.out.println("All Users:");
        for (User user : userConnection.getAll().values()) {
            System.out.println(user);
        }

        new java.util.Scanner(System.in).nextLine();
    }

    // User class representing the data structure
    static class User implements Serializable {
        private String userId;
        private String name;
        private int age;
        private String email;

        public User(String userId, String name, int age, String email) {
            this.userId = userId;
            this.name = name;
            this.age = age;
            this.email = email;
        }

        @Override
        public String toString() {
            return "User{" +
                    "userId='" + userId + '\'' +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", email='" + email + '\'' +
                    '}';
        }
    }
}
