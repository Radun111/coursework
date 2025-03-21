package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Student;
import db.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AdminManageStudentsController implements Initializable {

    @FXML private TableView<Student> studentsTable;
    @FXML private TableColumn<Student, Integer> colStudentID;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colEmail;
    @FXML private TableColumn<Student, String> colPhoneNumber;
    @FXML private TableColumn<Student, String> colPassword;
    @FXML private TableColumn<Student, LocalDate> colDOB;
    @FXML private TableColumn<Student, String> colAddress;

    @FXML private Button homeButton;
    @FXML private Button manageCoursesButton;
    @FXML private Button manageStudentsButton;
    @FXML private Button enrollmentManagementButton;
    @FXML private Button academicRecordsButton;
    @FXML private Button reportsAnalyticsButton;
    @FXML private Button logoutButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind table columns to Student model properties
        colStudentID.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        loadStudents(); // Load students into the table

        homeButton.setOnAction(event -> navigateTo(event, "/view/AdminDashboard.fxml", "Admin Dashboard"));
        manageCoursesButton.setOnAction(event -> navigateTo(event, "/view/AdminManageCourse.fxml", "Manage Courses"));
        manageStudentsButton.setOnAction(event -> navigateTo(event, "/view/AdminManageStudents.fxml", "Manage Students"));
        enrollmentManagementButton.setOnAction(event -> navigateTo(event, "/view/AdminEnrollmentManagement.fxml", "Enrollment Management"));
        academicRecordsButton.setOnAction(event -> navigateTo(event, "/view/AdminAcadamicRecords.fxml", "Academic Records"));
        reportsAnalyticsButton.setOnAction(event -> navigateTo(event, "/view/AdminReportsAnalytics.fxml", "Reports & Analytics"));
        logoutButton.setOnAction(event -> navigateTo(event, "/view/Login.fxml", "Login"));
    }
    

    private void loadStudents() {
        String query = "SELECT * FROM students";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String password = rs.getString("password");
                LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
                String address = rs.getString("address");

                studentsTable.getItems().add(new Student(studentId, name, email, phoneNumber, password, dateOfBirth, address));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load students: " + e.getMessage());
        }
    }

    @FXML
    private void addStudent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddStudentDialog.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Student");
            stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
            stage.showAndWait();

            // Reload students after adding
            studentsTable.getItems().clear();
            loadStudents();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load the add student dialog.");
        }
    }

    @FXML
    private void updateStudent() {
        Student selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateStudentDialog.fxml"));
                Parent root = loader.load();

                UpdateStudentDialogController controller = loader.getController();
                controller.setSelectedStudent(selectedStudent);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Update Student");
                stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
                stage.showAndWait();

                // Reload students after updating
                studentsTable.getItems().clear();
                loadStudents();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Could not load the update student dialog.");
            }
        } else {
            showAlert("Error", "No student selected.");
        }
    }

    @FXML
    private void deleteStudent() {
        Student selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            String query = "DELETE FROM students WHERE student_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, selectedStudent.getStudentId());
                int rowsDeleted = stmt.executeUpdate();
                if (rowsDeleted > 0) {
                    studentsTable.getItems().remove(selectedStudent); // Remove from table
                    showAlert("Success", "Student deleted successfully.");
                } else {
                    showAlert("Error", "Failed to delete student.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Database error: " + e.getMessage());
            }
        } else {
            showAlert("Error", "No student selected.");
        }
    }

    @FXML
    private void resetForm() {
        studentsTable.getItems().clear();
        loadStudents();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

private void navigateTo(javafx.event.ActionEvent event, String fxmlPath, String title) { // Navigate to a specific page
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

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
