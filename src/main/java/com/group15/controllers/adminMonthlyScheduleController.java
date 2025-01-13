/**
 * Controller class for managing the admin monthly schedule view.
 * Provides functionalities for viewing, filtering, and managing movie schedules.
 */
package com.group15.controllers;
import com.group15.Facade;
import com.group15.Movie;
import com.group15.Schedule;
import com.group15.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javafx.event.ActionEvent;

/**
 * Controller for the Admin Monthly Schedule interface.
 * Handles schedule management, including adding, filtering, and deleting schedules.
 */
public class adminMonthlyScheduleController {

    /** Table for displaying schedules. */
    @FXML
    private TableView<Schedule> scheduleTable;

    /** Column for displaying schedule dates. */
    @FXML
    private TableColumn<Schedule, Date> dateColumn;

    /** Column for displaying schedule times. */
    @FXML
    private TableColumn<Schedule, Time> timeColumn;

    /** Column for displaying hall names. */
    @FXML
    private TableColumn<Schedule, String> hallColumn;

    /** Column for displaying movie titles. */
    @FXML
    private TableColumn<Schedule, String> titleColumn;

    /** Column for displaying sold tickets. */
    @FXML
    private TableColumn<Schedule, Integer> soldTicketColumn;

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
    private Button closeButton;

    /** Button for saving changes. */
    @FXML
    private Button saveButton;// Correct type for closeButton

    /** Button for navigating to the movie management view. */
    @FXML
    private Button movieManagementButton;

    /** Button for navigating to the monthly schedule view. */
    @FXML
    private Button monthlyScheduleButton;

    /** Button for navigating to the cancellation view. */
    @FXML
    private Button cancellationButton;

    /** DatePicker for filtering schedules by date. */
    @FXML
    private DatePicker datePickerFilter;

    /** DatePicker for adding new schedules. */
    @FXML
    private DatePicker datePickerAdd;

    /** Button for adding a new schedule. */
    @FXML
    private Button addScheduleButton;

    /** ChoiceBox for selecting schedule times. */
    @FXML
    private ChoiceBox<String> timeChoiceBox;

    /** ChoiceBox for selecting movies. */
    @FXML
    private ChoiceBox<String> movieChoiceBox;

    /** RadioButton for selecting Hall A. */
    @FXML
    private RadioButton radioButtonA;

    /** RadioButton for selecting Hall B. */
    @FXML
    private RadioButton radioButtonB;

    /** Facade for managing backend operations. */
    private Facade facade;

    /** Current admin user. */
    private User user;

    /** Observable list for managing schedule data. */
    private ObservableList<Schedule> scheduleList = FXCollections.observableArrayList();

    /**
     * Constructor for initializing the admin monthly schedule controller.
     */
    public adminMonthlyScheduleController() {
        this.facade = new Facade();
        this.scheduleList = FXCollections.observableArrayList(); // Initialize the list
    }

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
     * Handles date selection for filtering schedules.
     *
     * @param event The action event.
     */
    @FXML
    private void handleDatePickerAction(ActionEvent event) {
        LocalDate selectedDate = datePickerFilter.getValue();
        loadSchedules(selectedDate);
    }

    /**
     * Handles date selection for adding new schedules.
     *
     * @param event The action event.
     * @return The selected date.
     */
    @FXML
    private LocalDate handleDatePickerAddAction(ActionEvent event) {
        return datePickerAdd.getValue();
    }

    /**
     * Initializes the controller and sets up UI components.
     */
    @FXML
    public void initialize(){
        // Add context menu for deletion
        scheduleTable.setRowFactory(tv -> {
            TableRow<Schedule> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Delete Schedule");
            deleteItem.setOnAction(event -> {
                Schedule selectedSchedule = row.getItem();
                if (selectedSchedule != null && selectedSchedule.getScheduleId() != 0 && selectedSchedule.getTakenSeats() <= 0) {
                    // Remove the schedule from the database
                    facade.deleteSchedule(selectedSchedule.getScheduleId());
                    // Remove the user from the table's observable list
                    scheduleTable.getItems().remove(selectedSchedule);
                }
                if(selectedSchedule != null && selectedSchedule.getTakenSeats() > 0)
                    showAlert("Error", "You can not delete this schedule there are sold tickets.");
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

        setupRadioButtons();
        populateMovieChoiceBox();
        setupTimeChoiceBox();
        setupDatePicker();

        // Set up table columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("sessionTime"));
        hallColumn.setCellValueFactory(new PropertyValueFactory<>("hall"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        soldTicketColumn.setCellValueFactory(new PropertyValueFactory<>("takenSeats"));
        loadSchedules(null);


    }

    /**
     * Loads schedules into the table based on the selected date.
     *
     * @param selectedDate The selected date for filtering schedules.
     */
    private void loadSchedules(LocalDate selectedDate) {
        scheduleList.clear();
        List<Schedule> schedules = facade.getSchedules(selectedDate);
        scheduleList.addAll(schedules);
        scheduleTable.setItems(scheduleList);
    }

    /**
     * Sets up the radio buttons for selecting halls.
     */
    private void setupRadioButtons() {
        ToggleGroup group = new ToggleGroup();
        radioButtonA.setToggleGroup(group);
        radioButtonB.setToggleGroup(group);
    }

    /**
     * Populates the movie choice box with available movies.
     */
    private void populateMovieChoiceBox() {
        try {
            List<Movie> movies = facade.getAllMovies();
            for (Movie movie : movies) {
                movieChoiceBox.getItems().add(movie.getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the time choice box with predefined session times.
     */
    private void setupTimeChoiceBox() {
        timeChoiceBox.setItems(FXCollections.observableArrayList("12:00:00", "14:00:00", "16:00:00", "18:00:00", "20:00:00", "22:00:00"));
    }

    /**
     * Configures the date picker for adding schedules to only allow future dates.
     */
    private void setupDatePicker() {
        datePickerAdd.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
        datePickerAdd.setPromptText("Select a Date");
    }


    /**
     * Sets up the table columns for displaying schedule data.
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
     * Handles the navigation to the monthly schedule view.
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
     * Handles the navigation to the cancellation view.
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
     * Handles closing the current view and returning to the login view.
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
     * Handles saving changes and returning to the admin menu.
     *
     * @throws IOException If an error occurs during navigation.
     */
    public void handleSaveButton() throws IOException {

        // Load the admin menu scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/adminMenuGUI.fxml"));
        Parent adminMenuRoot = loader.load();

        // Get the admin controller and pass the user object
        AdminController adminController = loader.getController();
        adminController.setUser(user);

        // Set the admin menu scene on the current stage
        Stage currentStage = (Stage) saveButton.getScene().getWindow();
        currentStage.setScene(new Scene(adminMenuRoot));
        makeStageFillScreen(currentStage);
        currentStage.show();
    }

    /**
     * Handles adding a new schedule to the system.
     */
    @FXML
    private void handleAddScheduleButton() {
        try {
            // Get selected movie title
            String selectedMovieTitle = movieChoiceBox.getValue();
            if (selectedMovieTitle == null) {
                showAlert("Error", "Please select a movie.");
                return;
            }

            // Get selected movie object
            Movie selectedMovie = facade.getMovieByTitle(selectedMovieTitle);
            if (selectedMovie == null) {
                showAlert("Error", "Selected movie could not be found.");
                return;
            }

            // Get selected date
            LocalDate selectedDate = datePickerAdd.getValue();
            if (selectedDate == null) {
                showAlert("Error", "Please select a date.");
                return;
            }

            // Get selected session time
            String selectedSessionTime = timeChoiceBox.getValue();
            if (selectedSessionTime == null) {
                showAlert("Error", "Please select a session time.");
                return;
            }

            // Determine hall name and seating
            String hallName = radioButtonA.isSelected() ? "Hall A" : radioButtonB.isSelected() ? "Hall B" : null;
            if (hallName == null) {
                showAlert("Error", "Please select a hall.");
                return;
            }

            String seating = hallName.equals("Hall A") ? "f".repeat(16) : "f".repeat(48);

            // Create Schedule object
            Schedule newSchedule = new Schedule();
            newSchedule.setMovieId(selectedMovie.getMovieId());
            newSchedule.setDate(java.sql.Date.valueOf(selectedDate));
            newSchedule.setSessionTime(Time.valueOf(selectedSessionTime));
            newSchedule.setHall(hallName);
            newSchedule.setTakenSeats(0); // Assuming no seats are taken initially
            newSchedule.setSeating(seating);

            if(facade.checkCollapseInSchedule(java.sql.Date.valueOf(selectedDate), Time.valueOf(selectedSessionTime), hallName))
                showAlert("Error", "You can not add this schedule. Already there is a schedule at this date, time, and hall.");
            else{
                // Add schedule using facade
                facade.addNewSchedule(newSchedule);
                showAlert("Success", "Schedule added successfully.");
                loadSchedules(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while adding the schedule.");
        }
    }

    /**
     * Displays an alert with a given title and message.
     *
     * @param title   The title of the alert.
     * @param message The message to display.
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