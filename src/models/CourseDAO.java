package models;

import models.Course;
import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public static List<Course> getAllCourses() {
        List<Course> courseList = new ArrayList<>();
        String query = "SELECT * FROM courses";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Course course = new Course(
                    rs.getInt("course_id"),
                    rs.getString("course_name"),
                    rs.getInt("credit_hours"),
                    rs.getString("instructor"),
                    rs.getString("department"),
                    rs.getString("prerequisites"),
                    rs.getInt("max_capacity")
                );
                courseList.add(course);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseList;
    }
}
