/**
 * Controller class for Stage 1 in the application.
 * Handles the user interface and logic for movie browsing, searching, and transitioning to the next screen.
 */
package com.group15.controllers;

import com.group15.DataPass;
import com.group15.Facade;
import com.group15.Movie;
import com.group15.User;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;


import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;


public class stage1Controller {

    @FXML
    private Label Name;
    @FXML
    private Label Surname;
    @FXML
    private Label Username;
    @FXML
    private Label Role;
    @FXML
    private Button ButtonNext;
    @FXML
    private TextField movieNameField;
    @FXML
    private TextArea movieSummaryField;
    @FXML
    private TextField movieGenreField;
    @FXML
    private ChoiceBox<String> searchChoices;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie,String> movieColumn;
    @FXML
    private TableColumn<Movie,String> genreColumn;
    @FXML
    private ImageView poster;
    @FXML
    private Button closeButton;


    
    public DataPass data = new DataPass();

    private Facade facade = new Facade();

    private User user;


    /**
     * Sets the user details and updates the labels to display the user's information.
     *
     * @param user The user whose details are to be displayed.
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
     * Initializes the stage by setting up the search functionality,
     * populating the movie table, and configuring event listeners.
     */
    @FXML
    public void initialize(){
        ObservableList<Movie> movieList;
        ButtonNext.setVisible(false);
        searchChoices.getItems().addAll("by Genre", "by Name");
        searchChoices.setValue("by Name");
        movieTable.setEditable(false);
        movieColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        Image image = new Image(getClass().getResource("/images/extra/default_poster.jpg").toExternalForm());
        poster.setImage(image);

        performSearch(null);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            performSearch(newValue);
        });
        searchChoices.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            performSearch(searchField.getText());
        });

        if(searchField.getText() == null || searchField.getText().isBlank()){
            movieList = FXCollections.observableArrayList(facade.getAllMovies());
            movieTable.setItems(movieList);
        }

        movieTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {  // Sadece tek tıklama için
                movieTable.refresh();
                Movie selectedmovie = movieTable.getSelectionModel().getSelectedItem();  // Seçilen satırı al
                if (selectedmovie != null) {
                     showMovieDetails(selectedmovie);
                     data.movie = selectedmovie;
                     ButtonNext.setVisible(true);
                }
                else{
                    ButtonNext.setVisible(false);;
                }
            }
        });
    }

    /**
     * Logs out the current user and transitions to the login screen.
     */
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

    /**
     * Maximizes the stage to fill the screen.
     *
     * @param stage The stage to be maximized.
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
     * Navigates to the next screen, passing the user and movie data to the next controller.
     *
     * @param event The event that triggers the screen transition.
     */
    @FXML 
    public void nextScreen(ActionEvent event){
        try {
            // Load the second screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group15/stage2.fxml"));
            Parent root = loader.load();

            stage2Controller nextScreenData = loader.getController();

            nextScreenData.setUser(user);
            nextScreenData.setData(data);
            // Create a new scene and set it to the current stage
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Performs a search based on the current search field value and selected criteria.
     *
     * @param searchString The string to search for.
     */
    private void performSearch(String searchString){
        ObservableList<Movie> movieList;
        String choice = searchChoices.getValue();

        if(searchString == null || searchString.isBlank()){
            movieList = FXCollections.observableArrayList(facade.getAllMovies());
            movieTable.setItems(movieList);
        }

        if(choice.equals("by Name")){
            movieList = FXCollections.observableArrayList(facade.filterMoviesByTitle(searchString));
            movieTable.setItems(movieList);
        }
        else{
            movieList = FXCollections.observableArrayList(facade.filterMoviesByGenre(searchString));
            movieTable.setItems(movieList);
        }
        movieTable.refresh();
    }


    /**
     * Displays the details of a selected movie in the appropriate fields.
     *
     * @param movie The movie whose details are to be displayed.
     */
    private void showMovieDetails(Movie movie){
        data.movie=movie;
        movieNameField.setText(movie.getTitle());
        movieGenreField.setText(movie.getGenre());
        movieSummaryField.setText(movie.getSummary());

        String imagePath = movie.getPoster();
        Image image = new Image(getClass().getResource("/images/posters/" + imagePath + ".jpg").toExternalForm());
        poster.setImage(image);
    }

}
