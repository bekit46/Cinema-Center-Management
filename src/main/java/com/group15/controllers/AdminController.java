/**
 * Controller class for managing the Admin Main Menu view.
 * Provides functionalities for navigating to various admin features such as
 * movie management, monthly schedule, and cancellations.
 */
package com.group15.controllers;

import com.group15.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import java.io.IOException;

/**
 * Controller for the Admin Main Menu interface.
 * Handles navigation and displays user information.
 */
public class AdminController {

    /** Label for displaying the username of the admin. */
    @FXML
    private Label usernameLabel;

    /** Label for displaying the full name of the admin. */
    @FXML
    private Label nameSurnameLabel;

    /** Label for displaying the role of the admin. */
    @FXML
    private Label roleLabel;

    /** Button for closing the application. */
    @FXML
    private Button closeButton;// Correct type for closeButton

    /** Button for navigating to the Movie Management interface. */
    @FXML
    private Button movieManagementButton;

    /** Button for navigating to the Monthly Schedule interface. */
    @FXML
    private Button monthlyScheduleButton;

    /** Button for navigating to the Cancellation interface. */
    @FXML
    private Button cancellationButton;

    /** ImageView for displaying the main menu image. */
    @FXML
    private ImageView mainMenu;

    /** The current admin user. */
    private User user;

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
     * Initializes the controller and loads resources.
     */
    @FXML
    public void initialize() {
        // Load the image resource from the classpath
        Image image = new Image(getClass().getResource("/images/extra/mainMenu.png").toExternalForm());
        mainMenu.setImage(image);
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
     * Handles navigation to the Cancellations Processes view.
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
}
