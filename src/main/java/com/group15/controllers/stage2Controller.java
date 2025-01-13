/**
 * Controller class for Stage 2 of the application.
 * This class handles the logic for the second screen, including displaying movie details,
 * managing session selection, handling user interactions, and navigating between screens.
 */
package com.group15.controllers;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.group15.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class stage2Controller {

    @FXML
    private TextField movieNameField;

    @FXML
    private TextField movieGenreField;

    @FXML
    private TextArea movieSummaryField;

    @FXML
    private ImageView poster;

    @FXML
    private Button ButtonNext;

    @FXML
    private Button ButtonPrev;

    @FXML
    private Label Surname;

    @FXML
    private Label Name;

    @FXML
    private Label Username;

    @FXML
    private Label Role;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button SessionA1, SessionA2, SessionA3, SessionA4, SessionA5, SessionA6;

    @FXML
    private Button SessionB1, SessionB2, SessionB3, SessionB4, SessionB5, SessionB6;

    @FXML
    private Label vacantA1, vacantA2, vacantA3, vacantA4, vacantA5, vacantA6;

    @FXML
    private Label vacantB1, vacantB2, vacantB3, vacantB4, vacantB5, vacantB6;

    @FXML
    private Button closeButton;

    private Button lastPressedButton = null;

    public DataPass data;

    private String Hall = "";

    private User user;

    public Facade facade=new Facade();

    /**
     * Sets the user information and updates the UI labels.
     *
     * @param user the current user object.
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
     * Sets the data object and updates the UI with movie details.
     *
     * @param data the DataPass object containing movie and schedule information.
     */
    public void setData(DataPass data){
        this.data=data;
        Movie movie = data.movie;
        movieNameField.setText(movie.getTitle());
        movieGenreField.setText(movie.getGenre());
        movieSummaryField.setText(movie.getSummary());

        String imagePath = movie.getPoster();
        if(imagePath == null||imagePath.isEmpty()){
            imagePath = " ";
        }
        Image image;
        try{
            image = new Image(getClass().getResource("/images/posters/" + imagePath + ".jpg").toExternalForm());
            if(image.isError()){
                throw new IllegalArgumentException("Image not found");
            }
        }
        catch (Exception e){
            image = new Image(getClass().getResource("/images/extra/default_poster.jpg").toExternalForm());
        }
        poster.setImage(image);
    }

    /**
     * Initializes the controller, setting up event handlers and configuring the UI.
     */
    @FXML
    public void initialize() {

        ButtonNext.setOnAction(event -> {
            try {
                nextScreen(event); // Call the nextScreen method
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        ButtonPrev.setOnAction(event -> {
            try {
                prevScreen(); // Call the nextScreen method
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        SessionA1.setStyle("-fx-font-size: 20; -fx-background-color: grey;");
        SessionA2.setStyle("-fx-font-size: 20; -fx-background-color: grey;");
        SessionA3.setStyle("-fx-font-size: 20; -fx-background-color: grey;");
        SessionA4.setStyle("-fx-font-size: 20; -fx-background-color: grey;");
        SessionA5.setStyle("-fx-font-size: 20; -fx-background-color: grey;");
        SessionA6.setStyle("-fx-font-size: 20; -fx-background-color: grey;");
        SessionB1.setStyle("-fx-font-size: 20; -fx-background-color: grey;");
        SessionB2.setStyle("-fx-font-size: 20; -fx-background-color: grey;");
        SessionB3.setStyle("-fx-font-size: 20; -fx-background-color: grey;");
        SessionB4.setStyle("-fx-font-size: 20; -fx-background-color: grey;");
        SessionB5.setStyle("-fx-font-size: 20; -fx-background-color: grey;");
        SessionB6.setStyle("-fx-font-size: 20; -fx-background-color: grey;");


        LocalDate today = LocalDate.now();

        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
    
                // Bugün öncesindeki tarihleri devre dışı bırak
                if (date.isBefore(today)) {
                    setDisable(true);
                    setStyle("-fx-background-color: grey;"); // Stil: Pembe arka plan
                }
            }
        });
    
        // Tarih değişimlerini dinleyen listener
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleDateSelected(newValue);
            }
        });



        ButtonNext.setVisible(false);
    }

    /**
     * Handles navigation to the next screen based on the selected hall.
     *
     * @param event the action event triggered by the button.
     */
    public void nextScreen(ActionEvent event) {
        try {
            // Load the second screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/"+Hall+".fxml"));
            Parent root = loader.load();

            if(Hall.equals("hallB")) {
                hallBController nextScreenData = loader.getController();
                nextScreenData.setData(data);
                nextScreenData.setUser(user);
            } else {
                hallAController nextScreenData = loader.getController();
                nextScreenData.setData(data);
                nextScreenData.setUser(user);
            }

            // Create a new scene and set it to the current stage
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles navigation to the previous screen.
     */
    @FXML
    public void prevScreen() {
        try {
            // Load the second screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/stage1.fxml"));
            Parent root = loader.load();

            stage1Controller prev = loader.getController();
            prev.setUser(user);

            // Create a new scene and set it to the current stage
            Stage stage = (Stage) ButtonPrev.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Logs out the user and navigates back to the login screen.
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
     * Maximizes the stage to fill the screen.
     *
     * @param stage the stage to maximize.
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
     * Handles the selection of a session button and updates the schedule data.
     *
     * @param pressedButton the session button that was pressed.
     */
    private void sessionPickButton(Button pressedButton) {
        // Reset the style of the last pressed button
        if (lastPressedButton != null) {
            lastPressedButton.setStyle("-fx-font-size: 20; -fx-background-color: #009EF7;");
        }

        // Set the style for the newly pressed button
        pressedButton.setStyle("-fx-font-size: 20; -fx-background-color: green;");
        lastPressedButton = pressedButton;

        // Make the next button visible
        ButtonNext.setVisible(true);

        // Determine the hall and session time based on the button
        String hall = pressedButton.getLayoutY() == 280.0 ? "Hall A" : "Hall B";
        Hall = hall.equals("Hall A") ? "hallA" : "hallB";
        String timeString = determineSessionTime(pressedButton);

        // Convert timeString to Time object
        Time sessionTime = convertToTime(timeString);

        // Find the matching schedule for the selected hall and session time
        Schedule selectedSchedule = null;
        for (Schedule schedule : facade.getSchedules(data.selectedDate)) {
            if (schedule.getHall().equals(hall) && schedule.getSessionTime().equals(sessionTime)) {
                selectedSchedule = schedule;
                break;
            }
        }

        // Check if a matching schedule was found
        if (selectedSchedule == null) {
            System.err.println("No matching schedule found for hall: " + hall + " and time: " + timeString);
            return;
        }

        // Update the Schedule object with the selected schedule details
        selectedSchedule.setHall(hall);
        selectedSchedule.setSessionTime(sessionTime);
        selectedSchedule.setMovieId(data.movie.getMovieId());
        selectedSchedule.setMovie(data.movie);

        // Save the selected schedule into the data object
        data.schedule = selectedSchedule; // Save the schedule into the data object
    }



    //sql query

    /**
     * Handles date selection and updates the UI with the corresponding schedules.
     *
     * @param selectedDate the selected date.
     */
    private void handleDateSelected(LocalDate selectedDate) {
        // Retrieve schedules for the selected date
        data.selectedDate = selectedDate;
        List<Schedule> schedules = facade.getSchedules(selectedDate, data.movie.getMovieId());
        lastPressedButton=null;
        // Initialize vacant text fields to "-"
        vacantA1.setText("-");
        vacantA2.setText("-");
        vacantA3.setText("-");
        vacantA4.setText("-");
        vacantA5.setText("-");
        vacantA6.setText("-");
        vacantB1.setText("-");
        vacantB2.setText("-");
        vacantB3.setText("-");
        vacantB4.setText("-");
        vacantB5.setText("-");
        vacantB6.setText("-");

        // Define the mapping of hall and time to vacant fields
        Map<String, Label> vacantMap = new HashMap<>();
        vacantMap.put("HallA12:00", vacantA1);
        vacantMap.put("HallA14:00", vacantA2);
        vacantMap.put("HallA16:00", vacantA3);
        vacantMap.put("HallA18:00", vacantA4);
        vacantMap.put("HallA20:00", vacantA5);
        vacantMap.put("HallA22:00", vacantA6);
        vacantMap.put("HallB12:00", vacantB1);
        vacantMap.put("HallB14:00", vacantB2);
        vacantMap.put("HallB16:00", vacantB3);
        vacantMap.put("HallB18:00", vacantB4);
        vacantMap.put("HallB20:00", vacantB5);
        vacantMap.put("HallB22:00", vacantB6);

        // Format for extracting the session time
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

        // Iterate over the schedules and assign values to vacant fields
        for (Schedule schedule : schedules) {
            Time time = schedule.getSessionTime();
            String timeString = formatter.format(time); // Format the time
            String hall = schedule.getHall(); // Get the hall
            if(hall.equals("Hall A"))
                hall = "HallA";
            else
                hall="HallB";
            // Determine the key for the map
            String key = hall + timeString;
            if (vacantMap.containsKey(key)) {
                // Set the vacant field to the schedule's vacant value
                if(hall.equals("HallA"))
                    vacantMap.get(key).setText(String.valueOf(16-schedule.getTakenSeats()));
                else
                    vacantMap.get(key).setText(String.valueOf(48-schedule.getTakenSeats()));
            }
        }

        // Update button styles and actions based on vacant values
        updateButtonStylesAndActions();
    }

    /**
     * Updates the styles and actions of session buttons based on vacancy.
     */
    private void updateButtonStylesAndActions() {
        updateButtonStyle(SessionA1, vacantA1);
        updateButtonStyle(SessionA2, vacantA2);
        updateButtonStyle(SessionA3, vacantA3);
        updateButtonStyle(SessionA4, vacantA4);
        updateButtonStyle(SessionA5, vacantA5);
        updateButtonStyle(SessionA6, vacantA6);
        updateButtonStyle(SessionB1, vacantB1);
        updateButtonStyle(SessionB2, vacantB2);
        updateButtonStyle(SessionB3, vacantB3);
        updateButtonStyle(SessionB4, vacantB4);
        updateButtonStyle(SessionB5, vacantB5);
        updateButtonStyle(SessionB6, vacantB6);
    }

    /**
     * Updates the style and action of a session button based on vacancy.
     *
     * @param sessionButton the session button.
     * @param vacantText    the label displaying the vacancy.
     */
    private void updateButtonStyle(Button sessionButton, Label vacantText) {
        if (!vacantText.getText().equals("0") && !vacantText.getText().equals("-")) {
            sessionButton.setStyle("-fx-font-size: 20; -fx-background-color: #009EF7;");
            sessionButton.setOnAction(event -> sessionPickButton(sessionButton));
        } else {
            sessionButton.setStyle("-fx-font-size: 20; -fx-background-color: red;");
            sessionButton.setOnAction(null);
        }
    }

    /**
     * Determines the session time based on the pressed button.
     *
     * @param pressedButton the pressed session button.
     * @return the session time as a string.
     */
    private String determineSessionTime(Button pressedButton) {
        if (pressedButton == SessionA1) return "12:00";
        if (pressedButton == SessionA2) return "14:00";
        if (pressedButton == SessionA3) return "16:00";
        if (pressedButton == SessionA4) return "18:00";
        if (pressedButton == SessionA5) return "20:00";
        if (pressedButton == SessionA6) return "22:00";
        if (pressedButton == SessionB1) return "12:00";
        if (pressedButton == SessionB2) return "14:00";
        if (pressedButton == SessionB3) return "16:00";
        if (pressedButton == SessionB4) return "18:00";
        if (pressedButton == SessionB5) return "20:00";
        if (pressedButton == SessionB6) return "22:00";

        return null;
    }

    /**
     * Converts a time string to a Time object.
     *
     * @param timeString the time string.
     * @return the Time object, or null if parsing fails.
     */
    private Time convertToTime(String timeString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            java.util.Date date = formatter.parse(timeString);
            return new Time(date.getTime());  // Convert to SQL Time object
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }



}

