package com.group15;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
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


    public void deleteMovie(int movieId) {
        String query = "DELETE FROM Movies WHERE movie_id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, movieId);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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

    public void addNewSchedule(Schedule schedule) {
        // Correct SQL query
        String query = "INSERT INTO Schedules (fk_movie_id, date, session_time, hall_name, taken_seats, seating) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, schedule.getMovieId());
            // Convert java.util.Date to java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(schedule.getDate().getTime());
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

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT t.transaction_id, t.customer_name, t.customer_surname, t.date, t.fk_movie_id, " +
                "t.seat_quantity, t.total_price, t.discount_applied, m.title AS movie_title " +
                "FROM Transactions t LEFT JOIN Movies m ON t.fk_movie_id = m.movie_id";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int transactionId = rs.getInt("transaction_id");
                String customerName = rs.getString("customer_name");
                String customerSurname = rs.getString("customer_surname");
                Date date = rs.getDate("date");
                int movieId = rs.getInt("fk_movie_id");
                int seatQuantity = rs.getInt("seat_quantity");
                int totalPrice = rs.getInt("total_price");
                boolean discountApplied = rs.getBoolean("discount_applied");
                String movieTitle = rs.getString("movie_title");

                transactions.add(new Transaction(transactionId, customerName, customerSurname, date, movieId, movieTitle, seatQuantity, totalPrice, discountApplied));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public Transaction getTransactionById(int transactionId) {
        String query = "SELECT t.transaction_id, t.customer_name, t.customer_surname, t.date, t.fk_movie_id, " +
                "t.seat_quantity, t.total_price, t.discount_applied, m.title AS movie_title " +
                "FROM Transactions t LEFT JOIN Movies m ON t.fk_movie_id = m.movie_id WHERE t.transaction_id = ?";
        Transaction transaction = null;

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transactionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String customerName = rs.getString("customer_name");
                    String customerSurname = rs.getString("customer_surname");
                    Date date = rs.getDate("date");
                    int movieId = rs.getInt("fk_movie_id");
                    int seatQuantity = rs.getInt("seat_quantity");
                    int totalPrice = rs.getInt("total_price");
                    boolean discountApplied = rs.getBoolean("discount_applied");
                    String movieTitle = rs.getString("movie_title");

                    transaction = new Transaction(transactionId, customerName, customerSurname, date, movieId, movieTitle, seatQuantity, totalPrice, discountApplied);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transaction;
    }

    public List<Transaction> getFilteredTransactions(String name, String surname, LocalDate date) {
        List<Transaction> transactions = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT t.transaction_id, t.customer_name, t.customer_surname, t.date, t.fk_movie_id, " +
                "t.seat_quantity, t.total_price, t.discount_applied, m.title AS movie_title " +
                "FROM Transactions t LEFT JOIN Movies m ON t.fk_movie_id = m.movie_id WHERE 1=1");

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
            params.add(java.sql.Date.valueOf(date)); // Convert LocalDate to java.sql.Date
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
                    int movieId = rs.getInt("fk_movie_id");
                    int seatQuantity = rs.getInt("seat_quantity");
                    int totalPrice = rs.getInt("total_price");
                    boolean discountApplied = rs.getBoolean("discount_applied");
                    String movieTitle = rs.getString("movie_title");

                    transactions.add(new Transaction(transactionId, customerName, customerSurname, transactionDate, movieId, movieTitle, seatQuantity, totalPrice, discountApplied));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return transactions;
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
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            stmt.setInt(3, transactionItem.getProductQuantity());
            stmt.setInt(4, transactionItem.getTotalPrice());

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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