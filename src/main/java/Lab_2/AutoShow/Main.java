package Lab_2.AutoShow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/Lab2_1.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String stylesPath = this.getClass().getResource("/style.css").toExternalForm();
        root.getStylesheets().add(stylesPath);
        primaryStage.setTitle("Distributed Computations - Lab2_1");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
