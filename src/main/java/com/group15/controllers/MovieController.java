package com.group15.controllers;
import com.group15.Facade;
import com.group15.Movie;
import com.group15.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

public class MovieController {
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie, Integer> movieIdColumn;
    @FXML
    private TableColumn<Movie, String> titleColumn;
    @FXML
    private TableColumn<Movie, String> genreColumn;
    @FXML
    private TableColumn<Movie, Integer> priceColumn;
    @FXML
    private TableColumn<Movie, Integer> discountColumn;
    @FXML
    private TableColumn<Movie, Integer> taxColumn;

    @FXML
    private Label usernameLabel;
    @FXML
    private Label nameSurnameLabel;
    @FXML
    private Label roleLabel;

    @FXML
    private Button closeButton; // Correct type for closeButton
    @FXML
    private Button saveButton;
    @FXML
    private Button inventoryButton;
    @FXML
    private Button employeeButton;
    @FXML
    private Button pricingButton;
    @FXML
    private Button revenueButton;


    private Facade facade;

    // To track modified movies
    private ObservableList<Movie> modifiedMovies;

    public MovieController() {
        this.facade = new Facade();
        this.modifiedMovies = FXCollections.observableArrayList(); // Initialize the list
    }

    private User user;

    public void setUser(User user) {
        this.user = user;
        // Update the labels with the user's information
        usernameLabel.setText(user.getUsername());
        nameSurnameLabel.setText(user.getName() + " " + user.getSurname());
        roleLabel.setText(user.getRole());
    }

    @FXML
    private void initialize() {
        // Initialize the table columns
        movieTable.setEditable(true);
        priceColumn.setEditable(true);
        discountColumn.setEditable(true);
        taxColumn.setEditable(true);
        movieTable.getSelectionModel().setCellSelectionEnabled(true);

        movieIdColumn.setCellValueFactory(new PropertyValueFactory<>("movieId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
        taxColumn.setCellValueFactory(new PropertyValueFactory<>("tax"));
        loadMovieData();

        // Set cell factories and edit commit handlers
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        priceColumn.setOnEditCommit(event -> {
            Movie movie = event.getRowValue();
            movie.setPrice(event.getNewValue());
            // Track the modified movie
            if (!modifiedMovies.contains(movie)) {
                modifiedMovies.add(movie);
            }
            movieTable.refresh();
        });

        discountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        discountColumn.setOnEditCommit(event -> {
            Movie movie = event.getRowValue();
            movie.setDiscount(event.getNewValue());
            // Track the modified movie
            if (!modifiedMovies.contains(movie)) {
                modifiedMovies.add(movie);
            }
            movieTable.refresh();
        });

        taxColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        taxColumn.setOnEditCommit(event -> {
            Movie movie = event.getRowValue();
            movie.setTax(event.getNewValue());
            // Track the modified movie
            if (!modifiedMovies.contains(movie)) {
                modifiedMovies.add(movie);
            }
            movieTable.refresh();
        });
    }

    private void loadMovieData() {
        ObservableList<Movie> movies = FXCollections.observableArrayList(facade.getAllMovies());
        movieTable.setItems(movies);
    }

    @FXML
    public void handleCloseButton() {
        // Get the current stage
        Stage currentStage = (Stage) closeButton.getScene().getWindow();
        // Close the current stage
        currentStage.close();

        // Load the login scene
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/loginGUI.fxml"));
            Parent loginRoot = loader.load();

            // Create a new stage for the login scene
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(loginRoot));
            loginStage.setTitle("Login");
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }

    public void handleSaveButton() {
        try {
            // Iterate over modified movies and update them in the database
            for (Movie movie : modifiedMovies) {
                facade.updateMovie(movie);
            }

            // Optionally, clear the modified list after saving
            modifiedMovies.clear();

            // Load the manager menu scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/managerMenuGUI.fxml"));
            Parent managerMenuRoot = loader.load();

            // Get the manager controller and pass the user object
            ManagerController managerController = loader.getController();
            managerController.setUser(user);

            // Set the manager menu scene on the current stage
            Stage currentStage = (Stage) saveButton.getScene().getWindow();
            currentStage.setScene(new Scene(managerMenuRoot));
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }

    @FXML
    public void handleInventoryButton() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/inventoryGUI.fxml"));
            Parent inventoryRoot = loader.load();

            // Get the controller associated with the FXML file
            InventoryController inventoryController = loader.getController();

            // Pass the user information to the controller
            inventoryController.setUser(user);

            // Set the new scene on the current stage
            Stage currentStage = (Stage) inventoryButton.getScene().getWindow();
            currentStage.setScene(new Scene(inventoryRoot));
            currentStage.setTitle("Inventory Management");
            makeStageFillScreen(currentStage);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }

    @FXML
    public void handleEmployeeButton() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/employeesGUI.fxml"));
            Parent employeeRoot = loader.load();

            // Get the controller associated with the FXML file
            EmployeeController employeeController = loader.getController();

            // Pass the user information to the controller
            employeeController.setUser(user);

            // Set the new scene on the current stage
            Stage currentStage = (Stage) employeeButton.getScene().getWindow();
            currentStage.setScene(new Scene(employeeRoot));
            currentStage.setTitle("Employee Management");
            makeStageFillScreen(currentStage);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handlePricingButton() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/moviesGUI.fxml"));
            Parent pricingRoot = loader.load();

            // Get the controller associated with the FXML file
            MovieController pricingController = loader.getController();

            // Pass the user information to the controller
            pricingController.setUser(user);

            // Set the new scene on the current stage
            Stage currentStage = (Stage) pricingButton.getScene().getWindow();
            currentStage.setScene(new Scene(pricingRoot));
            currentStage.setTitle("Movie Pricing Management");
            makeStageFillScreen(currentStage);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRevenueButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/revenueGUI.fxml"));
            Parent revenueRoot = loader.load();

            // Get the controller associated with the FXML file
            RevenueController revenueController = loader.getController();

            // Pass the user information to the controller
            revenueController.setUser(user);

            // Set the new scene on the current stage
            Stage currentStage = (Stage) revenueButton.getScene().getWindow();
            currentStage.setScene(new Scene(revenueRoot));
            currentStage.setTitle("Revenue Display");
            makeStageFillScreen(currentStage);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeStageFillScreen(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        // Set the stage size to exactly match the screen dimensions
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());
    }
}
