package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import db.DBConnection;
import models.PendingApproval;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PendingApprovalsController implements Initializable {

    @FXML private TableView<PendingApproval> pendingTable;
    @FXML private TableColumn<PendingApproval, Integer> colStudentID;
    @FXML private TableColumn<PendingApproval, String> colStudentName;
    @FXML private TableColumn<PendingApproval, Integer> colCourseID;
    @FXML private TableColumn<PendingApproval, String> colCourseTitle;
    @FXML private TableColumn<PendingApproval, String> colRequestDate;
    @FXML private TableColumn<PendingApproval, String> colActions;

    @FXML private TextField searchField;
    @FXML private Button btnSearch;
    @FXML private Button btnApprove;
    @FXML private Button btnReject;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind table columns to PendingApproval model properties
        colStudentID.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colCourseID.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colCourseTitle.setCellValueFactory(new PropertyValueFactory<>("courseTitle"));
        colRequestDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));

        // Load pending approvals
        loadPendingApprovals();

        // Set button actions
        btnApprove.setOnAction(event -> approveRequest());
        btnReject.setOnAction(event -> rejectRequest());
    }

    // Load pending approvals from the database
    private void loadPendingApprovals() {
        String query = "SELECT registration_id, student_id, full_name, course_id, course_name, registration_date " +
                       "FROM course_registrations WHERE status = 'Pending'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                String studentName = rs.getString("full_name");
                int courseId = rs.getInt("course_id");
                String courseTitle = rs.getString("course_name");
                String requestDate = rs.getTimestamp("registration_date").toString();

                pendingTable.getItems().add(new PendingApproval(studentId, studentName, courseId, courseTitle, requestDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load pending approvals: " + e.getMessage());
        }
    }

    // Approve a pending request
    private void approveRequest() {
        PendingApproval selectedApproval = pendingTable.getSelectionModel().getSelectedItem();
        if (selectedApproval != null) {
            // Move the request to the enrollments table
            String insertQuery = "INSERT INTO enrollments (student_id, student_name, course_id, course_title, enrollment_date) " +
                                 "VALUES (?, ?, ?, ?, CURDATE())";
            String updateQuery = "UPDATE course_registrations SET status = 'Approved' WHERE student_id = ? AND course_id = ?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                 PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

                // Insert into enrollments table
                insertStmt.setInt(1, selectedApproval.getStudentId());
                insertStmt.setString(2, selectedApproval.getStudentName());
                insertStmt.setInt(3, selectedApproval.getCourseId());
                insertStmt.setString(4, selectedApproval.getCourseTitle());
                insertStmt.executeUpdate();

                // Update status in course_registrations table
                updateStmt.setInt(1, selectedApproval.getStudentId());
                updateStmt.setInt(2, selectedApproval.getCourseId());
                updateStmt.executeUpdate();

                // Remove the approved request from the table
                pendingTable.getItems().remove(selectedApproval);
                showAlert("Success", "Request approved successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to approve request: " + e.getMessage());
            }
        } else {
            showAlert("Error", "No request selected.");
        }
    }

    // Reject a pending request
    private void rejectRequest() {
        PendingApproval selectedApproval = pendingTable.getSelectionModel().getSelectedItem();
        if (selectedApproval != null) {
            String query = "UPDATE course_registrations SET status = 'Rejected' WHERE student_id = ? AND course_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, selectedApproval.getStudentId());
                stmt.setInt(2, selectedApproval.getCourseId());
                stmt.executeUpdate();

                // Remove the rejected request from the table
                pendingTable.getItems().remove(selectedApproval);
                showAlert("Success", "Request rejected successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to reject request: " + e.getMessage());
            }
        } else {
            showAlert("Error", "No request selected.");
        }
    }

    // Navigation methods for left bar buttons
    @FXML
    private void handleEnrollmentManagement() {
        navigateToInterface("/view/AdminEnrollmentManagement.fxml");
    }

    @FXML
    private void handleManageEnrollments() {
        navigateToInterface("/view/AdminManageEnrollment.fxml");
    }

    @FXML
    private void handlePendingApprovals() {
        navigateToInterface("/view/AdminPendingApprovals.fxml");
    }

    @FXML
    private void handleViewReports() {
        navigateToInterface("/view/AdminViewReports.fxml");
    }

    @FXML
    private void handleLogout() {
        navigateToInterface("/view/AdminDashboard.fxml");
    }   
    // Helper method to navigate to a specific interface in the same window
    private void navigateToInterface(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) pendingTable.getScene().getWindow(); // Get the current stage
            stage.setScene(new Scene(root)); // Set the new scene
            stage.setTitle("Enrollment Management System");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load the interface: " + e.getMessage());
        }
    }

    // Show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}