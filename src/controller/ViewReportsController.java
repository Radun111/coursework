package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import db.DBConnection;
import models.CourseRegistration;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ViewReportsController implements Initializable {

    @FXML private TableView<CourseRegistration> reportTable;
    @FXML private TableColumn<CourseRegistration, Integer> colStudentID;
    @FXML private TableColumn<CourseRegistration, String> colStudentName;
    @FXML private TableColumn<CourseRegistration, Integer> colCourseID;
    @FXML private TableColumn<CourseRegistration, String> colCourseTitle;
    @FXML private TableColumn<CourseRegistration, String> colStatus;
    @FXML private TableColumn<CourseRegistration, String> colDateEnrolled;

    @FXML private TextField searchField;
    @FXML private DatePicker fromDate;
    @FXML private DatePicker toDate;
    @FXML private Button btnGenerateReport;
    @FXML private Button btnExportPDF;
    @FXML private Button btnExportExcel;
    @FXML private Button btnExportCSV;

    @FXML private PieChart enrollmentChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind table columns to CourseRegistration model properties
        colStudentID.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colCourseID.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colCourseTitle.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDateEnrolled.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));

        // Load registration data
        loadRegistrationData(null, null);

        // Set button actions
        btnGenerateReport.setOnAction(event -> generateReport());
        btnExportPDF.setOnAction(event -> exportToPDF());
        btnExportExcel.setOnAction(event -> exportToExcel());
        btnExportCSV.setOnAction(event -> exportToCSV());
    }

    // Load registration data from the database
    private void loadRegistrationData(LocalDate startDate, LocalDate endDate) {
        String query = "SELECT student_id, full_name, course_id, course_name, status, registration_date " +
                       "FROM course_registrations WHERE 1=1";
        if (startDate != null) {
            query += " AND registration_date >= '" + startDate + "'";
        }
        if (endDate != null) {
            query += " AND registration_date <= '" + endDate + "'";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            reportTable.getItems().clear(); // Clear the table before adding new data
            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                String fullName = rs.getString("full_name");
                int courseId = rs.getInt("course_id");
                String courseName = rs.getString("course_name");
                String status = rs.getString("status");
                String registrationDate = rs.getTimestamp("registration_date").toString();

                reportTable.getItems().add(new CourseRegistration(studentId, fullName, courseId, courseName, status, registrationDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load registration data: " + e.getMessage());
        }
    }

    // Generate report based on date range
    private void generateReport() {
        LocalDate startDate = fromDate.getValue();
        LocalDate endDate = toDate.getValue();

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            showAlert("Error", "From Date must be before To Date.");
            return;
        }

        loadRegistrationData(startDate, endDate);
        updatePieChart();
    }

    // Update the PieChart with registration statistics
    private void updatePieChart() {
        enrollmentChart.getData().clear();

        int approvedCount = 0;
        int pendingCount = 0;
        int rejectedCount = 0;

        for (CourseRegistration registration : reportTable.getItems()) {
            switch (registration.getStatus()) {
                case "Approved":
                    approvedCount++;
                    break;
                case "Pending":
                    pendingCount++;
                    break;
                case "Rejected":
                    rejectedCount++;
                    break;
            }
        }

        enrollmentChart.getData().add(new PieChart.Data("Approved", approvedCount));
        enrollmentChart.getData().add(new PieChart.Data("Pending", pendingCount));
        enrollmentChart.getData().add(new PieChart.Data("Rejected", rejectedCount));
    }

    // Export report to PDF
    private void exportToPDF() {
        showAlert("Info", "Export to PDF functionality not implemented yet.");
    }

    // Export report to Excel
    private void exportToExcel() {
        showAlert("Info", "Export to Excel functionality not implemented yet.");
    }

    // Export report to CSV
    private void exportToCSV() {
        showAlert("Info", "Export to CSV functionality not implemented yet.");
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
            Stage stage = (Stage) reportTable.getScene().getWindow(); // Get the current stage
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