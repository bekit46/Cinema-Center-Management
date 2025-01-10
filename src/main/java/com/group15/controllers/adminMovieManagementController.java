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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class adminMovieManagementController {
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie, String> posterColumn;
    @FXML
    private TableColumn<Movie, String> titleColumn;
    @FXML
    private TableColumn<Movie, String> genreColumn;
    @FXML
    private TableColumn<Movie, String> summaryColumn;


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

    private ObservableList<Movie> modifiedMovies;
    private User user;
    private Facade facade;

    public adminMovieManagementController() {
        this.facade = new Facade();
        this.modifiedMovies = FXCollections.observableArrayList(); // Initialize the list
    }

    public void setUser(User user) {
        this.user = user;
        // Update the labels with the user's information
        usernameLabel.setText(user.getUsername());
        nameSurnameLabel.setText(user.getName() + " " + user.getSurname());
        roleLabel.setText(user.getRole());
    }

    @FXML
    private void initialize(){
        // Add context menu for deletion
        movieTable.setRowFactory(tv -> {
            TableRow<Movie> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Delete Movie");
            deleteItem.setOnAction(event -> {
                Movie selectedMovie = row.getItem();
                List<Schedule> schedules = facade.getSchedulesByMovieId(selectedMovie.getMovieId());
                // Check if any schedule has taken seats
                boolean hasTakenSeats = schedules.stream().anyMatch(schedule -> schedule.getTakenSeats() > 0);
                if (!hasTakenSeats) {
                    // Remove the movie from the database
                    facade.deleteMovie(selectedMovie.getMovieId());
                    showAlert("Success", "Movie deleted successfully.");
                    // Remove the movie from the table's observable list
                    movieTable.getItems().remove(selectedMovie);
                } else {
                    showAlert("Error", "Movie cannot be deleted because it has a schedule with taken seats.");
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

        // Initialize the table columns
        movieTable.setEditable(true);
        posterColumn.setEditable(true);
        titleColumn.setEditable(true);
        genreColumn.setEditable(true);
        summaryColumn.setEditable(true);
        movieTable.getSelectionModel().setCellSelectionEnabled(true);

        posterColumn.setCellValueFactory(new PropertyValueFactory<>("poster"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        summaryColumn.setCellValueFactory(new PropertyValueFactory<>("summary"));
        loadMovieData();

        posterColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        posterColumn.setOnEditCommit(event -> {
            Movie movie = event.getRowValue();
            String newPoster = event.getNewValue();
            if(newPoster.isEmpty())
                showAlert("Error", "Poster path can not be empty.");
            else{
                movie.setPoster(newPoster);
                // Check if this is the placeholder row
                if (!modifiedMovies.contains(movie)) {
                    modifiedMovies.add(movie);
                }
                refreshTableWithPlaceholder();
            }
            movieTable.refresh();
        });

        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        titleColumn.setOnEditCommit(event -> {
            Movie movie = event.getRowValue();
            String newTitle = event.getNewValue();
            if(newTitle.isEmpty())
                showAlert("Error", "Movie title can not be empty.");
            else{
                movie.setTitle(newTitle);
                // Check if this is the placeholder row
                if (!modifiedMovies.contains(movie)) {
                    modifiedMovies.add(movie);
                }
                refreshTableWithPlaceholder();
            }
            movieTable.refresh();
        });

        genreColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        genreColumn.setOnEditCommit(event -> {
            Movie movie = event.getRowValue();
            String newGenre = event.getNewValue();
            if(newGenre.isEmpty())
                showAlert("Error", "Movie genre can not be empty.");
            else{
                movie.setGenre(newGenre);
                // Check if this is the placeholder row
                if (!modifiedMovies.contains(movie)) {
                    modifiedMovies.add(movie);
                }
                refreshTableWithPlaceholder();
            }
            movieTable.refresh();
        });

        summaryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        summaryColumn.setOnEditCommit(event -> {
            Movie movie = event.getRowValue();
            String newSummary = event.getNewValue();
            if(newSummary.isEmpty())
                showAlert("Error", "Movie summary can not be empty.");
            else{
                movie.setSummary(newSummary);
                // Check if this is the placeholder row
                if (!modifiedMovies.contains(movie)) {
                    modifiedMovies.add(movie);
                }
                refreshTableWithPlaceholder();
            }
            movieTable.refresh();
        });
    }

    private void refreshTableWithPlaceholder() {
        ObservableList<Movie> currentMovies = FXCollections.observableArrayList(movieTable.getItems());

        // Check if a placeholder already exists
        boolean hasPlaceholder = currentMovies.stream()
                .anyMatch(movie -> movie.getMovieId() == 0);
        // If no placeholder exists, add one
        if (!hasPlaceholder) {
            currentMovies.add(new Movie(0, "", "", "", "", 0, 0, 0)); // Add placeholder row
        }
        movieTable.setItems(currentMovies);
    }

    private void loadMovieData() {
        ObservableList<Movie> movies = FXCollections.observableArrayList(facade.getAllMovies());
        Movie placeholder = new Movie(0, "", "", "", "", 0, 0, 0); // Adjust fields as needed
        movies.add(placeholder);
        movieTable.setItems(movies);
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
        try {
            // Iterate over modified movies and update them in the database
            for (Movie modifiedMovie : modifiedMovies) {
                if (modifiedMovie.getMovieId() != 0) { // Ensure it's not a placeholder
                    facade.updateMovie(modifiedMovie);
                }
                else {
                    facade.addNewMovie(modifiedMovie);
                }
            }
            // Optionally, clear the modified list after saving
            modifiedMovies.clear();

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
        } catch (IOException e) {
            e.printStackTrace();
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
}
