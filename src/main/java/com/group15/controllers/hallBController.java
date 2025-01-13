/**
 * Controller class for managing the Hall B view in the JavaFX application.
 * Handles seat selection, user interaction, and navigation between screens.
 * Provides functionality to dynamically update the seat states and user information.
 */
package com.group15.controllers;

import com.group15.DataPass;
import com.group15.Facade;
import com.group15.Schedule;
import com.group15.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Screen;
import javafx.stage.Stage;


import java.io.IOException;

import javafx.event.ActionEvent;

public class hallBController {

    @FXML
    public Button ButtonA1, ButtonA2, ButtonA3, ButtonA4, ButtonA5, ButtonA6, ButtonA7, ButtonA8;
    @FXML
    public Button ButtonB1, ButtonB2, ButtonB3, ButtonB4, ButtonB5, ButtonB6, ButtonB7, ButtonB8;
    @FXML
    public Button ButtonC1, ButtonC2, ButtonC3, ButtonC4, ButtonC5, ButtonC6, ButtonC7, ButtonC8;
    @FXML
    public Button ButtonD1, ButtonD2, ButtonD3, ButtonD4, ButtonD5, ButtonD6, ButtonD7, ButtonD8;
    @FXML
    public Button ButtonE1, ButtonE2, ButtonE3, ButtonE4, ButtonE5, ButtonE6, ButtonE7, ButtonE8;
    @FXML
    public Button ButtonF1, ButtonF2, ButtonF3, ButtonF4, ButtonF5, ButtonF6, ButtonF7, ButtonF8;
    @FXML
    public Button ButtonNext, ButtonPrev, closeButton;
    @FXML
    public Label Surname, Name, Username, Role;
    @FXML
    public TextArea cart;
    
    public DataPass data;

    Facade facade = new Facade();

    /**
     * Sets the shared DataPass object and initializes seat states.
     * @param data the DataPass object containing shared data.
     */
    public void setData(DataPass data){
        this.data = data;
        Schedule schedule = data.schedule;

        String redSeats = "";
        redSeats = facade.getSeatingById(schedule.getScheduleId());
        int pointer = 0;

        ButtonNext.setVisible(false);

        for (char row = 'A'; row <= 'F'; row++) {
            for (int col = 1; col <= 8; col++) {
                // Dynamically create the button name (e.g., ButtonA1, ButtonB5)
                String buttonName = "Button" + row + col;

                try {
                    // Use reflection to find the button by its name
                    Button button = (Button) this.getClass().getDeclaredField(buttonName).get(this);
                    if(redSeats.charAt(pointer++)=='t'){
                        button.setStyle("-fx-background-color: red;");
                        continue;
                    }
                    addButtonToggleHandler(button);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();  // Handle the exception if the button is not found
                }
            }
        }
    }

    private User user;

    /**
     * Sets the user data and updates the UI labels.
     * @param user the User object containing user details.
     */
    public void setUser(User user) {
        this.user = user;
        // Update the labels with the user's information
        Username.setText("Username: "+user.getUsername());
        Name.setText("Name: "+user.getName());
        Surname.setText("Surname: "+user.getSurname());
        Role.setText("Role: "+user.getRole());
    }

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        
    }

    /**
     * Adds a toggle handler for a button to manage seat selection.
     * @param button the Button to add the handler to.
     */
    private void addButtonToggleHandler(Button button) {
        button.setOnAction(event -> {
            String currentStyle = button.getStyle();

            if ("-fx-background-color: green; -fx-font-size: 18; -fx-font-weight: bold".equals(currentStyle)) {
                button.setStyle("-fx-background-color: #80868B; -fx-font-size: 18; -fx-font-weight: bold");
                data.removeSeat(button.getText());
            } else {
                button.setStyle("-fx-background-color: green; -fx-font-size: 18; -fx-font-weight: bold");
                data.addSeat(button.getText());
            }
            if(data.seatIsEmpty()){
                ButtonNext.setVisible(false);
                cart.setText("");
            }
            else{
                ButtonNext.setVisible(true);
                String Ticketnumber = String.valueOf(data.seatSize());
                double taxValue = (((double)data.movie.getPrice())/100)*data.movie.getTax();
                data.tax=taxValue * data.seatSize();
                data.setTotalPrice((((double)data.movie.getPrice())+taxValue)*data.seatSize());
                String TicketPrice = String.valueOf((((double)data.movie.getPrice())+taxValue)*data.seatSize());
                cart.setText("Ticket  x"+Ticketnumber +"   ₺"+TicketPrice+"\n");
            }
        });
    }

    /**
     * Navigates to the next screen (Stage 4).
     * @param event the ActionEvent triggered by clicking the Next button.
     */
    @FXML
    private void NextScreen(ActionEvent event) {
        try {
            // Load the second screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/stage4.fxml"));
            Parent root = loader.load();

            data.cartContent = cart.getText();

            stage4Controller nextSceneDataPass = loader.getController();
            nextSceneDataPass.setData(data);
            nextSceneDataPass.setUser(user);
            
            // Create a new scene and set it to the current stage
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates to the previous screen (Stage 2).
     * @param event the ActionEvent triggered by clicking the Prev button.
     */
    @FXML
    private void PrevScreen(ActionEvent event){
        try {
            // Load the second screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/stage2.fxml"));
            Parent root = loader.load();

            data.clearSelectedSeats();
            stage2Controller prev = loader.getController();
            prev.setData(data);
            prev.setUser(user);
                       
            // Create a new scene and set it to the current stage
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs out the current user and navigates back to the login screen.
     */
    @FXML
    public void logout() {
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
     * Adjusts the stage to fill the entire screen.
     * @param stage the Stage to adjust.
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
