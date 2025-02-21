import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args)  {
        System.out.println("Testing GitHub Connection!");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL resource = getClass().getResource("/view/AdminManageStudents.fxml"); 
        if (resource == null) {
            throw new IllegalStateException("FXML file 'Main.fxml' not found in /view directory!");
        }
        Parent root = FXMLLoader.load(resource);
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Admin ");
        primaryStage.show();
    }

}
