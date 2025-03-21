package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Course;
import db.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminManageCourseController implements Initializable {

    @FXML private TableView<Course> coursesTable; // Table to display courses
    @FXML private TableColumn<Course, Integer> colCourseID; // Column for course ID
    @FXML private TableColumn<Course, String> colCourseName; // Column for course name
    @FXML private TableColumn<Course, Integer> colCredits; // Column for credits
    @FXML private TableColumn<Course, String> colInstructor; // Column for instructor
    @FXML private TableColumn<Course, String> colDepartment; // Column for department
    @FXML private TableColumn<Course, String> colPrerequisites; // Column for prerequisites
    @FXML private TableColumn<Course, Integer> colMaxCapacity; // Column for max capacity

    @FXML private TextField searchField; // Search field for filtering courses
    @FXML private Button btnAddCourse; // Button to add a new course
    @FXML private Button btnUpdateCourse; // Button to update a course
    @FXML private Button btnDeleteCourse; // Button to delete a course
    @FXML private Button btnReset; // Button to reset the form

    @FXML private Button homeButton;
    @FXML private Button manageCoursesButton;
    @FXML private Button manageStudentsButton;
    @FXML private Button enrollmentManagementButton;
    @FXML private Button academicRecordsButton;
    @FXML private Button reportsAnalyticsButton;
    @FXML private Button logoutButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind table columns to Course model properties
        colCourseID.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colCredits.setCellValueFactory(new PropertyValueFactory<>("creditHours"));
        colInstructor.setCellValueFactory(new PropertyValueFactory<>("instructor"));
        colDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        colPrerequisites.setCellValueFactory(new PropertyValueFactory<>("prerequisites"));
        colMaxCapacity.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));

        loadCourses(); // Load courses into the table

        // Set button actions
        btnAddCourse.setOnAction(event -> addCourse());
        btnUpdateCourse.setOnAction(event -> updateCourse());
        btnDeleteCourse.setOnAction(event -> deleteCourse());
        btnReset.setOnAction(event -> resetForm());

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
        String query = "SELECT * FROM courses";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int courseId = rs.getInt("course_id");
                String courseName = rs.getString("course_name");
                int creditHours = rs.getInt("credit_hours");
                String instructor = rs.getString("instructor");
                String department = rs.getString("department");
                String prerequisites = rs.getString("prerequisites");
                int maxCapacity = rs.getInt("max_capacity");

                coursesTable.getItems().add(new Course(courseId, courseName, creditHours, instructor, department, prerequisites, maxCapacity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load courses: " + e.getMessage());
        }
    }

    private void addCourse() { // Add a new course
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddCourseDialog.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Course");
            stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
            stage.showAndWait();

            // Reload courses after adding
            coursesTable.getItems().clear();
            loadCourses();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load the add course dialog.");
        }
    }

    private void updateCourse() { // Update a selected course
        Course selectedCourse = coursesTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateCourseDialog.fxml"));
                Parent root = loader.load();

                UpdateCourseDialogController controller = loader.getController();
                controller.setSelectedCourse(selectedCourse);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Update Course");
                stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
                stage.showAndWait();

                // Reload courses after updating
                coursesTable.getItems().clear();
                loadCourses();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Could not load the update course dialog.");
            }
        } else {
            showAlert("Error", "No course selected.");
        }
    }

    private void deleteCourse() { // Delete a selected course
        Course selectedCourse = coursesTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            String query = "DELETE FROM courses WHERE course_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, selectedCourse.getCourseId());
                int rowsDeleted = stmt.executeUpdate();
                if (rowsDeleted > 0) {
                    coursesTable.getItems().remove(selectedCourse); // Remove from table
                    showAlert("Success", "Course deleted successfully.");
                } else {
                    showAlert("Error", "Failed to delete course.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Database error: " + e.getMessage());
            }
        } else {
            showAlert("Error", "No course selected.");
        }
    }

    private void resetForm() { // Reset the form and reload courses
        coursesTable.getItems().clear();
        loadCourses();
    }

    private void showAlert(String title, String message) { // Show alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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