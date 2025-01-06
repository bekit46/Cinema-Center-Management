package com.group15.controllers;
import com.group15.Facade;
import com.group15.Movie;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import com.group15.Transaction;
import com.group15.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class adminCancellationController {
    @FXML
    private TableView<Transaction> transactionTable;
    @FXML
    private TableColumn<Transaction, String> nameColumn;
    @FXML
    private TableColumn<Transaction, String> surnameColumn;
    @FXML
    private TableColumn<Transaction, Date> dateColumn;
    @FXML
    private TableColumn<Transaction, String> movieTitleColumn;
    @FXML
    private TableColumn<Transaction, Integer> seatQuantityColumn;
    @FXML
    private TableColumn<Transaction, String> detailsColumn;
    @FXML
    private TableColumn<Transaction, Integer> costColumn;
    @FXML
    private TextField nameBox;
    @FXML
    private TextField surnameBox;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label nameSurnameLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Button closeButton;
    @FXML
    private Button saveButton;// Correct type for closeButton
    @FXML
    private Button movieManagementButton;
    @FXML
    private Button monthlyScheduleButton;
    @FXML
    private Button cancellationButton;
    @FXML
    private DatePicker datePicker;

    private User user;
    private Facade facade;

    public adminCancellationController() {
        this.facade = new Facade();// Initialize the list
    }

    public void setUser(User user) {
        this.user = user;
        // Update the labels with the user's information
        usernameLabel.setText(user.getUsername());
        nameSurnameLabel.setText(user.getName() + " " + user.getSurname());
        roleLabel.setText(user.getRole());
    }

    @FXML
    public void initialize(){
        // Set up table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("customerSurname"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        movieTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        seatQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("seatQuantity"));
        detailsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDetails()));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

        // Load all transactions initially
        loadTransactions(null, null, null);
        // Add listeners to dynamically reload data when filters are applied
        nameBox.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        surnameBox.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
    }

    private void loadTransactions(String name, String surname, LocalDate date) {
        ObservableList<Transaction> transactions;

        if (name == null && surname == null && date == null) {
            // Fetch all transactions if no filters are applied
            transactions = FXCollections.observableArrayList(facade.getAllTransactions());
        } else {
            // Fetch filtered transactions
            transactions = FXCollections.observableArrayList(facade.getFilteredTransactions(name, surname, date));
        }

        // Update the table with the fetched data
        transactionTable.setItems(transactions);
    }


    private void applyFilters() {
        String name = nameBox.getText().trim();
        String surname = surnameBox.getText().trim();
        LocalDate date = datePicker.getValue();

        loadTransactions(
                name.isEmpty() ? null : name,
                surname.isEmpty() ? null : surname,
                date
        );
    }

    @FXML
    public void handleMovieManagementButton() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/movieManagementGUI.fxml"));
            Parent movieManagementRoot = loader.load();

            // Get the controller associated with the FXML file
            adminMovieManagementController movieManagementController = loader.getController();

            // Pass the user information to the controller
            movieManagementController.setUser(user);

            // Set the new scene on the current stage
            Stage currentStage = (Stage) movieManagementButton.getScene().getWindow();
            currentStage.setScene(new Scene(movieManagementRoot));
            currentStage.setTitle("Movie Management");
            makeStageFillScreen(currentStage);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMonthlyScheduleButton() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/monthlyScheduleGUI.fxml"));
            Parent monthlyScheduleRoot = loader.load();

            // Get the controller associated with the FXML file
            adminMonthlyScheduleController monthlyScheduleController = loader.getController();

            // Pass the user information to the controller
            monthlyScheduleController.setUser(user);

            // Set the new scene on the current stage
            Stage currentStage = (Stage) monthlyScheduleButton.getScene().getWindow();
            currentStage.setScene(new Scene(monthlyScheduleRoot));
            currentStage.setTitle("Monthly Schedule Management");
            makeStageFillScreen(currentStage);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCancellationButton() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/cancellationGUI.fxml"));
            Parent adminCancellationControllerRoot = loader.load();

            // Get the controller associated with the FXML file
            adminCancellationController cancellationController = loader.getController();

            // Pass the user information to the controller
            cancellationController.setUser(user);

            // Set the new scene on the current stage
            Stage currentStage = (Stage) cancellationButton.getScene().getWindow();
            currentStage.setScene(new Scene(adminCancellationControllerRoot));
            currentStage.setTitle("Cancellations Processes");
            makeStageFillScreen(currentStage);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            makeStageFillScreen(currentStage);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }

    public void handleSaveButton() throws IOException {

        // Load the admin menu scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/adminMenuGUI.fxml"));
        Parent adminMenuRoot = loader.load();

        // Get the admin controller and pass the user object
        AdminController adminController = loader.getController();
        adminController.setUser(user);

        // Set the admin menu scene on the current stage
        Stage currentStage = (Stage) saveButton.getScene().getWindow();
        currentStage.setScene(new Scene(adminMenuRoot));
        makeStageFillScreen(currentStage);
        currentStage.show();
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
