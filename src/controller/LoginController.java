package controller;

import db.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    public void login() {
        // Hardcoded credentials for testing
        String username = usernameField.getText();
        String password = passwordField.getText();


        //Username-"admin"
        //Password-"12345"
        if (username.equals("admin") && password.equals("12345")) {
            errorLabel.setText("Login Successful!");
            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setVisible(true);
        } else {
            errorLabel.setText("Invalid username or password!");
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setVisible(true);
        }
    }

    private boolean validateCredentials(String username, String password) {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Returns true if a record is found
        } catch (SQLException e) {
            System.out.println("Database query failed: " + e.getMessage());
        }
        return false;
    }

    @FXML
    public void reset() {
        usernameField.clear();
        passwordField.clear();
        errorLabel.setVisible(false);
    }
}
