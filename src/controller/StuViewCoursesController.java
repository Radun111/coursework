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
import javafx.scene.control.cell.PropertyValueFactory; // For binding table columns to data

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import db.DBConnection;
import models.CourseDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Course;

public class StuViewCoursesController implements Initializable {

    @FXML private TableView<Course> tblCourses; // Table to display courses
    @FXML private TableColumn<Course, Integer> colCourseID; // Column for course ID
    @FXML private TableColumn<Course, String> colCourseName; // Column for course name
    @FXML private TableColumn<Course, Integer> colCredits; // Column for credit hours
    @FXML private TableColumn<Course, String> colInstructor; // Column for instructor
    @FXML private TableColumn<Course, String> colDepartment; // Column for department
    @FXML private TableColumn<Course, String> colPrerequisites; // Column for prerequisites
    @FXML private TableColumn<Course, Integer> colMaxCapacity; // Column for max capacity

    @FXML private TextField searchField; // Search bar for filtering courses

    private ObservableList<Course> courseList; // List of courses to display
    private String studentEmail; // Stores the logged-in student's email

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

        loadCourseData(); // Load course data into the table

        // Add listener to search field for real-time filtering
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterCourses(newValue));
    }

    private void loadCourseData() { // Load all courses from the database
        courseList = FXCollections.observableArrayList(CourseDAO.getAllCourses());
        if (courseList.isEmpty()) {
            System.out.println("No courses found in the database.");
        }
        tblCourses.setItems(courseList); // Set table data
    }

    private void filterCourses(String keyword) { // Filter courses based on search keyword
        if (keyword.isEmpty()) {
            tblCourses.setItems(courseList); // Reset to full list if keyword is empty
            return;
        }

        // Filter courses by name, department, or instructor
        ObservableList<Course> filteredList = courseList.stream()
                .filter(course -> course.getCourseName().toLowerCase().contains(keyword.toLowerCase()) ||
                                  course.getDepartment().toLowerCase().contains(keyword.toLowerCase()) ||
                                  course.getInstructor().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        tblCourses.setItems(filteredList); // Update table with filtered results
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
    private void handleViewCoursesButtonAction(ActionEvent event) { // Already on the view courses page
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
            } else if (fxmlPath.equals("/view/StuProfile.fxml")) {
                StuProfileController profileController = loader.getController();
                profileController.setStudentEmail(studentEmail);
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

    public void setStudentEmail(String email) { // Set the student's email
        this.studentEmail = email;
        System.out.println("Student email set: " + email);
    }
}