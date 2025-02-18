package Gui;

import Model.user;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import Service.userService;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AjouterUser implements Initializable {

    private final userService userService = new userService();

    @FXML
    private TextField TFusername;

    @FXML
    private PasswordField TFpassword;

    @FXML
    private TextField TFemail;

    @FXML
    private TextField TFnom;

    @FXML
    private TextField TFprenom;

    @FXML
    private TextField TFnumero;

    @FXML
    private CheckBox TFhomme;

    @FXML
    private CheckBox TFfemme;

    @FXML
    private TextField TFcin;

    @FXML
    private ComboBox<String> TFrole;

    @FXML
    private TextField TFadresse;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add choices to the ChoiceBox
        TFrole.getItems().addAll("RHR", "Employe");
    }

    @FXML
    void ajouterUser(ActionEvent event) {
        // Récupérer les valeurs des champs
        String username = TFusername.getText();
        String password = TFpassword.getText();
        String email = TFemail.getText();
        String nom = TFnom.getText();
        String prenom = TFprenom.getText();
        String adresse = TFadresse.getText();
        String role = TFrole.getValue();
        String cinStr = TFcin.getText().trim();
        String numeroStr = TFnumero.getText().trim();

        // Vérifier que les champs texte ne sont pas vides
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || nom.isEmpty() || prenom.isEmpty() || adresse.isEmpty() || role == null) {
            afficherAlerte("Erreur", "Champs manquants", "Tous les champs doivent être remplis.");
            return;
        }

        // Vérifier le format du CIN (doit commencer par 0 ou 1 et contenir exactement 8 chiffres)
        if (!cinStr.matches("^[01][0-9]{7}$")) {
            afficherAlerte("Erreur", "CIN invalide", "Le CIN doit commencer par 1 ou 0 et contenir exactement 8 chiffres.");
            return;
        }

        // Vérifier le format du numéro de téléphone (8 chiffres)
        if (!numeroStr.matches("^[0-9]{8}$")) {
            afficherAlerte("Erreur", "Numéro de téléphone invalide", "Le numéro de téléphone doit contenir exactement 8 chiffres.");
            return;
        }

        // Vérifier que le numéro de téléphone et le CIN sont des nombres valides
        int numero = 0, cin = 0;
        try {
            numero = Integer.parseInt(TFnumero.getText());
            cin = Integer.parseInt(TFcin.getText());
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "Numéro ou CIN invalide", "Le numéro de téléphone et le CIN doivent être des nombres valides.");
            return;
        }

        // Vérifier que le sexe est sélectionné
        String sexe = "";
        if (TFhomme.isSelected()) {
            sexe = "Homme";
            TFfemme.setSelected(false);
        } else if (TFfemme.isSelected()) {
            sexe = "Femme";
            TFhomme.setSelected(false);
        } else {
            afficherAlerte("Erreur", "Sexe manquant", "Veuillez sélectionner un sexe.");
            return;
        }

        // Vérifier que l'email est valide
        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            afficherAlerte("Erreur", "Email invalide", "Veuillez entrer un email valide.");
            return;
        }

        // Créer l'objet utilisateur
        user u = new user(cin, nom, email, prenom, username, password, role, sexe, adresse, numero);

        try {
            userService.add(u);  // Ajouter l'utilisateur à la base de données

            // Switch back to the employee list view (afficherUser interface)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherUser.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    // Fonction pour afficher des alertes
    private void afficherAlerte(String titre, String header, String contenu) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(header);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    @FXML
    private void openEmployeeListView(ActionEvent event) {
        try {
            // Charger la vue des employés
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherUser.fxml"));  // Assurez-vous que le chemin est correct
            Parent root = loader.load();

            // Récupérer la scène actuelle et la mettre à jour avec la vue des employés
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));  // Change la scène
            stage.setTitle("Liste des Employés");  // Vous pouvez définir un titre approprié

        } catch (IOException e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Erreur de chargement");
            error.setContentText("Échec du chargement de la vue des employés : " + e.getMessage());
            error.showAndWait();
        }
    }


}
