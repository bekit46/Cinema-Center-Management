package com.group15.controllers;

import java.io.IOException;

import com.group15.*;
import javafx.event.ActionEvent;
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


public class hallAController {

    @FXML
    public Button ButtonA1;
    @FXML
    public Button ButtonA2;
    @FXML
    public Button ButtonA3;
    @FXML
    public Button ButtonA4;
    @FXML
    public Button ButtonB1;
    @FXML
    public Button ButtonB2;
    @FXML
    public Button ButtonB3;
    @FXML
    public Button ButtonB4;
    @FXML
    public Button ButtonC1;
    @FXML
    public Button ButtonC2;
    @FXML
    public Button ButtonC3;
    @FXML
    public Button ButtonC4;
    @FXML
    public Button ButtonD1;
    @FXML
    public Button ButtonD2;
    @FXML
    public Button ButtonD3;
    @FXML
    public Button ButtonD4;
    @FXML
    public Button ButtonNext, ButtonPrev;
    @FXML
    public Label Surname, Name, Username, Role;
    @FXML
    public TextArea cart;
    @FXML
    public Button closeButton;
    
    public Facade facade = new Facade();



   public DataPass data;


   public void setData(DataPass data){
       this.data = data;
       Schedule schedule = data.schedule;
       String redSeats = "";
       Facade facade = new Facade();
       redSeats = facade.getSeatingById(schedule.getScheduleId());
       int pointer = 0;
       ButtonNext.setVisible(false);

       for (char row = 'A'; row <= 'D'; row++) {
           for (int col = 1; col <= 4; col++) {
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


    @FXML
    public void initialize() {

    }

    private User user;

    public void setUser(User user) {
        this.user = user;
        // Update the labels with the user's information
        Username.setText("Username: "+user.getUsername());
        Name.setText("Name: "+user.getName());
        Surname.setText("Surname: "+user.getSurname());
        Role.setText("Role: "+user.getRole());
    }


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
                data.tax = taxValue * data.seatSize();
                data.setTotalPrice((((double)data.movie.getPrice())+taxValue)*data.seatSize());
                String TicketPrice = String.valueOf((((double)data.movie.getPrice())+taxValue)*data.seatSize());
                cart.setText("Ticket  x"+Ticketnumber +"   ₺"+TicketPrice+"\n");
            }
        });
    }

    @FXML
    private void NextScreen(ActionEvent event){
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

    @FXML
    public void logout(){
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
    public void makeStageFillScreen(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        // Set the stage size to exactly match the screen dimensions
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());
    }
}

