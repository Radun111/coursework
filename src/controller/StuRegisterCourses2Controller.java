package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Course;
import db.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class StuRegisterCourses2Controller {

    @FXML private Label studentIdLabel; // Label to display student ID
    @FXML private Label nameLabel; // Label to display student name
    @FXML private Label dobLabel; // Label to display student date of birth
    @FXML private Label addressLabel; // Label to display student address
    @FXML private Label emailLabel; // Label to display student email
    @FXML private Label phoneLabel; // Label to display student phone number
    @FXML private Label courseIdLabel; // Label to display course ID
    @FXML private Label courseNameLabel; // Label to display course name

    @FXML private CheckBox olMathCheckBox; // Checkbox for OL Math passed
    @FXML private CheckBox olEnglishCheckBox; // Checkbox for OL English passed
    @FXML private CheckBox alPassCheckBox; // Checkbox for AL minimum C passes
    @FXML private TextField additionalAcademicField; // Text field for additional academic info

    private Course selectedCourse; // Stores the selected course
    private String studentEmail; // Stores the logged-in student's email

    public void setSelectedCourse(Course course) { // Set the selected course
        this.selectedCourse = course;
        if (course != null) {
            courseIdLabel.setText(String.valueOf(course.getCourseId())); // Display course ID
            courseNameLabel.setText(course.getCourseName()); // Display course name
        }
    }

    public void setStudentEmail(String email) { // Set the student's email
        this.studentEmail = email;
        if (email != null && !email.isEmpty()) {
            loadStudentDetails(email); // Load student details
        }
    }

    private void loadStudentDetails(String email) { // Load student details from the database
        String query = "SELECT student_id, name, date_of_birth, address, phone_number FROM students WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) { // If student data is found
                studentIdLabel.setText(String.valueOf(rs.getInt("student_id"))); // Display student ID
                nameLabel.setText(rs.getString("name")); // Display student name
                dobLabel.setText(rs.getDate("date_of_birth").toString()); // Display student DOB
                addressLabel.setText(rs.getString("address")); // Display student address
                phoneLabel.setText(rs.getString("phone_number")); // Display student phone number
                emailLabel.setText(email); // Display student email
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load student details."); // Handle database error
        }
    }

    @FXML
    private void handleRegister() { // Handle course registration submission
        if (selectedCourse == null || studentEmail == null || studentEmail.isEmpty()) {
            showAlert("Error", "Course or student details are missing."); // Handle missing data
            return;
        }

        if (isAlreadyRegistered(selectedCourse.getCourseId())) { // Check if already registered
            showAlert("Error", "You have already registered for this course.");
            return;
        }

        // Get academic information
        boolean olMathPassed = olMathCheckBox.isSelected();
        boolean olEnglishPassed = olEnglishCheckBox.isSelected();
        boolean alPassed = alPassCheckBox.isSelected();
        String additionalInfo = additionalAcademicField.getText();

        // Save registration to the database
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO course_registrations (student_id, full_name, dob, address, email, phone, course_id, course_name, ol_math_passed, ol_english_passed, al_minimum_c_passes, additional_academic_info, registration_date, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            String query = "SELECT student_id, name, date_of_birth, address, phone_number FROM students WHERE email = ?";
            try (PreparedStatement studentStmt = conn.prepareStatement(query)) {
                studentStmt.setString(1, studentEmail);
                ResultSet rs = studentStmt.executeQuery();

                if (rs.next()) { // If student data is found
                    int studentId = rs.getInt("student_id");
                    String name = rs.getString("name");
                    String dob = rs.getDate("date_of_birth").toString();
                    String address = rs.getString("address");
                    String phone = rs.getString("phone_number");

                    stmt.setInt(1, studentId);
                    stmt.setString(2, name);
                    stmt.setDate(3, java.sql.Date.valueOf(dob));
                    stmt.setString(4, address);
                    stmt.setString(5, studentEmail);
                    stmt.setString(6, phone);
                    stmt.setInt(7, selectedCourse.getCourseId());
                    stmt.setString(8, selectedCourse.getCourseName());
                    stmt.setBoolean(9, olMathPassed);
                    stmt.setBoolean(10, olEnglishPassed);
                    stmt.setBoolean(11, alPassed);
                    stmt.setString(12, additionalInfo);
                    stmt.setTimestamp(13, new Timestamp(System.currentTimeMillis()));
                    stmt.setString(14, "Pending");

                    int rowsInserted = stmt.executeUpdate();
                    if (rowsInserted > 0) { // If registration is successful
                        showAlert("Success", "Course registration submitted successfully!");
                        clearForm(); // Clear the form
                    } else {
                        showAlert("Error", "Failed to register for the course."); // Handle registration failure
                    }
                } else {
                    showAlert("Error", "Student details not found."); // Handle missing student data
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while saving the registration."); // Handle database error
        }
    }

    private boolean isAlreadyRegistered(int courseId) { // Check if the student is already registered for the course
        String query = "SELECT * FROM course_registrations WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(studentIdLabel.getText()));
            stmt.setInt(2, courseId);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // If a record exists, the student is already registered
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to check registration status."); // Handle database error
            return false;
        }
    }

    @FXML
    private void handleCancel() { // Handle cancel button click
        clearForm(); // Clear the form
    }

    private void clearForm() { // Clear form fields
        olMathCheckBox.setSelected(false);
        olEnglishCheckBox.setSelected(false);
        alPassCheckBox.setSelected(false);
        additionalAcademicField.clear();
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

    private void showAlert(String title, String message) { // Show alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}