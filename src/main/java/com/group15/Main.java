package com.group15;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * Main class for the Cinema Center application.
 * This class initializes and launches the JavaFX application, setting up the main stage
 * and configuring it to display the login GUI.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage the primary stage for this application.
     * @throws Exception if there is an issue loading the FXML file.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginGUI.fxml"));
        // Set the scene using the loaded FXML
        Scene scene = new Scene(loader.load());

        // Configure the stage
        primaryStage.setTitle("Cinema Center Login");
        primaryStage.setScene(scene);
        makeStageFillScreen(primaryStage);
        primaryStage.show();
    }

    /**
     * Configures the stage to fill the entire screen.
     *
     * @param stage the stage to configure.
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
     * Main entry point for the application.
     *
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}