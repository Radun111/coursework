package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.Student;
import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UpdateStudentDialogController {

    @FXML private TextField txtName;
    @FXML private TextField txtDOB;
    @FXML private TextField txtAddress;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhoneNumber;
    @FXML private Button btnUpdate;

    private Student selectedStudent;

    @FXML
    public void initialize() {
        btnUpdate.setOnAction(event -> updateStudent());
    }

    public void setSelectedStudent(Student student) {
        this.selectedStudent = student;
        populateFields();
    }

    private void populateFields() {
        if (selectedStudent != null) {
            txtName.setText(selectedStudent.getName());
            txtDOB.setText(selectedStudent.getDateOfBirth().toString());
            txtAddress.setText(selectedStudent.getAddress());
            txtEmail.setText(selectedStudent.getEmail());
            txtPhoneNumber.setText(selectedStudent.getPhoneNumber());
        }
    }

    private void updateStudent() {
        String name = txtName.getText().trim();
        String dob = txtDOB.getText().trim();
        String address = txtAddress.getText().trim();
        String email = txtEmail.getText().trim();
        String phoneNumber = txtPhoneNumber.getText().trim();

        // Validate fields
        if (name.isEmpty() || dob.isEmpty() || address.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        // Validate date of birth
        LocalDate dateOfBirth;
        try {
            dateOfBirth = LocalDate.parse(dob, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            showAlert("Error", "Invalid date format. Please use yyyy-MM-dd.");
            return;
        }

        // Update the database
        String query = "UPDATE students SET name = ?, date_of_birth = ?, address = ?, email = ?, phone_number = ? WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setDate(2, java.sql.Date.valueOf(dateOfBirth));
            stmt.setString(3, address);
            stmt.setString(4, email);
            stmt.setString(5, phoneNumber);
            stmt.setInt(6, selectedStudent.getStudentId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert("Success", "Student updated successfully.");
                closeWindow();
            } else {
                showAlert("Error", "Failed to update student.");
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