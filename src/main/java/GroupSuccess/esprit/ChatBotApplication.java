package GroupSuccess.esprit;

import Controller.ChatBotController;
import Service.AbsencePenaliteService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChatBotApplication extends Application {

    private AbsencePenaliteService service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatBotInterface.fxml"));
        BorderPane root = loader.load(); // Use BorderPane instead of AnchorPane

        // Créer une instance du service
        service = new AbsencePenaliteService();

        // Obtenir le contrôleur et initialiser le service
        ChatBotController controller = loader.getController();
        controller.setService(service);  // Cette ligne doit être correcte si setService() est défini dans le contrôleur

        // Créer et afficher la scène
        Scene scene = new Scene(root);
        primaryStage.setTitle("Absences&Pénalités");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}