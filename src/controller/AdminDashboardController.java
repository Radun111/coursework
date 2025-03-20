package controller;


import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import db.DBConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    @FXML private ListView<String> coursesListView; // ListView to display courses
    @FXML private ListView<String> studentsListView; // ListView to display students

    @FXML private Button homeButton;
    @FXML private Button manageCoursesButton;
    @FXML private Button manageStudentsButton;
    @FXML private Button enrollmentManagementButton;
    @FXML private Button academicRecordsButton;
    @FXML private Button reportsAnalyticsButton;
    @FXML private Button logoutButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCourses(); // Load courses into the ListView
        loadStudents(); // Load students into the ListView

        // Set button actions
        homeButton.setOnAction(event -> navigateTo(event, "/view/AdminDashboard.fxml", "Admin Dashboard"));
        manageCoursesButton.setOnAction(event -> navigateTo(event, "/view/AdminManageCourse.fxml", "Manage Courses"));
        manageStudentsButton.setOnAction(event -> navigateTo(event, "/view/AdminManageStudents.fxml", "Manage Students"));
        enrollmentManagementButton.setOnAction(event -> navigateTo(event, "/view/AdminEnrollmentManagement.fxml", "Enrollment Management"));
        academicRecordsButton.setOnAction(event -> navigateTo(event, "/view/AdminAcadamicRecords.fxml", "Academic Records"));
        reportsAnalyticsButton.setOnAction(event -> navigateTo(event, "/view/AdminReportsAnalytics.fxml", "Reports & Analytics"));
        logoutButton.setOnAction(event -> navigateTo(event, "/view/Login.fxml", "Login"));
    }

    private void loadCourses() { // Load all courses from the database
        String query = "SELECT course_id, course_name FROM courses";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String courseId = rs.getString("course_id");
                String courseName = rs.getString("course_name");
                coursesListView.getItems().add(courseId + " - " + courseName); // Add course to ListView
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error loading courses: " + e.getMessage());
        }
    }

    private void loadStudents() { // Load all students from the database
        String query = "SELECT student_id, name FROM students";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String studentId = rs.getString("student_id");
                String studentName = rs.getString("name");
                studentsListView.getItems().add(studentId + " - " + studentName); // Add student to ListView
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error loading students: " + e.getMessage());
        }
    }

    private void navigateTo(javafx.event.ActionEvent event, String fxmlPath, String title) { // Navigate to a specific page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

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