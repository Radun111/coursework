package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class StuDashboardController {

    @FXML
    private void handleProfileButtonAction(ActionEvent event) {
        System.out.println("Profile button clicked!"); // Debugging

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StuProfile.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Student Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Could not load StuProfile.fxml");
        }
        }

        @FXML
    private void handleViewCoursesButtonAction(ActionEvent event) {
        System.out.println("View Courses button clicked!"); 

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StuViewCourses.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Student View Courses");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Could not load StuViewCourses.fxml");
        }
    }
}
