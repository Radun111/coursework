package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddEnrollmentDialogController {

    @FXML private TextField txtStudentID;
    @FXML private TextField txtStudentName;
    @FXML private TextField txtCourseID;
    @FXML private TextField txtCourseTitle;
    @FXML private TextField txtEnrollmentDate;
    @FXML private Button btnSave;

    @FXML
    public void initialize() {
        btnSave.setOnAction(event -> saveEnrollment());
    }

    private void saveEnrollment() {
        String studentID = txtStudentID.getText().trim();
        String studentName = txtStudentName.getText().trim();
        String courseID = txtCourseID.getText().trim();
        String courseTitle = txtCourseTitle.getText().trim();
        String enrollmentDate = txtEnrollmentDate.getText().trim();

        // Validate fields
        if (studentID.isEmpty() || studentName.isEmpty() || courseID.isEmpty() || courseTitle.isEmpty() || enrollmentDate.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        // Insert into the database
        String query = "INSERT INTO enrollments (student_id, student_name, course_id, course_title, enrollment_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(studentID));
            stmt.setString(2, studentName);
            stmt.setInt(3, Integer.parseInt(courseID));
            stmt.setString(4, courseTitle);
            stmt.setString(5, enrollmentDate);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert("Success", "Enrollment added successfully.");
                clearForm();
            } else {
                showAlert("Error", "Failed to add enrollment.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error: " + e.getMessage());
        }
    }

    private void clearForm() {
        txtStudentID.clear();
        txtStudentName.clear();
        txtCourseID.clear();
        txtCourseTitle.clear();
        txtEnrollmentDate.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}