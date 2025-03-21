package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import db.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReportsAndAnalyticsController implements Initializable {

    @FXML private Label lblTotalStudents;
    @FXML private Label lblTotalCourses;
    @FXML private Label lblActiveEnrollments;
    @FXML private Label lblPendingApprovals;

    @FXML private PieChart enrollmentChart;
    @FXML private BarChart<String, Number> performanceChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    @FXML private Button btnExportPDF;
    @FXML private Button btnExportExcel;
    @FXML private Button btnExportCSV;

    @FXML private Button homeButton;
    @FXML private Button manageCoursesButton;
    @FXML private Button manageStudentsButton;
    @FXML private Button enrollmentManagementButton;
    @FXML private Button academicRecordsButton;
    @FXML private Button reportsAnalyticsButton;
    @FXML private Button logoutButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load data into labels and charts
        loadTotalStudents();
        loadTotalCourses();
        loadActiveEnrollments();
        loadPendingApprovals();
        loadEnrollmentChart();
        loadPerformanceChart();

        // Set button actions
        btnExportPDF.setOnAction(event -> exportToPDF());
        btnExportExcel.setOnAction(event -> exportToExcel());
        btnExportCSV.setOnAction(event -> exportToCSV());

         // Set button actions
         homeButton.setOnAction(event -> navigateTo(event, "/view/AdminDashboard.fxml", "Admin Dashboard"));
         manageCoursesButton.setOnAction(event -> navigateTo(event, "/view/AdminManageCourse.fxml", "Manage Courses"));
         manageStudentsButton.setOnAction(event -> navigateTo(event, "/view/AdminManageStudents.fxml", "Manage Students"));
         enrollmentManagementButton.setOnAction(event -> navigateTo(event, "/view/AdminEnrollmentManagement.fxml", "Enrollment Management"));
         academicRecordsButton.setOnAction(event -> navigateTo(event, "/view/AdminAcadamicRecords.fxml", "Academic Records"));
         reportsAnalyticsButton.setOnAction(event -> navigateTo(event, "/view/AdminReportsAnalytics.fxml", "Reports & Analytics"));
         logoutButton.setOnAction(event -> navigateTo(event, "/view/Login.fxml", "Login"));
      
    }

    private void loadTotalStudents() {
        String query = "SELECT COUNT(*) AS total_students FROM students";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                lblTotalStudents.setText(String.valueOf(rs.getInt("total_students")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load total students: " + e.getMessage());
        }
    }

    private void loadTotalCourses() {
        String query = "SELECT COUNT(*) AS total_courses FROM courses";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                lblTotalCourses.setText(String.valueOf(rs.getInt("total_courses")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load total courses: " + e.getMessage());
        }
    }

    private void loadActiveEnrollments() {
        String query = "SELECT COUNT(*) AS active_enrollments FROM enrollments";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                lblActiveEnrollments.setText(String.valueOf(rs.getInt("active_enrollments")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load active enrollments: " + e.getMessage());
        }
    }

    private void loadPendingApprovals() {
        String query = "SELECT COUNT(*) AS pending_approvals FROM course_registrations WHERE status = 'Pending'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                lblPendingApprovals.setText(String.valueOf(rs.getInt("pending_approvals")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load pending approvals: " + e.getMessage());
        }
    }

    private void loadEnrollmentChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Use course_title instead of course_name
        String query = "SELECT course_title, COUNT(*) AS enrollments FROM enrollments GROUP BY course_title";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pieChartData.add(new PieChart.Data(rs.getString("course_title"), rs.getInt("enrollments")));
            }

            enrollmentChart.setData(pieChartData);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load enrollment chart data: " + e.getMessage());
        }
    }

    private void loadPerformanceChart() {
        ObservableList<BarChart.Series<String, Number>> barChartData = FXCollections.observableArrayList();
        BarChart.Series<String, Number> series = new BarChart.Series<>();
        series.setName("Average GPA");

        // Join courses table to get course_name
        String query = "SELECT c.course_name, AVG(s.grade) AS avg_gpa " +
                       "FROM student_academic_records s " +
                       "JOIN courses c ON s.course_id = c.course_id " +
                       "GROUP BY c.course_name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                series.getData().add(new BarChart.Data<>(rs.getString("course_name"), rs.getDouble("avg_gpa")));
            }

            barChartData.add(series);
            performanceChart.setData(barChartData);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load performance chart data: " + e.getMessage());
        }
    }

    private void exportToPDF() {
        // Implement PDF export logic
        showAlert("Info", "Export to PDF functionality not implemented yet.");
    }

    private void exportToExcel() {
        // Implement Excel export logic
        showAlert("Info", "Export to Excel functionality not implemented yet.");
    }

    private void exportToCSV() {
        // Implement CSV export logic
        showAlert("Info", "Export to CSV functionality not implemented yet.");
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
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