package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.Course;
import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddCourseDialogController {

    @FXML private TextField txtCourseName;
    @FXML private TextField txtCredits;
    @FXML private TextField txtInstructor;
    @FXML private TextField txtDepartment;
    @FXML private TextField txtPrerequisites;
    @FXML private TextField txtMaxCapacity;

    @FXML
private void saveCourse() {
    String courseName = txtCourseName.getText();
    int credits = Integer.parseInt(txtCredits.getText());
    String instructor = txtInstructor.getText();
    String department = txtDepartment.getText();
    String prerequisites = txtPrerequisites.getText();
    int maxCapacity = Integer.parseInt(txtMaxCapacity.getText());

    String query = "INSERT INTO courses (course_name, credit_hours, instructor, department, prerequisites, max_capacity) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, courseName);
        stmt.setInt(2, credits);
        stmt.setString(3, instructor);
        stmt.setString(4, department);
        stmt.setString(5, prerequisites);
        stmt.setInt(6, maxCapacity);

        int rowsInserted = stmt.executeUpdate();
        if (rowsInserted > 0) {
            showAlert("Success", "Course added successfully.");
        } else {
            showAlert("Error", "Failed to add course.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert("Error", "Database error: " + e.getMessage());
    }
}
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}