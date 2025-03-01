package GroupSuccess.esprit;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);


            String css = getClass().getResource("/styles.css").toExternalForm();
            if (css != null) {
                scene.getStylesheets().add(css);
            } else {
                System.err.println("CSS file not found!");
            }

            primaryStage.setTitle("Gestion des Congés et Bulletins de Paie");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
