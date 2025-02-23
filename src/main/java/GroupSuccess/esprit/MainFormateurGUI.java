package GroupSuccess.esprit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFormateurGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier FXML (assurez-vous que le fichier est dans src/main/resources/gui)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormateur.fxml"));
            Parent root = loader.load();

            // Créer une scène
            Scene scene = new Scene(root);

            // Configurer la fenêtre
            primaryStage.setTitle("Gestion des Formateurs");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur de chargement de AjouetFormateur.fxml !");
        }
    }
}
