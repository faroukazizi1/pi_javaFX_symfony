package Controller;

import Gui.UserSession;
import Service.AbsencePenaliteService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class ChatBotController {

    @FXML
    private TextField inputField;

    @FXML
    private TextArea chatArea;

    @FXML
    private AnchorPane anchorPane;

    private AbsencePenaliteService service;
    private int cin = -1;

    // Méthode pour injecter le service
    public void setService(AbsencePenaliteService service) {
        this.service = service;
    }

    // Méthode pour démarrer la conversation
    public void start() {
        chatArea.appendText("Bonjour! Entrez votre CIN : \n");
    }

    // Méthode appelée automatiquement à l'initialisation de la scène (FXML)
    @FXML
    public void initialize() {
        UserSession session = UserSession.getInstance();
        String userRole = session.getRole();
        if (!"RHR".equals(userRole)) {
            Platform.runLater(() -> {
                try {
                    // Charger la vue du ChatBotController
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatBotInterface.fxml"));
                    Parent chatbotView = loader.load();

                    // Récupérer le contrôleur si nécessaire
                    ChatBotController controller = loader.getController();

                    // Afficher la vue (ajuster selon ton layout)
                    Scene scene = new Scene(chatbotView);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("ChatBot");
                    stage.show();



                } catch (IOException e) {
                    e.printStackTrace(); // Gérer l'erreur proprement
                }
            });
        }

        start();  // Appeler start() lors de l'initialisation

        // Ajouter un écouteur sur la touche "Entrée"
        inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSubmit(); // Appelle la méthode d'envoi
            }

        });
    }


    // Méthode pour gérer l'envoi de l'entrée (CIN ou choix)
    @FXML
    private void handleSubmit() {
        String input = inputField.getText();
        if (input.isEmpty()) {
            showErrorMessage("Le CIN ou le choix ne peut pas être vide.");
            return;
        }

        try {
            if (cin == -1) {
                // Première entrée - Cin de l'utilisateur
                try {
                    cin = Integer.parseInt(input);
                    Map<String, Object> absencePenaliteInfo = service.getAbsenceAndPenaliteInfoByCin(cin);

                    if (absencePenaliteInfo.isEmpty()) {
                        chatArea.appendText("CIN non trouvé. Veuillez entrer un CIN valide : \n");
                        cin = -1;
                        inputField.clear();
                    } else {
                        chatArea.appendText("CIN trouvé : " + absencePenaliteInfo.get("cin") + "\n");
                        chatArea.appendText("Que voulez-vous savoir ? \n");
                        chatArea.appendText("1. Le nombre d'absences.\n");
                        chatArea.appendText("2. Le type de pénalité.\n");
                        chatArea.appendText("3. Le seuil de pénalité.\n");
                        chatArea.appendText("4. Toutes les informations.\n");
                        chatArea.appendText("5. Détection de fraudes dans les absences.\n");
                        chatArea.appendText("6. Quitter.\n");
                        clearFields();
                    }
                } catch (NumberFormatException e) {
                    showErrorMessage("Le CIN doit être un nombre entier valide.");
                    inputField.clear();
                }
            } else {
                int choix = Integer.parseInt(input);
                Map<String, Object> absencePenaliteInfo = service.getAbsenceAndPenaliteInfoByCin(cin);
                String message = "";

                switch (choix) {
                    case 1:
                        message = "Le nombre d'absences est : " + absencePenaliteInfo.get("nbr_abs");
                        break;
                    case 2:
                        message = "Le type de pénalité est : " + absencePenaliteInfo.get("penalite_type");
                        break;
                    case 3:
                        message = "Le seuil de pénalité est : " + absencePenaliteInfo.get("seuil");
                        break;
                    case 4:
                        message = "Voici toutes les informations concernant votre absence et pénalités :\n" +
                                "CIN : " + absencePenaliteInfo.get("cin") + "\n" +
                                "Le nombre d'absences est : " + absencePenaliteInfo.get("nbr_abs") + "\n" +
                                "Le type de pénalité est : " + absencePenaliteInfo.get("penalite_type") + "\n" +
                                "Le seuil de pénalité est : " + absencePenaliteInfo.get("seuil") + "\n" +
                                "Détection de fraudes :\n" + service.detectFraudulentAbsences(cin);
                        break;
                    case 5:
                        message = "Analyse des absences en cours...\n";
                        detectFraud();
                        break;
                    case 6:
                        message = "À bientôt !";
                        break;
                    default:
                        message = "Choix invalide. Veuillez entrer un numéro valide.";
                        break;
                }

                chatArea.appendText(message + "\n");
                inputField.clear();

                if (choix == 5) {
                    inputField.setDisable(true);
                }
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Veuillez entrer un nombre valide.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Méthode pour afficher un message d'erreur
    private void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour effacer les champs après la validation du CIN
    private void clearFields() {
        inputField.clear();
    }

    // Méthode pour détecter les fraudes dans les absences
    @FXML
    private void detectFraud() {
        try {
            if (cin == -1) {
                chatArea.appendText("Veuillez entrer votre CIN avant de détecter des fraudes.\n");
                return;
            }

            String fraudMessages = service.detectFraudulentAbsences(cin);
            if (!fraudMessages.isEmpty()) {
                chatArea.appendText(fraudMessages);
            } else {
                chatArea.appendText("Aucune fraude détectée pour ce CIN.\n");
            }
        } catch (Exception e) {
            chatArea.appendText("Erreur lors de la détection de fraude.\n");
            e.printStackTrace();
        }
    }
}