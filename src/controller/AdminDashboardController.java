package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import models.Course;
import models.Student;
import db.DBConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    @FXML private ListView<String> coursesListView; // ListView to display courses
    @FXML private ListView<String> studentsListView; // ListView to display students

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCourses(); // Load courses into the ListView
        loadStudents(); // Load students into the ListView
    }

    private void loadCourses() { // Load all courses from the database
        String query = "SELECT course_id, course_name FROM courses";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String courseId = rs.getString("course_id");
                String courseName = rs.getString("course_name");
                coursesListView.getItems().add(courseId + " - " + courseName); // Add course to ListView
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error loading courses: " + e.getMessage());
        }
    }

    private void loadStudents() { // Load all students from the database
        String query = "SELECT student_id, name FROM students";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String studentId = rs.getString("student_id");
                String studentName = rs.getString("name");
                studentsListView.getItems().add(studentId + " - " + studentName); // Add student to ListView
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error loading students: " + e.getMessage());
        }
    }
}