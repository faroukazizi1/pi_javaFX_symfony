package gui;

import Model.Formateur;
import Service.FormateurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ModifFormateur {

    @FXML
    private TextField tfNom;
    @FXML
    private TextField tfPrenom;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfNumero;
    @FXML
    private TextField tfSpecialite;

    private Formateur formateur;
    private FormateurService formateurService = new FormateurService();

    @FXML
    public void initialize() {
        // Initialisation des champs si nécessaire
    }

    // Méthode pour définir le formateur à modifier
    public void setFormateur(Formateur formateur) {
        this.formateur = formateur;
        tfNom.setText(formateur.getNom_F());
        tfPrenom.setText(formateur.getPrenom_F());
        tfEmail.setText(formateur.getEmail());
        tfNumero.setText(String.valueOf(formateur.getNumero()));
        tfSpecialite.setText(formateur.getSpecialite());
    }

    @FXML
    private void modifierFormateur() {
        String email = tfEmail.getText();
        String numeroStr = tfNumero.getText();

        // Vérification du numéro : doit contenir 8 chiffres
        if (!isValidNumero(numeroStr)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le numéro doit contenir exactement 8 chiffres.");
            alert.showAndWait();
            return; // Arrête l'exécution si le numéro est invalide
        }

        // Vérification de l'email
        if (!isValidEmail(email)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir une adresse email valide.");
            alert.showAndWait();
            return; // Arrête l'exécution si l'email est invalide
        }

        // Mettre à jour le formateur avec les nouvelles valeurs
        formateur.setNom_F(tfNom.getText());
        formateur.setPrenom_F(tfPrenom.getText());
        formateur.setEmail(email);
        formateur.setNumero(Integer.parseInt(numeroStr));
        formateur.setSpecialite(tfSpecialite.getText());

        // Appeler le service pour mettre à jour le formateur dans la base de données
        formateurService.update(formateur);

        // Afficher une confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setContentText("Le formateur a été modifié avec succès !");
        alert.showAndWait();
    }

    @FXML
    private void handleSave() {
        modifierFormateur();
    }

    @FXML
    private void handleRetour(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherFormateur.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour valider l'email avec une regex
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,6}$";
        return email.matches(emailRegex);
    }

    // Méthode pour valider le numéro (doit être exactement 8 chiffres)
    private boolean isValidNumero(String numero) {
        return numero.matches("\\d{8}"); // Vérifie que le numéro contient 8 chiffres
    }
}
