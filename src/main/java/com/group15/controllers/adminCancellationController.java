/**
 * Controller class for managing the admin cancellation view.
 * Handles the display and management of transactions, including refunds and filtering.
 */
package com.group15.controllers;
import com.group15.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
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

/**
 * Controller for the Admin Cancellation feature.
 * Provides functionalities such as refunding transactions and filtering transaction records.
 */
public class adminCancellationController {

    /** Table for displaying transactions. */
    @FXML
    private TableView<Transaction> transactionTable;

    /** Column for displaying customer names. */
    @FXML
    private TableColumn<Transaction, String> nameColumn;

    /** Column for displaying customer surnames. */
    @FXML
    private TableColumn<Transaction, String> surnameColumn;

    /** Column for displaying transaction dates. */
    @FXML
    private TableColumn<Transaction, Date> dateColumn;

    /** Column for displaying movie titles. */
    @FXML
    private TableColumn<Transaction, String> movieTitleColumn;

    /** Column for displaying seat quantities. */
    @FXML
    private TableColumn<Transaction, Integer> seatQuantityColumn;

    /** Column for displaying transaction details. */
    @FXML
    private TableColumn<Transaction, String> detailsColumn;

    /** Column for displaying transaction costs. */
    @FXML
    private TableColumn<Transaction, Integer> costColumn;

    /** TextField for filtering by customer name. */
    @FXML
    private TextField nameBox;

    /** TextField for filtering by customer surname. */
    @FXML
    private TextField surnameBox;

    /** Label for displaying the username of the admin. */
    @FXML
    private Label usernameLabel;

    /** Label for displaying the full name of the admin. */
    @FXML
    private Label nameSurnameLabel;

    /** Label for displaying the admin's role. */
    @FXML
    private Label roleLabel;

    /** Button for closing the current window. */
    @FXML
    private Button closeButton;

    /** Button for saving changes. */
    @FXML
    private Button saveButton;// Correct type for closeButton

    /** Button for navigating to movie management. */
    @FXML
    private Button movieManagementButton;

    /** Button for navigating to the monthly schedule. */
    @FXML
    private Button monthlyScheduleButton;

    /** Button for handling cancellation processes. */
    @FXML
    private Button cancellationButton;

    /** DatePicker for filtering transactions by date. */
    @FXML
    private DatePicker datePicker;

    /** Current admin user. */
    private User user;

    /** Facade for managing backend operations. */
    private Facade facade;

    /**
     * Constructor for initializing the admin cancellation controller.
     */
    public adminCancellationController() {
        this.facade = new Facade();// Initialize the list
    }

    /**
     * Sets the user information and updates the UI labels.
     *
     * @param user The admin user.
     */
    public void setUser(User user) {
        this.user = user;
        // Update the labels with the user's information
        usernameLabel.setText(user.getUsername());
        nameSurnameLabel.setText(user.getName() + " " + user.getSurname());
        roleLabel.setText(user.getRole());
    }

    /**
     * Initializes the controller and sets up UI components.
     */
    @FXML
    public void initialize(){
        // Add context menu for deletion
        transactionTable.setRowFactory(tv -> {
            TableRow<Transaction> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Refund Transaction");

            deleteItem.setOnAction(event -> {
                Transaction selectedTransaction = row.getItem();
                if (selectedTransaction != null) {
                    // Perform database operations to handle the refund
                    boolean success = facade.deleteTransactionAndUpdate(selectedTransaction);

                    if (success) {
                        // Remove the transaction from the table's observable list
                        showAlert("Success", "Transaction refunded successfully.");
                        transactionTable.getItems().remove(selectedTransaction);
                    } else {
                        // Optionally show an error message if the operation fails
                        showAlert("Error", "Failed to refund the transaction.");
                    }
                }
            });
            contextMenu.getItems().add(deleteItem);

            // Only show context menu for non-empty rows
            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            return row;
        });

        // Set up table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("customerSurname"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        movieTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        seatQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("seatQuantity"));
        detailsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDetails()));
        costColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTotalPrice()).asObject());

        // Load all transactions initially
        loadTransactions(null, null, null);
        // Add listeners to dynamically reload data when filters are applied
        nameBox.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        surnameBox.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
    }

    /**
     * Loads transactions into the table based on filters.
     *
     * @param name The customer name filter.
     * @param surname The customer surname filter.
     * @param date The transaction date filter.
     */
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

    /**
     * Applies filters to the transaction table dynamically.
     */
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

    /**
     * Handles navigation to the Movie Management view.
     */
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

    /**
     * Handles navigation to the Monthly Schedule view.
     */
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

    /**
     * Handles navigation to the Cancellation Processes view.
     */
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

    /**
     * Handles the close button action, closing the current stage and returning to the login view.
     */
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

    /**
     * Handles saving changes and navigating to the admin menu.
     *
     * @throws IOException If an error occurs while loading the view.
     */
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

    /**
     * Adjusts the stage to fill the screen.
     *
     * @param stage The stage to adjust.
     */
    public void makeStageFillScreen(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        // Set the stage size to exactly match the screen dimensions
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());
    }

    /**
     * Displays an alert with a given title and message.
     *
     * @param title   The title of the alert.
     * @param message The message to display.
     */
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);

        // Create custom content
        Label content = new Label(message);
        content.setWrapText(true);
        content.setStyle("-fx-font-size: 18px;");

        // Set the custom content
        alert.getDialogPane().setContent(content);

        // Set a specific rectangular size for the alert
        alert.getDialogPane().setPrefSize(400, 200); // Width: 400, Height: 200

        alert.showAndWait();
    }
}
