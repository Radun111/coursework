package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import db.DBConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EnrollmentManagementController implements Initializable {

    @FXML private ListView<String> enrolledStudentsListView;
    @FXML private ListView<String> pendingApprovalsListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadEnrolledStudents();
        loadPendingApprovals();
    }

    // Load enrolled students (status = 'Approved')
    private void loadEnrolledStudents() {
        String query = "SELECT full_name, course_name, registration_date FROM course_registrations WHERE status = 'Approved'";
        loadDataIntoListView(query, enrolledStudentsListView);
    }

    // Load pending approvals (status = 'Pending')
    private void loadPendingApprovals() {
        String query = "SELECT full_name, course_name, registration_date FROM course_registrations WHERE status = 'Pending'";
        loadDataIntoListView(query, pendingApprovalsListView);
    }

    // Helper method to load data into a ListView
    private void loadDataIntoListView(String query, ListView<String> listView) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String fullName = rs.getString("full_name");
                String courseName = rs.getString("course_name");
                String registrationDate = rs.getTimestamp("registration_date").toString();

                // Format the data for display
                String displayText = String.format("%s - %s (Registered on: %s)", fullName, courseName, registrationDate);
                listView.getItems().add(displayText);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load data: " + e.getMessage());
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
            Stage stage = (Stage) enrolledStudentsListView.getScene().getWindow(); // Get the current stage
            stage.setScene(new Scene(root)); // Set the new scene
            stage.setTitle("Enrollment Management System");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load the interface: " + e.getMessage());
        }
    }

    // Show an alert dialog
    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}