package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBConnection;

public class LoginController {

    @FXML private TextField emailField; // Input field for email
    @FXML private PasswordField passwordField; // Input field for password
    @FXML private Label errorLabel; // Label to display error messages
    @FXML private RadioButton studentRadio; // Radio button for student role
    @FXML private RadioButton adminRadio; // Radio button for admin role
    @FXML private RadioButton facultyRadio; // Radio button for faculty role

    @FXML
    private void reset() { // Reset form fields and error message
        emailField.clear();
        passwordField.clear();
        errorLabel.setText("");
        errorLabel.setVisible(false);
    }

    @FXML
    private void login(ActionEvent event) { // Handle login button click
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) { // Validate input fields
            showError("Email or password cannot be empty!");
            return;
        }

        String role = getSelectedRole(); // Get selected role (Student, Admin, Faculty)
        if (role.isEmpty()) { // Validate role selection
            showError("Please select a role!");
            return;
        }

        if (validateLogin(email, password, role)) { // Validate login credentials
            loadDashboard(event, role, email); // Load appropriate dashboard
        } else {
            showError("Invalid email or password!");
        }
    }

    private void showError(String message) { // Display error message
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private String getSelectedRole() { // Get the selected role from radio buttons
        if (studentRadio.isSelected()) return "Student";
        if (adminRadio.isSelected()) return "Admin";
        if (facultyRadio.isSelected()) return "Faculty";
        return "";
    }

    private boolean validateLogin(String email, String password, String role) { // Validate login against database
        String table = switch (role) { // Determine table based on role
            case "Student" -> "students";
            case "Admin" -> "admins";
            case "Faculty" -> "faculty";
            default -> "";
        };

        if (table.isEmpty()) return false;

        String query = "SELECT * FROM " + table + " WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            return stmt.executeQuery().next(); // Check if a matching record exists
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database error!");
            return false;
        }
    }

    private void loadDashboard(ActionEvent event, String role, String email) { // Load the appropriate dashboard
        String fxmlFile = switch (role) { // Determine FXML file based on role
            case "Admin" -> "/view/AdminDashboard.fxml";
            case "Student" -> "/view/StuDashboard.fxml";
            case "Faculty" -> "/view/FacultyDashboard.fxml";
            default -> "";
        };

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent dashboardPage = loader.load();

            if (role.equals("Student")) { // Pass email to Student Dashboard controller
                StuDashboardController controller = loader.getController();
                controller.setStudentEmail(email);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(dashboardPage));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading dashboard!");
        }
    }

    @FXML
    private void openSignup(ActionEvent event) throws IOException { // Open sign-up page
        Parent signupPage = FXMLLoader.load(getClass().getResource("/view/SignUp.fxml"));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(signupPage));
        window.centerOnScreen();
        window.show();
    }
}