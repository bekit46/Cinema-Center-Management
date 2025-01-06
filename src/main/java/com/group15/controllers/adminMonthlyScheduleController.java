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


public class adminMonthlyScheduleController {
    @FXML
    private TableView<Schedule> scheduleTable;
    @FXML
    private TableColumn<Schedule, Date> dateColumn;
    @FXML
    private TableColumn<Schedule, Time> timeColumn;
    @FXML
    private TableColumn<Schedule, String> hallColumn;
    @FXML
    private TableColumn<Schedule, String> titleColumn;
    @FXML
    private TableColumn<Schedule, Integer> soldTicketColumn;

    @FXML
    private Label usernameLabel;
    @FXML
    private Label nameSurnameLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Button closeButton;
    @FXML
    private Button saveButton;// Correct type for closeButton
    @FXML
    private Button movieManagementButton;
    @FXML
    private Button monthlyScheduleButton;
    @FXML
    private Button cancellationButton;
    @FXML
    private DatePicker datePickerFilter;
    @FXML
    private DatePicker datePickerAdd;
    @FXML
    private Button addScheduleButton;
    @FXML
    private ChoiceBox<String> timeChoiceBox;
    @FXML
    private ChoiceBox<String> movieChoiceBox;
    @FXML
    private RadioButton radioButtonA;
    @FXML
    private RadioButton radioButtonB;


    private Facade facade;
    private User user;
    private ObservableList<Schedule> scheduleList = FXCollections.observableArrayList();

    public adminMonthlyScheduleController() {
        this.facade = new Facade();
        this.scheduleList = FXCollections.observableArrayList(); // Initialize the list
    }

    public void setUser(User user) {
        this.user = user;
        // Update the labels with the user's information
        usernameLabel.setText(user.getUsername());
        nameSurnameLabel.setText(user.getName() + " " + user.getSurname());
        roleLabel.setText(user.getRole());
    }

    @FXML
    private void handleDatePickerAction(ActionEvent event) {
        LocalDate selectedDate = datePickerFilter.getValue();
        loadSchedules(selectedDate);
    }

    @FXML
    private LocalDate handleDatePickerAddAction(ActionEvent event) {
        return datePickerAdd.getValue();
    }

    @FXML
    public void initialize(){
        // Add context menu for deletion
        scheduleTable.setRowFactory(tv -> {
            TableRow<Schedule> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Delete Schedule");
            deleteItem.setOnAction(event -> {
                Schedule selectedSchedule = row.getItem();
                if (selectedSchedule != null && selectedSchedule.getScheduleId() != 0 && selectedSchedule.getTakenSeats() == 0) {
                    // Remove the schedule from the database
                    facade.deleteSchedule(selectedSchedule.getScheduleId());
                    // Remove the user from the table's observable list
                    scheduleTable.getItems().remove(selectedSchedule);
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

    private void loadSchedules(LocalDate selectedDate) {
        scheduleList.clear();
        List<Schedule> schedules = facade.getSchedules(selectedDate);
        scheduleList.addAll(schedules);
        scheduleTable.setItems(scheduleList);
    }

    private void setupRadioButtons() {
        ToggleGroup group = new ToggleGroup();
        radioButtonA.setToggleGroup(group);
        radioButtonB.setToggleGroup(group);
    }

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

    private void setupTimeChoiceBox() {
        timeChoiceBox.setItems(FXCollections.observableArrayList("12:00:00", "14:00:00", "16:00:00", "18:00:00", "20:00:00", "22:00:00"));
    }

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

            // Add schedule using facade
            facade.addNewSchedule(newSchedule);
            showAlert("Success", "Schedule added successfully.");
            loadSchedules(null);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while adding the schedule.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
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
