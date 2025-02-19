package GroupSuccess.esprit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TestFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Ajouterabsence.fxml"));



            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setTitle("Gestion Absence");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Affiche l'erreur dans la console pour un meilleur débogage
        }
    }
}
