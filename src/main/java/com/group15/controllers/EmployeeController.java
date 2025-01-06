package com.group15.controllers;
import com.group15.Facade;
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

public class EmployeeController {
    @FXML
    private TableView<User> employeeTable;
    @FXML
    private TableColumn<User, Integer> UserIdColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> surnameColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;

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

    // To track modified users
    private ObservableList<User> modifiedUsers;

    public EmployeeController() {
        this.facade = new Facade();
        this.modifiedUsers = FXCollections.observableArrayList(); // Initialize the list
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
        // Add context menu for deletion
        employeeTable.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Delete User");
            deleteItem.setOnAction(event -> {
                User selectedUser = row.getItem();
                if (selectedUser != null && selectedUser.getUserId() != this.user.getUserId() && selectedUser.getUserId() != 0) {
                    // Remove the user from the database
                    facade.deleteUser(selectedUser.getUserId());
                    // Remove the user from the table's observable list
                    employeeTable.getItems().remove(selectedUser);
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

        // Initialize the table columns
        employeeTable.setEditable(true);
        usernameColumn.setEditable(true);
        nameColumn.setEditable(true);
        surnameColumn.setEditable(true);
        roleColumn.setEditable(true);
        passwordColumn.setEditable(true);
        employeeTable.getSelectionModel().setCellSelectionEnabled(true);

        UserIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        loadEmployeeData();

        // Set cell factories and edit commit handlers
        usernameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        usernameColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            String newUsername = event.getNewValue();
            user.setUsername(newUsername);
            // Check if this is the placeholder row
            if (!modifiedUsers.contains(user)) {
                modifiedUsers.add(user);
            }
            refreshTableWithPlaceholder();
            employeeTable.refresh();
        });

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            String newName = event.getNewValue();
            user.setName(newName);
            // Check if this is the placeholder row
            if (!modifiedUsers.contains(user)) {
                modifiedUsers.add(user);
            }
            refreshTableWithPlaceholder();
            employeeTable.refresh();
        });

        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            String newSurname = event.getNewValue();
            user.setSurname(newSurname);
            // Check if this is the placeholder row
            if (!modifiedUsers.contains(user)) {
                modifiedUsers.add(user);
            }
            refreshTableWithPlaceholder();
            employeeTable.refresh();
        });

        roleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        roleColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            String newRole = event.getNewValue();
            user.setRole(newRole);
            // Check if this is the placeholder row
            if (!modifiedUsers.contains(user)) {
                modifiedUsers.add(user);
            }
            refreshTableWithPlaceholder();
            employeeTable.refresh();
        });

        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            String newPassword = event.getNewValue();
            user.setPassword(newPassword);
            // Check if this is the placeholder row
            if (!modifiedUsers.contains(user)) {
                modifiedUsers.add(user);
            }
            refreshTableWithPlaceholder();
            employeeTable.refresh();
        });
    }

    private void refreshTableWithPlaceholder() {
        ObservableList<User> currentUsers = FXCollections.observableArrayList(employeeTable.getItems());

        // Check if a placeholder already exists
        boolean hasPlaceholder = currentUsers.stream()
                .anyMatch(user -> user.getUserId() == 0);
        // If no placeholder exists, add one
        if (!hasPlaceholder) {
            currentUsers.add(new User(0, "", "", "", "", "")); // Add placeholder row
        }
        employeeTable.setItems(currentUsers);
    }

    private void loadEmployeeData() {
        ObservableList<User> users = FXCollections.observableArrayList(facade.getAllUsers());
        User placeholder = new User(0, "", "", "", "", ""); // Adjust fields as needed
        users.add(placeholder);
        employeeTable.setItems(users);
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
            // Iterate over modified users and update them in the database
            for (User modifiedUser : modifiedUsers) {
                if (modifiedUser.getUserId() != 0) { // Ensure it's not a placeholder
                    facade.updateUser(modifiedUser);
                }
                else {
                    facade.addNewUser(modifiedUser);
                }
            }
            // Optionally, clear the modified list after saving
            modifiedUsers.clear();

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
