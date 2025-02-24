package GroupSuccess.esprit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;

import java.io.IOException;

public class TestFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        if (loader.getLocation() == null) {
            System.out.println("FXML file not found!");
        } else {
            System.out.println("FXML file loaded successfully.");
        }
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("Gestion User");
            primaryStage.setScene(scene);
            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


    }
    


}

