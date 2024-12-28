package com.group15;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Facade {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cinema_db"; // Replace with your DB name
    private static final String DB_USER = "root"; // Replace with your username
    private static final String DB_PASSWORD = "ubkt1234"; // Replace with your password

    private Connection connect() throws Exception {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Method to validate user login
    public User validateLogin(String username, String password) {
        String query = "SELECT username, password, role FROM users WHERE username = ? AND password = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Extract user details
                String fetchedUsername = rs.getString("username");
                String fetchedPassword = rs.getString("password");
                String fetchedRole = rs.getString("role");

                // Create a new User object
                User user = new User(fetchedUsername, fetchedPassword, fetchedRole);
                return user; // Return the created User object
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if no user is found or an error occurs
    }
}

