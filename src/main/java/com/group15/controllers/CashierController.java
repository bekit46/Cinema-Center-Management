/**
 * Controller class for managing the cashier view.
 * Handles cashier-specific operations such as displaying user information and logging out.
 */
package com.group15.controllers;

import com.group15.Facade;
import com.group15.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the Cashier interface.
 * Manages user session and navigation.
 */
public class CashierController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Button closeButton; // Correct type for closeButton

    /** The current user. */
    private User user;

    /**
     * Sets the user information and updates the UI labels.
     *
     * @param user The cashier user.
     */
    public void setUser(User user) {
        this.user = user;
        // Update the labels with the user's information
        usernameLabel.setText(user.getUsername());
    }

    /**
     * Handles the close button action, logging out the cashier and returning to the login view.
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
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }
}
