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

<<<<<<< HEAD


            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setTitle("Absences&Pénalités");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Affiche l'erreur dans la console pour un meilleur débogage
        }
=======
>>>>>>> 0542470027de48c818e3792b43273f5b8dd31e9b
    }
}

