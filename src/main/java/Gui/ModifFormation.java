package Gui;

import Model.Formation;
import Service.FormationService;
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
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ModifFormation {

    @FXML
    private TextField tfTitre;
    @FXML
    private TextField tfDescription;
    @FXML
    private TextField tfDateD;
    @FXML
    private TextField tfDateF;
    @FXML
    private TextField tfDuree;
    @FXML
    private TextField tfImage;
    @FXML
    private TextField tfIdFormateur;

    private Formation formation;
    private FormationService formationService = new FormationService();

    @FXML
    public void initialize() {
        // Initialisation des champs si nécessaire
    }

    // Méthode pour définir la formation à modifier
    public void setFormation(Formation formation) {
        this.formation = formation;
        tfTitre.setText(formation.getTitre());
        tfDescription.setText(formation.getDescription());
        tfDateD.setText(formation.getDate_D() != null ? formation.getDate_D().toString() : "");
        tfDateF.setText(formation.getDate_F() != null ? formation.getDate_F().toString() : "");
        tfDuree.setText(String.valueOf(formation.getDuree()));
        tfImage.setText(formation.getImage());
        tfIdFormateur.setText(String.valueOf(formation.getId_Formateur()));
    }

    @FXML
    private void modifierFormation() {
        try {
            // Validation des champs et mise à jour de la formation
            String titre = tfTitre.getText();
            String description = tfDescription.getText();
            String image = tfImage.getText();
            int idFormateur = 0;

            // Vérifier que l'ID Formateur est valide
            try {
                idFormateur = Integer.parseInt(tfIdFormateur.getText());
            } catch (NumberFormatException e) {
                showAlert("Erreur", "ID Formateur invalide", "L'ID formateur doit être un entier.");
                return;
            }

            // Vérification des dates
            LocalDate dateDebut = null;
            LocalDate dateFin = null;

            try {
                if (!tfDateD.getText().isEmpty()) {
                    dateDebut = LocalDate.parse(tfDateD.getText());
                }
                if (!tfDateF.getText().isEmpty()) {
                    dateFin = LocalDate.parse(tfDateF.getText());
                }
            } catch (DateTimeParseException e) {
                showAlert("Erreur", "Format de date invalide", "Veuillez entrer les dates au format YYYY-MM-DD.");
                return;
            }

            // Validation que la date de début n'est pas dans le passé
            if (dateDebut != null && dateDebut.isBefore(LocalDate.now())) {
                showAlert("Erreur", "Date de début invalide", "La date de début ne peut pas être dans le passé.");
                return;
            }

            // Validation que la date de début n'est pas après la date de fin
            if (dateDebut != null && dateFin != null && dateDebut.isAfter(dateFin)) {
                showAlert("Erreur", "Dates incorrectes", "La date de début ne peut pas être après la date de fin.");
                return;
            }

            // Vérification de la durée
            int duree = 0;
            try {
                duree = Integer.parseInt(tfDuree.getText());
                // Vérification si la durée dépasse 999
                if (duree < 0 || duree > 999) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Durée invalide", "Veuillez entrer une durée valide entre 0 et 999.");
                return;
            }

            // Mise à jour de la formation avec les nouvelles valeurs
            formation.setTitre(titre);
            formation.setDescription(description);
            formation.setDate_D(Date.valueOf(dateDebut));
            formation.setDate_F(Date.valueOf(dateFin));
            formation.setDuree(duree);
            formation.setImage(image);
            formation.setId_Formateur(idFormateur);

            // Appeler le service pour mettre à jour la formation dans la base de données
            formationService.update(formation);

            // Afficher une confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("La formation a été modifiée avec succès !");
            alert.showAndWait();

        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue", "Vérifiez vos informations et réessayez.");
        }
    }

    @FXML
    private void handleSave() {
        modifierFormation();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleRetour(ActionEvent event) {
        try {
            // Assurez-vous que le chemin vers le fichier FXML est correct
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherFormation.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
