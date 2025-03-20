package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

import db.DBConnection;

public class SignUpController {

    @FXML private TextField fullNameField; // Input field for full name
    @FXML private TextField emailField; // Input field for email
    @FXML private TextField phoneNumberField; // Input field for phone number
    @FXML private TextField dateOfBirthField; // Input field for date of birth
    @FXML private TextField addressField; // Input field for address
    @FXML private PasswordField passwordField; // Input field for password
    @FXML private PasswordField confirmPasswordField; // Input field for confirming password
    @FXML private Label errorLabel; // Label to display error messages

    @FXML
    private void handleSignUp(ActionEvent event) { // Handle sign-up button click
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String dateOfBirth = dateOfBirthField.getText().trim();
        String address = addressField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || dateOfBirth.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("All fields are required!"); // Validate all fields are filled
            return;
        }

        if (!password.equals(confirmPassword)) { // Validate password match
            showError("Passwords do not match!");
            return;
        }

        if (saveStudent(fullName, email, phoneNumber, dateOfBirth, address, password)) { // Save student data to database
            showAlert("Success", "Student registered successfully!"); // Show success message
            navigateToLogin(event); // Redirect to login page
        } else {
            showError("Error saving student data!"); // Show error message
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) { // Handle cancel button click
        fullNameField.clear();
        emailField.clear();
        phoneNumberField.clear();
        dateOfBirthField.clear();
        addressField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        errorLabel.setVisible(false); // Clear form and hide error message
    }

    @FXML
    private void backToLogin(ActionEvent event) throws IOException { // Handle back to login button click
        navigateToLogin(event); // Redirect to login page
    }

    private boolean saveStudent(String fullName, String email, String phoneNumber, String dateOfBirth, String address, String password) { // Save student to database
        String query = "INSERT INTO students (name, email, phone_number, date_of_birth, address, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.setString(3, phoneNumber);
            stmt.setDate(4, Date.valueOf(dateOfBirth)); // Convert string to SQL date
            stmt.setString(5, address);
            stmt.setString(6, password);
            return stmt.executeUpdate() > 0; // Return true if insertion is successful
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database error: " + e.getMessage()); // Show database error
            return false;
        }
    }

    private void showError(String message) { // Display error message
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void showAlert(String title, String message) { // Show success alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToLogin(ActionEvent event) { // Redirect to login page
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(loginPage));
            window.centerOnScreen();
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error navigating to login page!"); // Show navigation error
        }
    }
}