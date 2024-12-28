package com.group15.controllers;

import com.group15.Facade;
import com.group15.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    public TextField usernameField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public Label errorLabel;

    @FXML
    public void handleLoginButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("admin") && password.equals("password")) {
            errorLabel.setText("Login successful!");
            errorLabel.setDisable(false);
        } else {
            errorLabel.setText("Invalid username or password. Please try again.");
            errorLabel.setDisable(false); // Enable the error label to show the message
        }
    }
}