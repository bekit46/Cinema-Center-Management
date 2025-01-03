package com.group15;

import java.util.Objects;

public class User {
    private int userId;
    private String username;
    private String name;
    private String surname;
    private String password;
    private String role;

    // No-argument constructor (required)
    public User() {}

    // Parameterized constructor
    public User(int userId, String username, String name, String surname,String password, String role) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.role = role;
    }

    // Getters Setters
    public int getUserId() { return userId;}
    public void setUserId(int userId) {this.userId = userId;}
    public String getUsername() { return username;}
    public void setUsername(String username) {this.username = username;}
    public String getName() { return name;}
    public void setName(String name) {this.name = name;}
    public String getSurname() { return surname;}
    public void setSurname(String surname) {this.surname = surname;}
    public String getPassword() { return password;}
    public void setPassword(String password) {
        if(password.length() >= 4)
            this.password = password;
    }
    public String getRole() {return role;}
    public void setRole(String role) {
        if(Objects.equals(role, "manager") || Objects.equals(role, "admin") || Objects.equals(role, "cashier"))
            this.role = role;
    }
}
