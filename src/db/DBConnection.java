package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/coursework_crs";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";

    
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Database Connection Failed: " + e.getMessage());
            return null;
        }
    }
}