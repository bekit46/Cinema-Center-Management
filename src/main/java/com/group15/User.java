package com.group15;

import java.util.Objects;
/**
 * Represents a user with various attributes such as ID, username, name,
 * surname, password, and role.
 * This class includes constructors, getters, and setters to manage the user data.
 */
public class User {
    private int userId;
    private String username;
    private String name;
    private String surname;
    private String password;
    private String role;

    // No-argument constructor (required)
    /**
     * Default no-argument constructor for creating a new User object.
     */
    public User() {}

    // Parameterized constructor
    /**
     * Parameterized constructor for creating a User object with specific attributes.
     *
     * @param userId    the unique identifier of the user
     * @param username  the username of the user
     * @param name      the first name of the user
     * @param surname   the surname of the user
     * @param password  the password of the user
     * @param role      the role of the user (e.g., admin, user)
     */
    public User(int userId, String username, String name, String surname,String password, String role) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.role = role;
    }

    // Getters Setters
    /**
     * Gets the user ID.
     *
     * @return the user ID
     */
    public int getUserId() { return userId;}

    /**
     * Sets the user ID.
     *
     * @param userId the new user ID
     */
    public void setUserId(int userId) {this.userId = userId;}
    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() { return username;}
    /**
     * Sets the username.
     *
     * @param username the new username
     */
    public void setUsername(String username) {this.username = username;}
    /**
     * Gets the first name of the user.
     *
     * @return the first name of the user
     */
    public String getName() { return name;}
    /**
     * Sets the first name of the user.
     *
     * @param name the new first name of the user
     */
    public void setName(String name) {this.name = name;}
    /**
     * Gets the surname of the user.
     *
     * @return the surname of the user
     */
    public String getSurname() { return surname;}
    /**
     * Sets the surname of the user.
     *
     * @param surname the new surname of the user
     */
    public void setSurname(String surname) {this.surname = surname;}
    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() { return password;}
    /**
     * Sets the password of the user.
     *
     * @param password the new password of the user
     */
    public void setPassword(String password) {this.password = password;}
    /**
     * Gets the role of the user.
     *
     * @return the role of the user
     */
    public String getRole() {return role;}
    /**
     * Sets the role of the user.
     *
     * @param role the new role of the user
     */
    public void setRole(String role) {this.role = role;}
}
