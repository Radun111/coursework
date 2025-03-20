package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import db.DBConnection;

public class StuProfileController implements Initializable {

    @FXML private TextField txtStudentID; // Input field for student ID
    @FXML private TextField txtStudentName; // Input field for student name
    @FXML private TextField txtStudentDOB; // Input field for student date of birth
    @FXML private TextField txtStudentEmail; // Input field for student email
    @FXML private TextField txtStudentPhone; // Input field for student phone number
    @FXML private TextField txtStudentAddress; // Input field for student address
    @FXML private PasswordField txtNewPassword; // Input field for new password
    @FXML private PasswordField txtConfirmPassword; // Input field for confirming new password

    private String studentEmail; // Stores the logged-in student's email

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Called when the FXML is loaded
    }

    public void setStudentEmail(String email) { // Set the student's email
        this.studentEmail = email;
        loadStudentData(); // Load student data when email is set
    }

    private void loadStudentData() { // Load student details from the database
        if (studentEmail == null || studentEmail.isEmpty()) {
            System.out.println("Student email is not set.");
            return;
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM students WHERE email = ?")) {

            statement.setString(1, studentEmail);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // If student data is found
                txtStudentID.setText(resultSet.getString("student_id")); // Display student ID
                txtStudentName.setText(resultSet.getString("name")); // Display student name
                txtStudentEmail.setText(resultSet.getString("email")); // Display student email
                txtStudentPhone.setText(resultSet.getString("phone_number")); // Display student phone number
                txtStudentDOB.setText(resultSet.getString("date_of_birth")); // Display student date of birth
                txtStudentAddress.setText(resultSet.getString("address")); // Display student address
            } else {
                System.out.println("No student found with email: " + studentEmail); // Handle missing data
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle database error
        }
    }

    @FXML
    private void updatePassword() { // Handle password update
        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) { // Validate input fields
            showAlert(Alert.AlertType.ERROR, "Error", "Password fields cannot be empty.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) { // Validate password match
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.");
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE students SET password = ? WHERE email = ?")) {

            stmt.setString(1, newPassword);
            stmt.setString(2, studentEmail);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) { // If password update is successful
                showAlert(Alert.AlertType.INFORMATION, "Success", "Password updated successfully!");
                txtNewPassword.clear();
                txtConfirmPassword.clear();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Password update failed."); // Handle update failure
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Database error."); // Handle database error
        }
    }

    @FXML
    private void handleCancelButtonAction() { // Handle cancel button click
        txtNewPassword.clear();
        txtConfirmPassword.clear();
        System.out.println("Cancel button clicked. Password fields cleared.");
    }

    @FXML
    private void handleHomeButtonAction(ActionEvent event) { // Navigate to student dashboard
        navigateTo(event, "/view/StuDashboard.fxml", "Student Dashboard");
    }

    @FXML
    private void handleProfileButtonAction(ActionEvent event) { // Already on the profile page
    }

    @FXML
    private void handleViewCoursesButtonAction(ActionEvent event) { // Navigate to view courses
        navigateTo(event, "/view/StuViewCourses.fxml", "View Courses");
    }

    @FXML
    private void handleRegisterCoursesButtonAction(ActionEvent event) { // Navigate to register courses
        navigateTo(event, "/view/StuRegisterCourses1.fxml", "Register Courses");
    }

    @FXML
    private void handleViewAcademicRecordsButtonAction(ActionEvent event) { // Navigate to academic records
        navigateTo(event, "/view/StuViewAcadamicRecords.fxml", "View Academic Records");
    }

    @FXML
    private void handleLogoutButtonAction(ActionEvent event) { // Logout and return to login page
        navigateTo(event, "/view/Login.fxml", "Login");
    }

    private void navigateTo(ActionEvent event, String fxmlPath, String title) { // Navigate to a specific page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Pass student email to the next controller if needed
            if (fxmlPath.equals("/view/StuDashboard.fxml")) {
                StuDashboardController dashboardController = loader.getController();
                dashboardController.setStudentEmail(studentEmail);
            } else if (fxmlPath.equals("/view/StuViewCourses.fxml")) {
                StuViewCoursesController viewCoursesController = loader.getController();
                viewCoursesController.setStudentEmail(studentEmail);
            } else if (fxmlPath.equals("/view/StuRegisterCourses1.fxml")) {
                StuRegisterCourses1Controller registerCoursesController = loader.getController();
                registerCoursesController.setStudentEmail(studentEmail);
            } else if (fxmlPath.equals("/view/StuViewAcadamicRecords.fxml")) {
                StuAcadamicRecordsController academicRecordsController = loader.getController();
                academicRecordsController.setStudentEmail(studentEmail);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title); // Set window title
            stage.centerOnScreen(); // Center the window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Could not load " + fxmlPath); // Handle navigation error
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) { // Show alert dialog
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}