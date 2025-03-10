package com.group15.controllers;
import com.group15.Facade;
import com.group15.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller class responsible for managing the login functionality.
 * Handles user authentication and navigation to appropriate user role-based scenes.
 */
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final Facade facade = new Facade();

    /**
     * Handles the login button action. Validates user credentials and navigates
     * to the appropriate scene based on the user's role.
     */
    @FXML
    public void handleLoginButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validate the user credentials using the facade
        User user = facade.validateLogin(username, password);

        if (user != null) {
            errorLabel.setText("Login successful!");
            errorLabel.setDisable(false);

            // Load the appropriate scene based on user role
            try {
                loadSceneBasedOnRole(user);
            } catch (IOException e) {
                e.printStackTrace();
                errorLabel.setText("An error occurred while loading the scene.");
                errorLabel.setDisable(false);
            }
        } else {
            errorLabel.setText("Invalid username or password. Please try again.");
            errorLabel.setDisable(false);
        }
    }

    /**
     * Loads the appropriate scene based on the user's role.
     *
     * @param user The logged-in user.
     * @throws IOException If an FXML file cannot be loaded.
     */
    private void loadSceneBasedOnRole(User user) throws IOException {
        FXMLLoader loader;
        Parent root;

        switch (user.getRole().toLowerCase()) {
            case "manager":
                loader = new FXMLLoader(getClass().getResource("/com/group15/managerMenuGUI.fxml"));
                root = loader.load();

                ManagerController managerController = loader.getController();
                managerController.setUser(user);
                break;

            case "admin":
                loader = new FXMLLoader(getClass().getResource("/com/group15/adminMenuGUI.fxml"));
                root = loader.load();

                AdminController adminController = loader.getController();
                adminController.setUser(user);
                break;

            case "cashier":
                loader = new FXMLLoader(getClass().getResource("/com/group15/stage1.fxml"));
                root = loader.load();

                stage1Controller cashierController = loader.getController();
                cashierController.setUser(user);
                break;

            default:
                errorLabel.setText("Role not recognized. Please contact the administrator.");
                return;
        }

        // Get the current stage and retain full-screen settings
        Stage currentStage = (Stage) usernameField.getScene().getWindow();

        // Set the new scene
        Scene newScene = new Scene(root);
        currentStage.setScene(newScene);

        // Restore full-screen mode
        makeStageFillScreen(currentStage);
        currentStage.show();
    }

    /**
     * Adjusts the stage to fill the entire screen.
     *
     * @param stage The stage to be adjusted.
     */
    public void makeStageFillScreen(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        // Set the stage size to exactly match the screen dimensions
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());
    }
}