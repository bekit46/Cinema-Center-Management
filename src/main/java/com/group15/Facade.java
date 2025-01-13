/**
 * The Facade class serves as a centralized interface for interacting with the database.
 * It provides various methods to manage users, products, movies, schedules, transactions, revenues, and transaction items.
 */
package com.group15;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Facade {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cinema_center?serverTimezone=Europe/Istanbul";
    private static final String DB_USER = "root"; // Replace with your username
    private static final String DB_PASSWORD = "ubkt1234"; // Replace with your password

    /**
     * Establishes a connection to the database.
     *
     * @return Connection object representing the database connection.
     * @throws Exception if a connection cannot be established.
     */
    public Connection connect() throws Exception {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Validates a user's login credentials.
     *
     * @param username the username of the user.
     * @param password the password of the user.
     * @return User object if credentials are valid, otherwise null.
     */
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

    /**
     * Retrieves all products from the database.
     *
     * @return List of Product objects.
     */
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

    /**
     * Updates a product's details in the database.
     *
     * @param product the Product object containing updated information.
     */
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

    /**
     * Updates the stock quantity of a product.
     *
     * @param productId the ID of the product to update.
     * @param quantity the quantity to deduct from the stock.
     */
    public void updateStock(int productId, int quantity) {
        String query = "UPDATE Products SET stock_quantity = stock_quantity - ? WHERE product_id = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the parameters for the prepared statement
            stmt.setInt(1, quantity);
            stmt.setDouble(2, productId);

            // Execute the update and check the number of affected rows
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Stock updated successfully.");
            } else {
                System.out.println("No product found with the given ID.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while updating the product: " + e.getMessage(), e);
        }
    }

    //####################################### USER FACADE CODES #####################################################################

    /**
     * Retrieves all users from the database.
     *
     * @return List of User objects.
     */
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

    public User getUserById(int employeeId) {
        User user = null;
        String query = "SELECT username, name, surname, role, password FROM Users WHERE user_id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, employeeId); // Bind the parameter
            try (ResultSet rs = stmt.executeQuery()) { // Execute the query
                if (rs.next()) { // Use if since user_id is unique
                    String username = rs.getString("username");
                    String name = rs.getString("name");
                    String surname = rs.getString("surname");
                    String role = rs.getString("role");
                    String password = rs.getString("password");

                    user = new User(employeeId, username, name, surname, password, role);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Adds a new user to the database.
     *
     * @param user the User object containing information about the new user.
     */
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

    /**
     * Deletes a user from the database by their ID.
     *
     * @param userId the ID of the user to delete.
     */
    public void deleteUser(int userId) {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates a user's details in the database.
     *
     * @param user the User object containing updated information.
     */
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

    /**
     * Retrieves all revenues from the database.
     * @return a list of Revenue objects.
     */
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

    /**
     * Updates the revenue values by adding new amounts.
     * @param newTaxFree the additional tax-free revenue.
     * @param newTotalTax the additional total tax.
     * @param newTotalAmount the additional total amount.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateRevenue(int newTaxFree, int newTotalTax, int newTotalAmount) {
        String query = "UPDATE Revenue SET tax_free = tax_free + ?, total_tax = total_tax + ?, total_amount = total_amount + ?";
        boolean isUpdated = false;

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set new values
            stmt.setInt(1, newTaxFree);
            stmt.setInt(2, newTotalTax);
            stmt.setInt(3, newTotalAmount);

            // Execute the update
            int rowsAffected = stmt.executeUpdate();
            isUpdated = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

    //####################################### MOVIES FACADE CODES #####################################################################

    /**
     * Retrieves all movies from the database.
     * @return a list of Movie objects.
     */
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT movie_id, poster, title, genre, summary, price, discount, tax FROM Movies";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int movieId = rs.getInt("movie_id");
                String poster = rs.getString("poster");
                String title = rs.getString("title");
                String genre = rs.getString("genre");
                String summary = rs.getString("summary");
                int price = rs.getInt("price");
                int discount = rs.getInt("discount");
                int tax = rs.getInt("tax");

                movies.add(new Movie(movieId, poster, title, genre, summary, price, discount, tax));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }

    /**
     * Retrieves a movie by its ID.
     * @param movieId the ID of the movie to retrieve.
     * @return the Movie object, or null if not found.
     */
    public Movie getMovieById(int movieId) {
        String query = "SELECT movie_id, poster, title, genre, summary, price, discount, tax FROM Movies WHERE movie_id = ?";
        Movie movie = null;  // Initialize the movie object to null
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, movieId);  // Set the movieId in the prepared statement

            ResultSet rs = stmt.executeQuery();  // Execute the query
            if (rs.next()) {  // If a result is found
                String poster = rs.getString("poster");
                String title = rs.getString("title");
                String genre = rs.getString("genre");
                String summary = rs.getString("summary");
                int price = rs.getInt("price");
                int discount = rs.getInt("discount");
                int tax = rs.getInt("tax");

                // Instantiate and return the Movie object
                movie = new Movie(movieId, poster, title, genre, summary, price, discount, tax);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movie;  // Return the movie object (it will be null if no result was found)
    }

    /**
     * Retrieves a movie by its title.
     * @param movieTitle the title of the movie to retrieve.
     * @return the Movie object, or null if not found.
     */
    public Movie getMovieByTitle(String movieTitle) {
        String query = "SELECT movie_id, poster, title, genre, summary, price, discount, tax FROM Movies WHERE title = ?";
        Movie movie = null;  // Initialize the movie object to null
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, movieTitle);  // Set the movieId in the prepared statement

            ResultSet rs = stmt.executeQuery();  // Execute the query
            if (rs.next()) {  // If a result is found
                int movieId = rs.getInt("movie_id");
                String poster = rs.getString("poster");
                String title = rs.getString("title");
                String genre = rs.getString("genre");
                String summary = rs.getString("summary");
                int price = rs.getInt("price");
                int discount = rs.getInt("discount");
                int tax = rs.getInt("tax");

                // Instantiate and return the Movie object
                movie = new Movie(movieId, poster, title, genre, summary, price, discount, tax);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movie;  // Return the movie object (it will be null if no result was found)
    }

    /**
     * Filters movies by their title.
     * @param title the title keyword to filter by.
     * @return a list of movies matching the keyword.
     */
    // Method to filter movies by title
    public List<Movie> filterMoviesByTitle(String title) {
        String query = "SELECT * FROM Movies WHERE title LIKE ?";
        List<Movie> movies = new ArrayList<>();

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + title + "%"); // Add wildcards to search for matching titles
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Create a Movie object and add it to the list
                movies.add(new Movie(
                        rs.getInt("movie_id"),
                        rs.getString("poster"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getString("summary"),
                        rs.getInt("price"),
                        rs.getInt("discount"),
                        rs.getInt("tax")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return movies;
    }

    /**
     * Filters movies by their genre.
     * @param genre the genre keyword to filter by.
     * @return a list of movies matching the genre.
     */
    // Method to filter movies by genre
    public List<Movie> filterMoviesByGenre(String genre) {
        String query = "SELECT * FROM Movies WHERE genre LIKE ?";
        List<Movie> movies = new ArrayList<>();

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + genre + "%"); // Add wildcards to search for matching genres
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Create a Movie object and add it to the list
                movies.add(new Movie(
                        rs.getInt("movie_id"),
                        rs.getString("poster"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getString("summary"),
                        rs.getInt("price"),
                        rs.getInt("discount"),
                        rs.getInt("tax")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return movies;
    }

    /**
     * Deletes a movie from the database.
     * @param movieId the ID of the movie to delete.
     */
    public void deleteMovie(int movieId) {
        String query = "DELETE FROM Movies WHERE movie_id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, movieId);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new movie to the database.
     * @param movie the Movie object to add.
     */
    public void addNewMovie(Movie movie) {
        // Insert the movie into the database
        String query = "INSERT INTO Movies (poster, title, genre, summary) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, movie.getPoster());
            stmt.setString(2, movie.getTitle());
            stmt.setString(3, movie.getGenre());
            stmt.setString(4, movie.getSummary());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates an existing movie in the database.
     * @param movie the Movie object with updated values.
     */
    public void updateMovie(Movie movie) {
        String query = "UPDATE movies SET poster = ?, title = ?, genre = ?, summary = ?, price = ?, discount = ?, tax = ? WHERE movie_id = ?";

        // Validation: Ensure movie ID and essential fields are valid before executing the update
        if (movie == null || movie.getMovieId() <= 0) {
            throw new IllegalArgumentException("Invalid movie object or movie ID.");
        }

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the parameters for the prepared statement
            stmt.setString(1, movie.getPoster());
            stmt.setString(2, movie.getTitle());
            stmt.setString(3, movie.getGenre());
            stmt.setString(4, movie.getSummary());
            stmt.setInt(5, movie.getPrice());
            stmt.setInt(6, movie.getDiscount());
            stmt.setInt(7, movie.getTax());
            stmt.setInt(8, movie.getMovieId());

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

    // ######################################################### SCHEDULE CLASS FACADE CODES #######################################

    /**
     * Retrieves all schedules from the database.
     * @param selectedDate the date to filter schedules (optional).
     * @return a list of Schedule objects.
     */
    public List<Schedule> getSchedules(LocalDate selectedDate) {
        List<Schedule> schedules = new ArrayList<>();
        String query;
        if (selectedDate == null) {
            // Modified query to join Movies table
            query = "SELECT s.schedule_id, s.fk_movie_id, m.title AS movie_title, s.date, s.session_time, s.hall_name, s.taken_seats, s.seating " +
                    "FROM Schedules s " +
                    "JOIN Movies m ON s.fk_movie_id = m.movie_id";
        } else {
            // Modified query to join Movies table and filter by date
            query = "SELECT s.schedule_id, s.fk_movie_id, m.title AS movie_title, s.date, s.session_time, s.hall_name, s.taken_seats, s.seating " +
                    "FROM Schedules s " +
                    "JOIN Movies m ON s.fk_movie_id = m.movie_id " +
                    "WHERE s.date = ?";
        }

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {

            if (selectedDate != null) {
                stmt.setString(1, selectedDate.toString());
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int scheduleId = rs.getInt("schedule_id");
                int movieId = rs.getInt("fk_movie_id");
                String movieTitle = rs.getString("movie_title");
                Date date = rs.getDate("date");
                Time sessionTime = rs.getTime("session_time");
                String hall = rs.getString("hall_name");
                int takenSeats = rs.getInt("taken_seats");
                String seating = rs.getString("seating");

                // Instantiate the Movie object
                Movie movie = getMovieById(movieId);

                // Create the Schedule object and associate the Movie
                Schedule schedule = new Schedule(scheduleId, movieId, date, sessionTime, hall, takenSeats, seating, movie);

                schedules.add(schedule);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schedules;
    }

    /**
     * Retrieves schedules for a specific date and movie ID.
     * @param selectedDate the date to filter schedules.
     * @param movieId the ID of the movie to filter schedules.
     * @return a list of Schedule objects.
     */
    public List<Schedule> getSchedules(LocalDate selectedDate, int movieId) {
        List<Schedule> schedules = new ArrayList<>();
        String query;
        // Modified query to join Movies table and filter by date
        query = "SELECT s.schedule_id, s.fk_movie_id, m.title AS movie_title, s.date, s.session_time, s.hall_name, s.taken_seats, s.seating " +
                "FROM Schedules s " +
                "JOIN Movies m ON s.fk_movie_id = m.movie_id " +
                "WHERE s.date = ? AND s.fk_movie_id = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {

            if (selectedDate != null) {
                stmt.setString(1, selectedDate.toString());
                stmt.setInt(2, movieId);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int scheduleId = rs.getInt("schedule_id");
                String movieTitle = rs.getString("movie_title");
                Date date = rs.getDate("date");
                Time sessionTime = rs.getTime("session_time");
                String hall = rs.getString("hall_name");
                int takenSeats = rs.getInt("taken_seats");
                String seating = rs.getString("seating");

                // Instantiate the Movie object
                Movie movie = getMovieById(movieId);

                // Create the Schedule object and associate the Movie
                Schedule schedule = new Schedule(scheduleId, movieId, date, sessionTime, hall, takenSeats, seating, movie);

                schedules.add(schedule);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schedules;
    }

    /**
     * Retrieves seating information for a specific schedule.
     * @param scheduleId the ID of the schedule.
     * @return the seating arrangement as a String.
     */
    public String getSeatingById(int scheduleId) {
        // SQL query to retrieve the seating information for the given schedule_id
        String query = "SELECT seating FROM Schedules WHERE schedule_id = ?";
        String seating = null;

        // Try-with-resources to manage database resources
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the schedule_id parameter in the query
            stmt.setInt(1, scheduleId);

            // Execute the query and process the result set
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve the seating column value
                    seating = rs.getString("seating");
                }
            }
        } catch (Exception e) {
            // Log the exception and handle database errors
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error fetching seating information for schedule_id: " + scheduleId, e);
        }

        return seating;
    }

    /**
     * Retrieves the hall name for a specific schedule.
     * @param scheduleId the ID of the schedule.
     * @return the hall name as a String.
     */
    public String getHallByScheduleId(int scheduleId) {
        // SQL query to retrieve the hall information for the given schedule_id
        String query = "SELECT hall_name FROM Schedules WHERE schedule_id = ?";
        String hall_name = null;

        // Try-with-resources to manage database resources
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the schedule_id parameter in the query
            stmt.setInt(1, scheduleId);

            // Execute the query and process the result set
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve the seating column value
                    hall_name = rs.getString("hall_name");
                }
            }
        } catch (Exception e) {
            // Log the exception and handle database errors
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error fetching hall information for schedule_id: " + scheduleId, e);
        }

        return hall_name;
    }

    /**
     * Retrieves all schedules for a specific movie ID.
     * @param movieId the ID of the movie.
     * @return a list of Schedule objects.
     */
    public List<Schedule> getSchedulesByMovieId(int movieId) {
        String query = "SELECT schedule_id, fk_movie_id, date, session_time, hall_name, taken_seats, seating FROM Schedules WHERE fk_movie_id = ?";

        List<Schedule> schedules = new ArrayList<>();
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, movieId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int scheduleId = rs.getInt("schedule_id");
                    Date date = rs.getDate("date");
                    Time sessionTime = rs.getTime("session_time");
                    String hall = rs.getString("hall_name");
                    int takenSeats = rs.getInt("taken_seats");
                    String seating = rs.getString("seating");

                    Movie movie = getMovieById(movieId);
                    Schedule schedule = new Schedule(scheduleId, movieId, date, sessionTime, hall, takenSeats, seating, movie);
                    schedules.add(schedule);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schedules;
    }

    /**
     * Checks if there is a schedule conflict for the given time and hall.
     * @param scheduleDate the date of the schedule.
     * @param scheduleTime the time of the schedule.
     * @param hall the hall name.
     * @return true if there is a conflict, false otherwise.
     */
    public boolean checkCollapseInSchedule(Date scheduleDate, Time scheduleTime, String hall) {
        String query = "SELECT schedule_id FROM Schedules WHERE date = ? AND session_time = ? AND hall_name = ?";
        boolean collapse = false;
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set parameters for the query
            stmt.setDate(1, scheduleDate);
            stmt.setTime(2, scheduleTime);
            stmt.setString(3, hall);

            // Execute query and check for a result
            try (ResultSet rs = stmt.executeQuery()) {
                collapse = rs.next();
            }
        } catch (Exception e) {
            // Log meaningful error message
            System.err.println("Error while checking schedule collapse: " + e.getMessage());
        }
        return collapse;
    }


    public void updateSchedule(Schedule schedule) {
        String query = "UPDATE Schedules SET schedule_id = ?, fk_movie_id = ?, date = ?, session_time = ?, hall_name = ?, taken_seats = ?, seating = ? WHERE schedule_id = ?";

        // Validation: Ensure schedule ID and essential fields are valid before executing the update
        if (schedule == null || schedule.getScheduleId() <= 0) {
            throw new IllegalArgumentException("Invalid schedule object or schedule ID.");
        }

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the parameters for the prepared statement
            stmt.setInt(1, schedule.getScheduleId());
            stmt.setInt(2, schedule.getMovieId());
            stmt.setDate(3, schedule.getDate());
            stmt.setTime(4, schedule.getSessionTime());
            stmt.setString(5, schedule.getHall());
            stmt.setInt(6, schedule.getTakenSeats());
            stmt.setString(7, schedule.getSeating());
            stmt.setInt(8, schedule.getScheduleId());

            // Execute the update and check the number of affected rows
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Schedule updated successfully.");
            } else {
                System.out.println("No Schedule found with the given ID.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while updating the Schedule: " + e.getMessage(), e);
        }
    }
    /**
     * Updates the seat information for a schedule.
     * @param scheduleId the ID of the schedule to update.
     * @param soldSeats the number of seats sold.
     * @param newSeating the updated seating arrangement.
     */
    public void updateScheduleSeats(int scheduleId, int soldSeats, String newSeating) {
        String query = "UPDATE Schedules SET taken_seats = taken_seats + ?, seating = ? WHERE schedule_id = ?";

        try (Connection conn = connect(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, soldSeats);
            preparedStatement.setString(2, newSeating);
            preparedStatement.setInt(3, scheduleId);

            int rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new schedule to the database.
     * @param schedule the Schedule object to add.
     */
    public void addNewSchedule(Schedule schedule) {
        // Correct SQL query
        String query = "INSERT INTO Schedules (fk_movie_id, date, session_time, hall_name, taken_seats, seating) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, schedule.getMovieId());
            // Convert java.util.Date to java.sql.Date
            Date sqlDate = new Date(schedule.getDate().getTime());
            stmt.setDate(2, sqlDate); // Convert java.util.Date to java.sql.Date
            stmt.setTime(3, schedule.getSessionTime()); // Use java.sql.Time
            stmt.setString(4, schedule.getHall());
            stmt.setInt(5, schedule.getTakenSeats());
            stmt.setString(6, schedule.getSeating());

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a schedule from the database.
     * @param scheduleId the ID of the schedule to delete.
     */
    public void deleteSchedule(int scheduleId) {
        String query = "DELETE FROM Schedules WHERE schedule_id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, scheduleId);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//######################################################## TRANSACTION TABLE FACADE CODES ########################################################################

    /**
     * Retrieves all transactions from the database.
     * @return a list of Transaction objects.
     */
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT t.transaction_id, t.customer_name, t.customer_surname, t.date, t.fk_schedule_id, " +
                "t.seat_quantity, t.total_price, t.discount_applied, m.title AS movie_title " +
                "FROM Transactions t " +
                "LEFT JOIN Schedules s ON t.fk_schedule_id = s.schedule_id " +
                "LEFT JOIN Movies m ON s.fk_movie_id = m.movie_id";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int transactionId = rs.getInt("transaction_id");
                String customerName = rs.getString("customer_name");
                String customerSurname = rs.getString("customer_surname");
                Date date = rs.getDate("date");
                int scheduleId = rs.getInt("fk_schedule_id");
                int seatQuantity = rs.getInt("seat_quantity");
                int totalPrice = rs.getInt("total_price");
                boolean discountApplied = rs.getBoolean("discount_applied");
                String movieTitle = rs.getString("movie_title");

                transactions.add(new Transaction(transactionId, customerName, customerSurname, date, scheduleId, movieTitle, seatQuantity, totalPrice, discountApplied));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public Transaction getTransactionById(int transactionId) {
        String query = "SELECT t.transaction_id, t.customer_name, t.customer_surname, t.date, t.fk_schedule_id, " +
                "t.seat_quantity, t.total_price, t.discount_applied, m.title AS movie_title " +
                "FROM Transactions t " +
                "LEFT JOIN Schedules s ON t.fk_schedule_id = s.schedule_id " +
                "LEFT JOIN Movies m ON s.fk_movie_id = m.movie_id " +
                "WHERE t.transaction_id = ?";
        Transaction transaction = null;

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transactionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String customerName = rs.getString("customer_name");
                    String customerSurname = rs.getString("customer_surname");
                    Date date = rs.getDate("date");
                    int scheduleId = rs.getInt("fk_schedule_id");
                    int seatQuantity = rs.getInt("seat_quantity");
                    int totalPrice = rs.getInt("total_price");
                    boolean discountApplied = rs.getBoolean("discount_applied");
                    String movieTitle = rs.getString("movie_title");

                    transaction = new Transaction(transactionId, customerName, customerSurname, date, scheduleId, movieTitle, seatQuantity, totalPrice, discountApplied);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transaction;
    }

    /**
     * Retrieves a transaction by its ID.
     * @param transactionId the ID of the transaction to retrieve.
     * @return the Transaction object, or null if not found.
     */
    public String getTransactionSeatsById(int transactionId) {
        String query = "SELECT seats FROM Transactions WHERE transaction_id = ?";

        String seats = null;
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transactionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    seats = rs.getString("seats");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seats;
    }

    /**
     * Retrieves the tax amount for a specific transaction.
     * @param transactionId the ID of the transaction.
     * @return the tax amount.
     */
    public int getTaxById(int transactionId) {
        String query = "SELECT tax FROM Transactions WHERE transaction_id = ?";

        int tax = 0;
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transactionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tax = rs.getInt("tax");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tax;
    }

    /**
     * Filters transactions by customer name, surname, and date.
     * @param name the customer's first name.
     * @param surname the customer's last name.
     * @param date the transaction date.
     * @return a list of filtered Transaction objects.
     */
    public List<Transaction> getFilteredTransactions(String name, String surname, LocalDate date) {
        List<Transaction> transactions = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT t.transaction_id, t.customer_name, t.customer_surname, t.date, t.fk_schedule_id, " +
                "t.seat_quantity, t.total_price, t.discount_applied, m.title AS movie_title " +
                "FROM Transactions t " +
                "LEFT JOIN Schedules s ON t.fk_schedule_id = s.schedule_id " +
                "LEFT JOIN Movies m ON s.fk_movie_id = m.movie_id WHERE 1=1");

        // List of parameters for the prepared statement
        List<Object> params = new ArrayList<>();

        // Add filters to the query and parameters list based on the provided arguments
        if (name != null && !name.trim().isEmpty()) {
            query.append(" AND t.customer_name LIKE ?");
            params.add("%" + name + "%");
        }

        if (surname != null && !surname.trim().isEmpty()) {
            query.append(" AND t.customer_surname LIKE ?");
            params.add("%" + surname + "%");
        }

        if (date != null) {
            query.append(" AND t.date = ?");
            params.add(Date.valueOf(date)); // Convert LocalDate to java.sql.Date
        }

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query.toString())) {
            // Set parameters dynamically based on the filters
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int transactionId = rs.getInt("transaction_id");
                    String customerName = rs.getString("customer_name");
                    String customerSurname = rs.getString("customer_surname");
                    Date transactionDate = rs.getDate("date");
                    int scheduleId = rs.getInt("fk_schedule_id");
                    int seatQuantity = rs.getInt("seat_quantity");
                    int totalPrice = rs.getInt("total_price");
                    boolean discountApplied = rs.getBoolean("discount_applied");
                    String movieTitle = rs.getString("movie_title");

                    transactions.add(new Transaction(transactionId, customerName, customerSurname, transactionDate, scheduleId, movieTitle, seatQuantity, totalPrice, discountApplied));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return transactions;
    }

    /**
     * Deletes a transaction and updates related tables.
     * @param transaction the Transaction object to delete.
     * @return true if successful, false otherwise.
     */
    public boolean deleteTransactionAndUpdate(Transaction transaction) {
        try (Connection conn = connect()) {
            conn.setAutoCommit(false); // Start transaction

            // Step 1: Update revenue table
            String updateRevenueQueryTaxFree = "UPDATE Revenue SET tax_free = tax_free - ?";
            try (PreparedStatement revenueStmt = conn.prepareStatement(updateRevenueQueryTaxFree)) {
                revenueStmt.setInt(1, (transaction.getTotalPrice() - transaction.getTaxById()));
                revenueStmt.executeUpdate();
            }

            String updateRevenueQueryTax = "UPDATE Revenue SET total_tax = total_tax - ?";
            try (PreparedStatement revenueStmt = conn.prepareStatement(updateRevenueQueryTax)) {
                revenueStmt.setInt(1, transaction.getTaxById());
                revenueStmt.executeUpdate();
            }

            String updateRevenueQueryTotal = "UPDATE Revenue SET total_amount = total_amount - ?";
            try (PreparedStatement revenueStmt = conn.prepareStatement(updateRevenueQueryTotal)) {
                int minus = transaction.getTotalPrice();
                revenueStmt.setInt(1, minus);
                revenueStmt.executeUpdate();
            }

            // Step 2: Update taken_seats in the schedule
            String updateTakenSeatsQuery = "UPDATE Schedules SET taken_seats = taken_seats - ? WHERE schedule_id = ?";
            try (PreparedStatement seatStmt = conn.prepareStatement(updateTakenSeatsQuery)) {
                seatStmt.setInt(1, transaction.getSeatQuantity());
                seatStmt.setInt(2, transaction.getScheduleId());
                seatStmt.executeUpdate();
            }

            // Step 3: Update available seats in the schedule
            String updateSeatingQuery = "UPDATE Schedules SET seating = ? WHERE schedule_id = ?";
            String seating = getSeatingById(transaction.getScheduleId());
            List<Integer> indices = getSeatIndices(transaction.getSeats(), getHallByScheduleId(transaction.getScheduleId()));

            // Convert the seating String to a StringBuilder for modification
            StringBuilder seatingBuilder = new StringBuilder(seating);

            // Update the characters at the specified indices
            for (int index : indices) {
                seatingBuilder.setCharAt(index - 1, 'f'); // Convert 1-based index to 0-based
            }

            // Convert the StringBuilder back to a String
            String updatedSeating = seatingBuilder.toString();

            try (PreparedStatement seatsStmt = conn.prepareStatement(updateSeatingQuery)) {
                seatsStmt.setString(1, updatedSeating);
                seatsStmt.setInt(2, transaction.getScheduleId());
                seatsStmt.executeUpdate();
            }

            // Step 4: Update stock_quantity of products
            String updateStockQuery = "UPDATE Products p " +
                    "JOIN Transaction_Items ti ON p.product_id = ti.fk_product_id " +
                    "SET p.stock_quantity = p.stock_quantity + ti.product_quantity " +
                    "WHERE ti.fk_transaction_id = ?";
            try (PreparedStatement stockStmt = conn.prepareStatement(updateStockQuery)) {
                stockStmt.setInt(1, transaction.getTransactionId());
                stockStmt.executeUpdate();
            }

            // Step 5: Delete the transaction
            String deleteTransactionQuery = "DELETE FROM Transactions WHERE transaction_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteTransactionQuery)) {
                deleteStmt.setInt(1, transaction.getTransactionId());
                deleteStmt.executeUpdate();
            }

            conn.commit(); // Commit transaction
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Converts seat information to indices based on hall layout.
     * @param seats the seat information as a String.
     * @param hall the hall name.
     * @return a list of seat indices.
     */
    public static List<Integer> getSeatIndices(String seats, String hall) {
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < seats.length(); i += 2) {
            char row = seats.charAt(i); // Get the row letter
            int column = Character.getNumericValue(seats.charAt(i + 1)); // Get the column number

            if (Objects.equals(hall, "Hall A")) {
                // Hall A: Rows A-D, Columns 1-4
                if (row < 'A' || row > 'D' || column < 1 || column > 4) {
                    throw new IllegalArgumentException("Invalid seat for Hall A: " + row + column);
                }
                int rowIndex = row - 'A'; // Convert row to zero-based index (A=0, B=1, ..., D=3)
                int index = rowIndex * 4 + column; // Calculate the 1-based index
                indices.add(index);
            } else {
                // Hall B: Rows A-F, Columns 1-8
                if (row < 'A' || row > 'F' || column < 1 || column > 8) {
                    throw new IllegalArgumentException("Invalid seat for Hall B: " + row + column);
                }
                int rowIndex = row - 'A'; // Convert row to zero-based index (A=0, B=1, ..., F=5)
                int index = rowIndex * 8 + column; // Calculate the 1-based index
                indices.add(index);
            }
        }

        return indices;
    }

    /**
     * Inserts a new transaction into the database.
     * @param customerName the customer's first name.
     * @param customerSurname the customer's last name.
     * @param date the transaction date.
     * @param scheduleId the associated schedule ID.
     * @param seatQuantity the number of seats purchased.
     * @param seats the seat details.
     * @param totalPrice the total transaction price.
     * @param tax the tax amount.
     * @param discountApplied whether a discount was applied.
     * @return the generated transaction ID, or -1 if insertion failed.
     */
    public int insertTransaction(String customerName, String customerSurname, Date date,
                                 int scheduleId, int seatQuantity, String seats,
                                 int totalPrice, int tax, boolean discountApplied) {
        String query = "INSERT INTO Transactions (customer_name, customer_surname, date, fk_schedule_id, " +
                "seat_quantity, seats, total_price, tax, discount_applied) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters for the query
            preparedStatement.setString(1, customerName);
            preparedStatement.setString(2, customerSurname);
            preparedStatement.setDate(3, date);
            preparedStatement.setInt(4, scheduleId);
            preparedStatement.setInt(5, seatQuantity);
            preparedStatement.setString(6, seats);
            preparedStatement.setInt(7, totalPrice);
            preparedStatement.setInt(8, tax);
            preparedStatement.setBoolean(9, discountApplied);

            // Execute the insert statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Get the auto-generated primary key (the ID)
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);  // Return the generated primary key (ID)
                    } else {
                        throw new SQLException("No primary key returned.");
                    }
                }
            } else {
                return -1;  // Return -1 if no rows were inserted (for example, an error occurred)
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;  // Return -1 if an exception occurs during the insert
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //######################################################## TRANSACTION ITEMS TABLE FACADE CODES ########################################################################
    public List<TransactionItem> getAllTransactionItems() {
        List<TransactionItem> transactionItems = new ArrayList<>();
        String query = "SELECT transaction_items_id, fk_transaction_id, fk_product_id, product_quantity, total_price FROM Transaction_Items";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int transactionItemsId = rs.getInt("transaction_items_id");
                int fkTransactionId = rs.getInt("fk_transaction_id");
                Integer fkProductId = rs.getObject("fk_product_id") != null ? rs.getInt("fk_product_id") : null;
                int productQuantity = rs.getInt("product_quantity");
                int totalPrice = rs.getInt("total_price");

                transactionItems.add(new TransactionItem(transactionItemsId, fkTransactionId, fkProductId, productQuantity, totalPrice));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionItems;
    }

    // Method to get a transaction item by its ID
    public TransactionItem getTransactionItemById(int transactionItemsId) {
        String query = "SELECT transaction_items_id, fk_transaction_id, fk_product_id, product_quantity, total_price FROM Transaction_Items WHERE transaction_items_id = ?";
        TransactionItem transactionItem = null;

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transactionItemsId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int fkTransactionId = rs.getInt("fk_transaction_id");
                Integer fkProductId = rs.getObject("fk_product_id") != null ? rs.getInt("fk_product_id") : null;
                int productQuantity = rs.getInt("product_quantity");
                int totalPrice = rs.getInt("total_price");

                transactionItem = new TransactionItem(transactionItemsId, fkTransactionId, fkProductId, productQuantity, totalPrice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionItem;
    }

    // Method to delete a transaction item by its ID
    public void deleteTransactionItem(int transactionItemsId) {
        String query = "DELETE FROM Transaction_Items WHERE transaction_items_id = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transactionItemsId);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Method to add a new transaction item
    public void addTransactionItem(TransactionItem transactionItem) {
        String query = "INSERT INTO Transaction_Items (fk_transaction_id, fk_product_id, product_quantity, total_price) VALUES (?, ?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transactionItem.getFkTransactionId());
            if (transactionItem.getFkProductId() != null) {
                stmt.setInt(2, transactionItem.getFkProductId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setInt(3, transactionItem.getProductQuantity());
            stmt.setInt(4, transactionItem.getTotalPrice());

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserts a new transaction item into the database.
     * @param transactionId the associated transaction ID.
     * @param productId the product ID.
     * @param productQuantity the quantity of the product.
     * @param totalPrice the total price for the product.
     * @return true if insertion was successful, false otherwise.
     */
    public boolean insertTransactionItem(int transactionId, int productId, int productQuantity, int totalPrice) {
        String query = "INSERT INTO Transaction_Items (fk_transaction_id, fk_product_id, product_quantity, total_price) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, transactionId);
            preparedStatement.setInt(2, productId);
            preparedStatement.setInt(3, productQuantity);
            preparedStatement.setInt(4, totalPrice);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;  // If 1 or more rows were affected, the insertion was successful.
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // If an exception occurs, the insertion fails.
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Retrieves transaction details for a specific transaction ID.
     * @param transactionId the ID of the transaction.
     * @return a formatted string of transaction details.
     */
    public String getTransactionDetails(int transactionId) {
        String query = "SELECT p.name AS product_name, ti.product_quantity " +
                "FROM Transaction_Items ti " +
                "JOIN Products p ON ti.fk_product_id = p.product_id " +
                "WHERE ti.fk_transaction_id = ?";
        List<String> details = new ArrayList<>();

        try (Connection conn = connect(); // Replace with your actual connection method
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transactionId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String productName = rs.getString("product_name");
                int productQuantity = rs.getInt("product_quantity");
                details.add(productName + " x" + productQuantity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.join(", ", details);
    }
}