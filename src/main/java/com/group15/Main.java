package com.group15;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class Main extends Application {

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

    public void makeStageFillScreen(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        // Set the stage size to exactly match the screen dimensions
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}