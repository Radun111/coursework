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

    @FXML private TableView<Course> tblCourses;
    @FXML private TableColumn<Course, Integer> colCourseID;
    @FXML private TableColumn<Course, String> colCourseName;
    @FXML private TableColumn<Course, Integer> colCredits;
    @FXML private TableColumn<Course, String> colInstructor;
    @FXML private TableColumn<Course, String> colDepartment;
    @FXML private TableColumn<Course, String> colPrerequisites;
    @FXML private TableColumn<Course, Integer> colMaxCapacity;

    @FXML private TextField searchField;
    @FXML private Button registerButton;

    private ObservableList<Course> courseList;
    private String studentEmail;

    @FXML
    public void initialize() {
        colCourseID.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colCredits.setCellValueFactory(new PropertyValueFactory<>("creditHours"));
        colInstructor.setCellValueFactory(new PropertyValueFactory<>("instructor"));
        colDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        colPrerequisites.setCellValueFactory(new PropertyValueFactory<>("prerequisites"));
        colMaxCapacity.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));

        loadCourseData();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch(newValue));
    }

    public void setStudentEmail(String email) {
        this.studentEmail = email;
        System.out.println("Student email set: " + email);
    }

    private void loadCourseData() {
        courseList = FXCollections.observableArrayList(CourseDAO.getAllCourses());
        if (courseList.isEmpty()) {
            System.out.println("No courses found in the database.");
        }
        tblCourses.setItems(courseList);
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        Course selectedCourse = tblCourses.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            if (studentEmail != null && !studentEmail.isEmpty()) {
                navigateTo(event, "/view/StuRegisterCourses2.fxml", "Register Courses", selectedCourse, studentEmail);
            } else {
                showAlert("Error", "Student details not found. Please log in again.");
            }
        } else {
            showAlert("No Course Selected", "Please select a course before registering.");
        }
    }

    private void navigateTo(ActionEvent event, String fxmlPath, String title, Course selectedCourse, String studentEmail) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            StuRegisterCourses2Controller controller = loader.getController();
            controller.setSelectedCourse(selectedCourse);
            controller.setStudentEmail(studentEmail);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Could not load the next page.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        System.out.println("Search button clicked!");
    }

    private void handleSearch(String keyword) {
        if (keyword.isEmpty()) {
            tblCourses.setItems(courseList);
            return;
        }

        ObservableList<Course> filteredList = courseList.stream()
                .filter(course -> course.getCourseName().toLowerCase().contains(keyword.toLowerCase()) ||
                                  course.getDepartment().toLowerCase().contains(keyword.toLowerCase()) ||
                                  course.getInstructor().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        tblCourses.setItems(filteredList);
    }

    @FXML
    private void handleHomeButtonAction(ActionEvent event) {
        navigateTo(event, "/view/StuDashboard.fxml", "Student Dashboard");
    }

    @FXML
    private void handleProfileButtonAction(ActionEvent event) {
        
    }

    @FXML
    private void handleViewCoursesButtonAction(ActionEvent event) {
        navigateTo(event, "/view/StuViewCourses.fxml", "View Courses");
    }

    @FXML
    private void handleRegisterCoursesButtonAction(ActionEvent event) {
        navigateTo(event, "/view/StuRegisterCourses1.fxml", "Register Courses");
    }

    @FXML
    private void handleViewAcademicRecordsButtonAction(ActionEvent event) {
        navigateTo(event, "/view/StuViewAcadamicRecords.fxml", "View Academic Records");
    }

    @FXML
    private void handleLogoutButtonAction(ActionEvent event) {
        navigateTo(event, "/view/Login.fxml", "Login");
    }

    private void navigateTo(ActionEvent event, String fxmlPath, String title) {
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
            stage.setTitle(title);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Could not load " + fxmlPath);
        }
    }
} 