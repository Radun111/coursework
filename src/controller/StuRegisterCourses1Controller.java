package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import models.Course;
import models.CourseDAO;

import java.io.IOException;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StuRegisterCourses1Controller {

    @FXML private TableView<Course> tblCourses; // Table to display available courses
    @FXML private TableColumn<Course, Integer> colCourseID; // Column for course ID
    @FXML private TableColumn<Course, String> colCourseName; // Column for course name
    @FXML private TableColumn<Course, Integer> colCredits; // Column for credit hours
    @FXML private TableColumn<Course, String> colInstructor; // Column for instructor
    @FXML private TableColumn<Course, String> colDepartment; // Column for department
    @FXML private TableColumn<Course, String> colPrerequisites; // Column for prerequisites
    @FXML private TableColumn<Course, Integer> colMaxCapacity; // Column for max capacity

    @FXML private TextField searchField; // Search bar for filtering courses
    @FXML private Button registerButton; // Button to register for a selected course

    private ObservableList<Course> courseList; // List of courses to display
    private String studentEmail; // Stores the logged-in student's email

    @FXML
    public void initialize() { // Initialize table columns and load course data
        colCourseID.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colCredits.setCellValueFactory(new PropertyValueFactory<>("creditHours"));
        colInstructor.setCellValueFactory(new PropertyValueFactory<>("instructor"));
        colDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        colPrerequisites.setCellValueFactory(new PropertyValueFactory<>("prerequisites"));
        colMaxCapacity.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));

        loadCourseData(); // Load course data into the table
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch(newValue)); // Add search functionality
    }

    public void setStudentEmail(String email) { // Set the student's email
        this.studentEmail = email;
        System.out.println("Student email set: " + email);
    }

    private void loadCourseData() { // Load all courses from the database
        courseList = FXCollections.observableArrayList(CourseDAO.getAllCourses());
        if (courseList.isEmpty()) {
            System.out.println("No courses found in the database.");
        }
        tblCourses.setItems(courseList); // Set table data
    }

    @FXML
    private void handleRegister(ActionEvent event) { // Handle course registration
        Course selectedCourse = tblCourses.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            if (studentEmail != null && !studentEmail.isEmpty()) {
                navigateTo(event, "/view/StuRegisterCourses2.fxml", "Register Courses", selectedCourse, studentEmail); // Navigate to registration confirmation
            } else {
                showAlert("Error", "Student details not found. Please log in again."); // Handle missing student email
            }
        } else {
            showAlert("No Course Selected", "Please select a course before registering."); // Handle no course selection
        }
    }

    private void navigateTo(ActionEvent event, String fxmlPath, String title, Course selectedCourse, String studentEmail) { // Navigate to a specific page with data
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            StuRegisterCourses2Controller controller = loader.getController();
            controller.setSelectedCourse(selectedCourse); // Pass selected course to the next controller
            controller.setStudentEmail(studentEmail); // Pass student email to the next controller

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title); // Set window title
            stage.centerOnScreen(); // Center the window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Could not load the next page."); // Handle navigation error
        }
    }

    private void showAlert(String title, String message) { // Show alert dialog
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleSearch(ActionEvent event) { // Handle search button click
        System.out.println("Search button clicked!");
    }

    private void handleSearch(String keyword) { // Filter courses based on search keyword
        if (keyword.isEmpty()) {
            tblCourses.setItems(courseList); // Reset to full list if keyword is empty
            return;
        }

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
    private void handleProfileButtonAction(ActionEvent event) { // Already on the profile page
    }

    @FXML
    private void handleViewCoursesButtonAction(ActionEvent event) { // Navigate to view courses
        navigateTo(event, "/view/StuViewCourses.fxml", "View Courses");
    }

    @FXML
    private void handleRegisterCoursesButtonAction(ActionEvent event) { // Already on the register courses page
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
}