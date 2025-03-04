package Gui;

import Model.Formateur;
import Service.FormateurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    private Button btnAjouter;
    @FXML
    private Button btnAfficher;
    @FXML
    private ComboBox<String> cmbSpecialite;  // ComboBox pour la spécialité

    private final FormateurService formateurService = new FormateurService(); // Service pour l'ajout en BD

    // Initialisation de la ComboBox avec les options de spécialité
    @FXML
    void initialize() {
        // Ajout des spécialités à la ComboBox
        cmbSpecialite.getItems().addAll("Soft Skills", "Programmation Web", "Programmation Mobile", "Programmation Desktop");
    }

    // Méthode pour ajouter un formateur
    @FXML
    void ajouterFormateur(ActionEvent event) {
        try {
            String numeroStr = txtNumero.getText().trim();
            String nom = txtNom.getText().trim();
            String prenom = txtPrenom.getText().trim();
            String email = txtEmail.getText().trim();
            String specialite = cmbSpecialite.getValue(); // Utilisation de la valeur sélectionnée dans ComboBox

            // Vérification des champs vides
            if (numeroStr.isEmpty() || nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || specialite == null) {
                showAlert(Alert.AlertType.WARNING, "Champs vides", "Veuillez remplir tous les champs.");
                return;
            }

            // Vérification du numéro (exactement 8 chiffres)
            if (!isValidNumero(numeroStr)) {
                showAlert(Alert.AlertType.ERROR, "Numéro invalide", "Le numéro doit contenir exactement 8 chiffres.");
                return;
            }

            // Vérification de l'email (format correct)
            if (!isValidEmail(email)) {
                showAlert(Alert.AlertType.ERROR, "Email invalide", "Veuillez entrer une adresse e-mail valide.");
                return;
            }

            // Conversion du numéro en entier
            int numero = Integer.parseInt(numeroStr);

            // Création et ajout du formateur
            Formateur formateur = new Formateur(numero, nom, prenom, email, specialite);
            formateurService.add(formateur);  // Enregistrement dans la base de données

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Formateur ajouté avec succès !");
            clearFields();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer un numéro valide.");
        }
    }

    // Méthode pour afficher les formateurs
    @FXML
    void afficherFormateurs(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherFormateur.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Impossible d'ouvrir la liste des formateurs.");
        }
    }

    // Vérifie si le numéro est composé exactement de 8 chiffres
    private boolean isValidNumero(String numero) {
        return numero.matches("\\d{8}");
    }

    // Vérifie si l'email suit un format valide
    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    // Méthode pour afficher des alertes
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour vider les champs après l'ajout
    private void clearFields() {
        txtNumero.clear();
        txtNom.clear();
        txtPrenom.clear();
        txtEmail.clear();
        cmbSpecialite.getSelectionModel().clearSelection();  // Réinitialise la sélection de la ComboBox
    }

            }
