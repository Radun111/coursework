package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateEnrollmentDialogController {

    @FXML private TextField txtEnrollmentID;
    @FXML private TextField txtStudentID;
    @FXML private TextField txtStudentName;
    @FXML private TextField txtCourseID;
    @FXML private TextField txtCourseTitle;
    @FXML private TextField txtEnrollmentDate;
    @FXML private Button btnUpdate;

    private int enrollmentId;

    @FXML
    public void initialize() {
        btnUpdate.setOnAction(event -> updateEnrollment());
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
        txtEnrollmentID.setText(String.valueOf(enrollmentId));
    }

    private void updateEnrollment() {
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

        // Update the database
        String query = "UPDATE enrollments SET student_id = ?, student_name = ?, course_id = ?, course_title = ?, enrollment_date = ? WHERE enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(studentID));
            stmt.setString(2, studentName);
            stmt.setInt(3, Integer.parseInt(courseID));
            stmt.setString(4, courseTitle);
            stmt.setString(5, enrollmentDate);
            stmt.setInt(6, enrollmentId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert("Success", "Enrollment updated successfully.");
                closeWindow();
            } else {
                showAlert("Error", "Failed to update enrollment.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error: " + e.getMessage());
        }
    }

    private void closeWindow() {
        btnUpdate.getScene().getWindow().hide();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}