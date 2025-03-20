package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.StudentAcademicRecord;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import db.DBConnection;

public class StuAcadamicRecordsController implements Initializable {

    @FXML private TableView<StudentAcademicRecord> academicRecordsTable; // Table to display academic records
    @FXML private TableColumn<StudentAcademicRecord, String> colCourseID; // Column for course ID
    @FXML private TableColumn<StudentAcademicRecord, String> colCourseName; // Column for course name
    @FXML private TableColumn<StudentAcademicRecord, Integer> colCredits; // Column for credit hours
    @FXML private TableColumn<StudentAcademicRecord, String> colGrade; // Column for grade
    @FXML private TableColumn<StudentAcademicRecord, String> colStatus; // Column for status
    @FXML private TableColumn<StudentAcademicRecord, String> colTerm; // Column for term
    @FXML private TableColumn<StudentAcademicRecord, Integer> colYear; // Column for year

    private String studentEmail; // Stores the logged-in student's email

    @Override
    public void initialize(URL location, ResourceBundle resources) { // Initialize table columns
        colCourseID.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));
        colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colTerm.setCellValueFactory(new PropertyValueFactory<>("term"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
    }

    public void setStudentEmail(String email) { // Set the student's email
        this.studentEmail = email;
        loadAcademicRecords(); // Load academic records when email is set
    }

    private void loadAcademicRecords() { // Load academic records from the database
        if (studentEmail == null || studentEmail.isEmpty()) {
            System.out.println("Student email is not set.");
            return;
        }

        String query = "SELECT c.course_id, c.course_name, c.credit_hours, sar.grade, sar.status, sar.term, sar.year " +
                       "FROM student_academic_records sar " +
                       "JOIN courses c ON sar.course_id = c.course_id " +
                       "JOIN students s ON sar.student_id = s.student_id " +
                       "WHERE s.email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, studentEmail);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) { // If records are found
                String courseID = rs.getString("course_id");
                String courseName = rs.getString("course_name");
                int credits = rs.getInt("credit_hours");
                String grade = rs.getString("grade");
                String status = rs.getString("status");
                String term = rs.getString("term");
                int year = rs.getInt("year");

                academicRecordsTable.getItems().add(new StudentAcademicRecord(courseID, courseName, credits, grade, status, term, year)); // Add record to table
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error loading academic records: " + e.getMessage()); // Handle database error
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
    private void handleViewAcademicRecordsButtonAction(ActionEvent event) { // Already on the academic records page
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
}