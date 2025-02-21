package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class AjouterFormateur {

    @FXML
    private TextField txtNumero;
    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtSpecialite;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnAfficher;

    @FXML
    void ajouterFormateur(ActionEvent event) {
        // Logique pour ajouter un formateur (déjà implémentée dans votre code actuel)
    }

    @FXML
    void afficherFormateurs(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherFormateur.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de chargement");
            alert.setHeaderText(null);
            alert.setContentText("Impossible d'ouvrir la liste des formateurs.");
            alert.showAndWait();
        }
    }
}
