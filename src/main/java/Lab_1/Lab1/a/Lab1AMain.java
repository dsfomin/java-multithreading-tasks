package Lab_1.Lab1.a;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Lab1AMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/Lab1_a.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String stylesPath = this.getClass().getResource("/style.css").toExternalForm();
        root.getStylesheets().add(stylesPath);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Distributed Computations - Lab_1.Lab1 a");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
