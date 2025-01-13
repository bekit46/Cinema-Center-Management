/**
 * The InventoryController class handles the logic for the inventory management GUI.
 * It allows users to view, edit, and manage product inventory, and navigate to other sections of the application.
 */
package com.group15.controllers;
import com.group15.Facade;
import com.group15.Product;
import com.group15.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class InventoryController {

    @FXML
    private TableView<Product> inventoryTable;
    @FXML
    private TableColumn<Product, Integer> idColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;
    @FXML
    private TableColumn<Product, Integer> quantityColumn;
    @FXML
    private TableColumn<Product, Integer> taxColumn;

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

    // To track modified products
    private ObservableList<Product> modifiedProducts;

    /**
     * Constructor for the InventoryController class. Initializes the Facade and modifiedProducts list.
     */
    public InventoryController() {
        this.facade = new Facade();
        this.modifiedProducts = FXCollections.observableArrayList(); // Initialize the list
    }

    private User user;


    /**
     * Sets the logged-in user's information and updates the corresponding labels.
     *
     * @param user the logged-in user
     */
    public void setUser(User user) {
        this.user = user;
        // Update the labels with the user's information
        usernameLabel.setText(user.getUsername());
        nameSurnameLabel.setText(user.getName() + " " + user.getSurname());
        roleLabel.setText(user.getRole());
    }

    /**
     * Initializes the controller, sets up the TableView columns, and loads inventory data.
     */
    @FXML
    private void initialize() {
        // Initialize the table columns
        inventoryTable.setEditable(true);
        priceColumn.setEditable(true);
        quantityColumn.setEditable(true);
        taxColumn.setEditable(true);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        taxColumn.setCellValueFactory(new PropertyValueFactory<>("tax"));
        loadInventoryData();

        // Set cell factories and edit commit handlers

        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceColumn.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            Double newPrice = event.getNewValue();
            if(newPrice < 0)
                showAlert("Error", "Price can not be negative.");
            else{
                product.setPrice(newPrice);
                // Track the modified product
                if (!modifiedProducts.contains(product)) {
                    modifiedProducts.add(product);
                }
            }
            inventoryTable.refresh();
        });

        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            Integer newQuantity = event.getNewValue();
            if(newQuantity < 0)
                showAlert("Error", "Stock quantity can not be negative.");
            else{
                product.setStockQuantity(newQuantity);
                // Track the modified product
                if (!modifiedProducts.contains(product)) {
                    modifiedProducts.add(product);
                }
            }
            inventoryTable.refresh();
        });

        taxColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        taxColumn.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            Integer newTax = event.getNewValue();
            if(newTax < 0 || newTax > 100)
                showAlert("Error", "Tax rate should be between %0 and %100.");
            else{
                product.setTax(newTax);
                // Track the modified product
                if (!modifiedProducts.contains(product)) {
                    modifiedProducts.add(product);
                }
            }
            inventoryTable.refresh();
        });
    }

    /**
     * Loads product data into the TableView.
     */
    private void loadInventoryData() {
        ObservableList<Product> products = FXCollections.observableArrayList(facade.getAllProducts());
        inventoryTable.setItems(products);
    }

    /**
     * Handles the logic for the close button, navigating back to the login screen.
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
     * Handles the logic for the save button, saving changes and navigating to the manager menu.
     */
    public void handleSaveButton() {
        try {
            // Iterate over modified products and update them in the database
            for (Product product : modifiedProducts) {
                facade.updateProduct(product);
            }

            // Optionally, clear the modified list after saving
            modifiedProducts.clear();

            // Load the manager menu scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/managerMenuGUI.fxml"));
            Parent managerMenuRoot = loader.load();

            // Get the manager controller and pass the user object
            ManagerController managerController = loader.getController();
            managerController.setUser(user);

            // Set the manager menu scene on the current stage
            Stage currentStage = (Stage) saveButton.getScene().getWindow();
            currentStage.setScene(new Scene(managerMenuRoot));
            makeStageFillScreen(currentStage);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }

    /**
     * Handles the navigation to the inventory management scene.
     */
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

    /**
     * Handles the navigation to the employee management scene.
     */
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

    /**
     * Handles the navigation to the movie pricing management scene.
     */
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

    /**
     * Handles the navigation to the revenue display scene.
     */
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

    /**
     * Configures the stage to fill the screen.
     *
     * @param stage the stage to configure
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
     * Displays an alert dialog with a given title and message.
     *
     * @param title the title of the alert
     * @param message the message of the alert
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
