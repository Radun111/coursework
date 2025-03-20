package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class StuDashboardController {
    @FXML private Label welcomeLabel; // Label to display welcome message
    @FXML private Label studentNameLabel; // Label to display student name
    @FXML private Label studentIdLabel; // Label to display student ID
    @FXML private Label studentEmailLabel; // Label to display student email

    private String studentEmail; // Stores the logged-in student's email

    public void setStudentEmail(String email) { // Set the student's email
        this.studentEmail = email;
        loadStudentData(); // Load student data when email is set
    }

    private void loadStudentData() { // Load student details from the database
        if (studentEmail == null || studentEmail.isEmpty()) {
            welcomeLabel.setText("Welcome, Student!");
            return;
        }

        String query = "SELECT student_id, name FROM students WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, studentEmail);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) { // If student data is found
                String studentId = rs.getString("student_id");
                String studentName = rs.getString("name");

                studentIdLabel.setText("ID: " + studentId); // Display student ID
                studentNameLabel.setText("Name: " + studentName); // Display student name
                studentEmailLabel.setText("Email: " + studentEmail); // Display student email
                welcomeLabel.setText("Welcome, " + studentName + "!"); // Display welcome message
            } else {
                welcomeLabel.setText("Student details not found."); // Handle missing data
            }
        } catch (SQLException e) {
            e.printStackTrace();
            welcomeLabel.setText("Error loading student details."); // Handle database error
        }
    }

    @FXML
    private void handleHomeButtonAction(ActionEvent event) { // Navigate to student dashboard
        navigateTo(event, "/view/StuDashboard.fxml", "Student Dashboard");
    }

    @FXML
    private void handleProfileButtonAction(ActionEvent event) { // Navigate to student profile
        navigateTo(event, "/view/StuProfile.fxml", "Student Profile");
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
            if (fxmlPath.equals("/view/StuProfile.fxml")) {
                StuProfileController profileController = loader.getController();
                profileController.setStudentEmail(studentEmail);
            } else if (fxmlPath.equals("/view/StuRegisterCourses1.fxml")) {
                StuRegisterCourses1Controller registerCoursesController = loader.getController();
                registerCoursesController.setStudentEmail(studentEmail);
            } else if (fxmlPath.equals("/view/StuViewCourses.fxml")) {
                StuViewCoursesController viewCoursesController = loader.getController();
                viewCoursesController.setStudentEmail(studentEmail);
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
}