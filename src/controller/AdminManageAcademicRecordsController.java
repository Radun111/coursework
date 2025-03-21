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
import models.AcademicRecord;
import db.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminManageAcademicRecordsController implements Initializable {

    @FXML private TableView<AcademicRecord> tblAcademicRecords; // Table to display academic records
    @FXML private TableColumn<AcademicRecord, Integer> colRecordId; // Column for record ID
    @FXML private TableColumn<AcademicRecord, Integer> colStudentId; // Column for student ID
    @FXML private TableColumn<AcademicRecord, Integer> colCourseId; // Column for course ID
    @FXML private TableColumn<AcademicRecord, String> colGrade; // Column for grade
    @FXML private TableColumn<AcademicRecord, String> colStatus; // Column for status
    @FXML private TableColumn<AcademicRecord, String> colTerm; // Column for term
    @FXML private TableColumn<AcademicRecord, Integer> colYear; // Column for year

    @FXML private TextField searchField; // Search field for filtering records
    @FXML private Button btnAddRecord; // Button to add a new record
    @FXML private Button btnUpdateRecord; // Button to update a record
    @FXML private Button btnDeleteRecord; // Button to delete a record
    @FXML private Button btnReset; // Button to reset the form

    @FXML private Button homeButton;
    @FXML private Button manageCoursesButton;
    @FXML private Button manageStudentsButton;
    @FXML private Button enrollmentManagementButton;
    @FXML private Button academicRecordsButton;
    @FXML private Button reportsAnalyticsButton;
    @FXML private Button logoutButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind table columns to AcademicRecord model properties
        colRecordId.setCellValueFactory(new PropertyValueFactory<>("recordId"));
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colCourseId.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colTerm.setCellValueFactory(new PropertyValueFactory<>("term"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));

        loadAcademicRecords(); // Load academic records into the table

        // Set button actions
        btnDeleteRecord.setOnAction(event -> deleteRecord());
        btnReset.setOnAction(event -> resetForm());

        homeButton.setOnAction(event -> navigateTo(event, "/view/AdminDashboard.fxml", "Admin Dashboard"));
        manageCoursesButton.setOnAction(event -> navigateTo(event, "/view/AdminManageCourse.fxml", "Manage Courses"));
        manageStudentsButton.setOnAction(event -> navigateTo(event, "/view/AdminManageStudents.fxml", "Manage Students"));
        enrollmentManagementButton.setOnAction(event -> navigateTo(event, "/view/AdminEnrollmentManagement.fxml", "Enrollment Management"));
        academicRecordsButton.setOnAction(event -> navigateTo(event, "/view/AdminAcadamicRecords.fxml", "Academic Records"));
        reportsAnalyticsButton.setOnAction(event -> navigateTo(event, "/view/AdminReportsAnalytics.fxml", "Reports & Analytics"));
        logoutButton.setOnAction(event -> navigateTo(event, "/view/Login.fxml", "Login"));
    }

    

    private void loadAcademicRecords() { // Load all academic records from the database
        String query = "SELECT * FROM student_academic_records";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int recordId = rs.getInt("record_id");
                int studentId = rs.getInt("student_id");
                int courseId = rs.getInt("course_id");
                String grade = rs.getString("grade");
                String status = rs.getString("status");
                String term = rs.getString("term");
                int year = rs.getInt("year");

                tblAcademicRecords.getItems().add(new AcademicRecord(recordId, studentId, courseId, grade, status, term, year));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load academic records: " + e.getMessage());
        }
    }

    private void deleteRecord() { // Delete a selected academic record
        AcademicRecord selectedRecord = tblAcademicRecords.getSelectionModel().getSelectedItem();
        if (selectedRecord != null) {
            String query = "DELETE FROM student_academic_records WHERE record_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, selectedRecord.getRecordId());
                int rowsDeleted = stmt.executeUpdate();
                if (rowsDeleted > 0) {
                    tblAcademicRecords.getItems().remove(selectedRecord); // Remove from table
                    showAlert("Success", "Academic record deleted successfully.");
                } else {
                    showAlert("Error", "Failed to delete academic record.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Database error: " + e.getMessage());
            }
        } else {
            showAlert("Error", "No academic record selected.");
        }
    }

    private void resetForm() { // Reset the form and reload records
        tblAcademicRecords.getItems().clear();
        loadAcademicRecords();
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

    private void showAlert(String title, String message) { // Show alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}