package com.group15;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Facade {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cinema_center?serverTimezone=Europe/Istanbul";
    private static final String DB_USER = "root"; // Replace with your username
    private static final String DB_PASSWORD = "ubkt1234"; // Replace with your password

    public Connection connect() throws Exception {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Method to validate user login
    public User validateLogin(String username, String password) {
        String query = "SELECT user_id, username, name, surname, password, role FROM users WHERE username = ? AND password = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Extract user details
                int fetchedUserId = rs.getInt("user_id");
                String fetchedUsername = rs.getString("username");
                String fetchedName = rs.getString("name");
                String fetchedSurname = rs.getString("surname");
                String fetchedPassword = rs.getString("password");
                String fetchedRole = rs.getString("role");

                // Create a new User object
                User user = new User(fetchedUserId, fetchedUsername, fetchedName, fetchedSurname, fetchedPassword, fetchedRole);
                return user; // Return the created User object
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if no user is found or an error occurs
    }

    //################################################# PRODUCT CLASS FACADE CODES #############################################################################

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT product_id, name, price, stock_quantity, tax_rate FROM Products";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int product_id = rs.getInt("product_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int stockQuantity = rs.getInt("stock_quantity");
                int tax = rs.getInt("tax_rate");
                products.add(new Product(product_id, name, price, stockQuantity, tax));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
        return products;
    }

    public void updateProduct(Product product) {
        String query = "UPDATE Products SET name = ?, price = ?, stock_quantity = ?, tax_rate = ? WHERE product_id = ?";

        // Validation: Ensure movie ID and essential fields are valid before executing the update
        if (product == null || product.getId() <= 0) {
            throw new IllegalArgumentException("Invalid product object or product ID.");
        }

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the parameters for the prepared statement
            stmt.setString(1, product.getProductName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getStockQuantity());
            stmt.setInt(4, product.getTax());
            stmt.setInt(5, product.getId());

            // Execute the update and check the number of affected rows
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Product updated successfully.");
            } else {
                System.out.println("No product found with the given ID.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while updating the product: " + e.getMessage(), e);
        }
    }

    //####################################### USER FACADE CODES #####################################################################

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT user_id, username, name, surname, role, password FROM Users";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int user_id = rs.getInt("user_id");
                String username = rs.getString("username");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String role = rs.getString("role");
                String password = rs.getString("password");

                users.add(new User(user_id, username, name, surname, password, role));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public void addNewUser(User user) {
        // Insert the user into the database
        String query = "INSERT INTO users (username, name, surname, role, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getSurname());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getPassword());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(int userId) {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUser(User user) {
        String query = "UPDATE users SET username = ?, name = ?, surname = ?, role = ?, password = ? WHERE user_id = ?";

        // Validation: Ensure movie ID and essential fields are valid before executing the update
        if (user == null || user.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user object or user ID.");
        }

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the parameters for the prepared statement
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getSurname());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getPassword());
            stmt.setInt(6, user.getUserId());

            // Execute the update and check the number of affected rows
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("No user found with the given ID.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while updating the user: " + e.getMessage(), e);
        }
    }


    //##################################### REVENUE FACADE FUNCTIONS ###################################################

    public List<Revenue> getAllRevenues() {
        List<Revenue> revenues = new ArrayList<>();
        String query = "SELECT tax_free, total_tax, total_amount FROM Revenue";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int taxFree = rs.getInt("tax_free");
                int totalTax = rs.getInt("total_tax");
                int total = rs.getInt("total_amount");
                revenues.add(new Revenue(taxFree, totalTax, total));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenues;
    }

    //####################################### MOVIES FACADE CODES #####################################################################

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT movie_id, title, genre, summary, price, discount, tax FROM Movies";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int movieId = rs.getInt("movie_id");
                String title = rs.getString("title");
                String genre = rs.getString("genre");
                String summary = rs.getString("summary");
                int price = rs.getInt("price");
                int discount = rs.getInt("discount");
                int tax = rs.getInt("tax");

                movies.add(new Movie(movieId, title, genre, summary, price, discount, tax));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }

    public void deleteMovie(int movieId) {
        String query = "DELETE FROM Movies WHERE movie_id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, movieId);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateMovie(Movie movie) {
        String query = "UPDATE movies SET title = ?, genre = ?, summary = ?, price = ?, discount = ?, tax = ? WHERE movie_id = ?";

        // Validation: Ensure movie ID and essential fields are valid before executing the update
        if (movie == null || movie.getMovieId() <= 0) {
            throw new IllegalArgumentException("Invalid movie object or movie ID.");
        }

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the parameters for the prepared statement
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getGenre());
            stmt.setString(3, movie.getSummary());
            stmt.setInt(4, movie.getPrice());
            stmt.setInt(5, movie.getDiscount());
            stmt.setInt(6, movie.getTax());
            stmt.setInt(7, movie.getMovieId());

            // Execute the update and check the number of affected rows
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Movie updated successfully.");
            } else {
                System.out.println("No movie found with the given ID.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while updating the movie: " + e.getMessage(), e);
        }
    }
}

