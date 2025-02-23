package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import db.DBConnection; 

public class LoginController {

    @FXML private TextField emailField; 
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private RadioButton studentRadio;
    @FXML private RadioButton adminRadio;
    @FXML private RadioButton facultyRadio;

    // Reset fields when "Reset" button is clicked
    @FXML
    private void reset() {
        emailField.clear();
        passwordField.clear();
        errorLabel.setText(""); 
        errorLabel.setVisible(false);
    }

    // Handle login button click
    @FXML
    private void login() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Email or password cannot be empty!");
            errorLabel.setVisible(true);
            return;
        }

        String role = getSelectedRole();
        if (role.isEmpty()) {
            errorLabel.setText("Please select a role!");
            errorLabel.setVisible(true);
            return;
        }

        // Validate credentials based on role
        if (validateLogin(email, password, role)) {
            System.out.println("Login successful as " + role);
            errorLabel.setVisible(false);
            loadDashboard(role);
        } else {
            errorLabel.setText("Invalid email or password!");
            errorLabel.setVisible(true);
        }
    }

    // Determine selected role
    private String getSelectedRole() {
        if (studentRadio.isSelected()) {
            return "Student";
        } else if (adminRadio.isSelected()) {
            return "Admin";
        } else if (facultyRadio.isSelected()) {
            return "Faculty";
        }
        return "";
    }

    // Validate email and password from the correct database table
    private boolean validateLogin(String email, String password, String role) {
        boolean isValid = false;
        String table = "";

        // Select the correct table based on role
        switch (role) {
            case "Student":
                table = "students";
                break;
            case "Admin":
                table = "admins";
                break;
            case "Faculty":
                table = "faculty";
                break;
        }

        String query = "SELECT * FROM " + table + " WHERE email = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                isValid = true; 
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Database error!");
            errorLabel.setVisible(true);
        }
        return isValid;
    }

    // Load dashboard based on role selection
    private void loadDashboard(String role) {
        String fxmlFile = "";
        switch (role) {
            case "Admin": 
                fxmlFile = "/view/AdminDashboard.fxml"; 
                break;
            case "Student": 
                fxmlFile = "/view/StuDashboard.fxml"; 
                break;
            case "Faculty": 
                fxmlFile = "/view/FacultyDashboard.fxml"; 
                break;
        }

        try {
            Parent dashboardPage = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(dashboardPage));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error loading dashboard!");
            errorLabel.setVisible(true);
        }
    }

    // Navigate to Sign-Up page
    @FXML
    private void openSignup(ActionEvent event) throws IOException {
        Parent signupPage = FXMLLoader.load(getClass().getResource("/view/SignUp.fxml"));
        Scene signupScene = new Scene(signupPage);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(signupScene);
        window.show();
    }
}
