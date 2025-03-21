package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import db.DBConnection;
import models.Enrollment;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManageEnrollmentsController implements Initializable {

    @FXML private TableView<Enrollment> enrollmentTable;
    @FXML private TableColumn<Enrollment, Integer> colEnrollmentID;
    @FXML private TableColumn<Enrollment, Integer> colStudentID;
    @FXML private TableColumn<Enrollment, String> colStudentName;
    @FXML private TableColumn<Enrollment, Integer> colCourseID;
    @FXML private TableColumn<Enrollment, String> colCourseTitle;
    @FXML private TableColumn<Enrollment, String> colEnrollmentDate;

    @FXML private TextField searchField;
    @FXML private Button btnSearch;
    @FXML private Button btnAddEnrollment;
    @FXML private Button btnUpdateEnrollment;
    @FXML private Button btnDeleteEnrollment;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind table columns to Enrollment model properties
        colEnrollmentID.setCellValueFactory(new PropertyValueFactory<>("enrollmentId"));
        colStudentID.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colCourseID.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colCourseTitle.setCellValueFactory(new PropertyValueFactory<>("courseTitle"));
        colEnrollmentDate.setCellValueFactory(new PropertyValueFactory<>("enrollmentDate"));

        // Load enrollments
        loadEnrollments();

        // Set button actions
        btnSearch.setOnAction(event -> searchEnrollments());
        btnAddEnrollment.setOnAction(event -> addEnrollment());
        btnUpdateEnrollment.setOnAction(event -> updateEnrollment());
        btnDeleteEnrollment.setOnAction(event -> deleteEnrollment());
    }

    // Load enrollments from the database
    private void loadEnrollments() {
        String query = "SELECT * FROM enrollments";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int enrollmentId = rs.getInt("enrollment_id");
                int studentId = rs.getInt("student_id");
                String studentName = rs.getString("student_name");
                int courseId = rs.getInt("course_id");
                String courseTitle = rs.getString("course_title");
                String enrollmentDate = rs.getDate("enrollment_date").toString();

                enrollmentTable.getItems().add(new Enrollment(enrollmentId, studentId, studentName, courseId, courseTitle, enrollmentDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load enrollments: " + e.getMessage());
        }
    }

    // Search enrollments by student ID or course ID
    private void searchEnrollments() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadEnrollments(); // Reload all enrollments if search field is empty
            return;
        }

        String query = "SELECT * FROM enrollments WHERE student_id = ? OR course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, searchText);
            stmt.setString(2, searchText);
            ResultSet rs = stmt.executeQuery();

            enrollmentTable.getItems().clear(); // Clear the table before adding search results
            while (rs.next()) {
                int enrollmentId = rs.getInt("enrollment_id");
                int studentId = rs.getInt("student_id");
                String studentName = rs.getString("student_name");
                int courseId = rs.getInt("course_id");
                String courseTitle = rs.getString("course_title");
                String enrollmentDate = rs.getDate("enrollment_date").toString();

                enrollmentTable.getItems().add(new Enrollment(enrollmentId, studentId, studentName, courseId, courseTitle, enrollmentDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to search enrollments: " + e.getMessage());
        }
    }

    // Add a new enrollment
   private void addEnrollment() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddEnrollmentDialog.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Add Enrollment");
        stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
        stage.showAndWait();

        // Reload enrollments after adding
        enrollmentTable.getItems().clear();
        loadEnrollments();
    } catch (IOException e) {
        e.printStackTrace();
        showAlert("Error", "Could not load the add enrollment dialog.");
    }
}

private void updateEnrollment() {
    Enrollment selectedEnrollment = enrollmentTable.getSelectionModel().getSelectedItem();
    if (selectedEnrollment != null) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateEnrollmentDialog.fxml"));
            Parent root = loader.load();

            UpdateEnrollmentDialogController controller = loader.getController();
            controller.setEnrollmentId(selectedEnrollment.getEnrollmentId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Enrollment");
            stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
            stage.showAndWait();

            // Reload enrollments after updating
            enrollmentTable.getItems().clear();
            loadEnrollments();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load the update enrollment dialog.");
        }
    } else {
        showAlert("Error", "No enrollment selected.");
    }
}
       

    // Delete an enrollment
    private void deleteEnrollment() {
        Enrollment selectedEnrollment = enrollmentTable.getSelectionModel().getSelectedItem();
        if (selectedEnrollment != null) {
            String query = "DELETE FROM enrollments WHERE enrollment_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, selectedEnrollment.getEnrollmentId());
                stmt.executeUpdate();

                // Remove the enrollment from the table
                enrollmentTable.getItems().remove(selectedEnrollment);
                showAlert("Success", "Enrollment deleted successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to delete enrollment: " + e.getMessage());
            }
        } else {
            showAlert("Error", "No enrollment selected.");
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