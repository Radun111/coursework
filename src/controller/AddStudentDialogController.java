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

public class AddStudentDialogController {

    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhoneNumber;
    @FXML private TextField txtPassword;
    @FXML private TextField txtDOB;
    @FXML private TextField txtAddress;
    @FXML private Button btnSave;

    @FXML
    public void initialize() {
        btnSave.setOnAction(event -> saveStudent());
    }

    private void saveStudent() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phoneNumber = txtPhoneNumber.getText().trim();
        String password = txtPassword.getText().trim();
        String dob = txtDOB.getText().trim();
        String address = txtAddress.getText().trim();

        // Validate fields
        if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || dob.isEmpty() || address.isEmpty()) {
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

        // Insert into the database
        String query = "INSERT INTO students (name, email, phone_number, password, date_of_birth, address) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phoneNumber);
            stmt.setString(4, password);
            stmt.setDate(5, java.sql.Date.valueOf(dateOfBirth));
            stmt.setString(6, address);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert("Success", "Student added successfully.");
                clearForm();
            } else {
                showAlert("Error", "Failed to add student.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error: " + e.getMessage());
        }
    }

    private void clearForm() {
        txtName.clear();
        txtEmail.clear();
        txtPhoneNumber.clear();
        txtPassword.clear();
        txtDOB.clear();
        txtAddress.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}