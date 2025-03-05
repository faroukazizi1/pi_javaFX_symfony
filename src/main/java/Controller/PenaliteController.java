package Controller;

import Service.TwilioSMSService;
import Service.penaliteService;
import Service.absenceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import models.penalite;
import models.absence;

import java.util.List;

public class PenaliteController {

    @FXML
    private ComboBox<absence> absenceComboBox;
    @FXML
    private ComboBox<String> typeComboBoxAdd, typeComboBoxUpdate;
    @FXML
    private ComboBox<absence> absenceComboBoxAdd;
    @FXML
    private TextField seuilAbsFieldAdd, seuilAbsFieldUpdate, idFieldUpdate, idFieldDelete;
    @FXML
    private GridPane penaliteGridView;
    @FXML
    private TextField cinFieldUpdate;
    @FXML
    private TextField cinFieldDelete;

    private penaliteService penaliteService = new penaliteService();
    private absenceService absenceService = new absenceService();
    private TwilioSMSService twilioService = new TwilioSMSService(); // Créer une instance du service Twilio

    @FXML
    public void initialize() {
        // Remplissage des ComboBox avec les types de pénalité
        if (typeComboBoxAdd != null) {
            typeComboBoxAdd.getItems().addAll("Avertissement écrit", "Suspension temporaire", "Amende", "Démotion");
        }
        if (typeComboBoxUpdate != null) {
            typeComboBoxUpdate.getItems().addAll("Avertissement écrit", "Suspension temporaire", "Amende", "Démotion");
        }

        // Charger les absences disponibles dans la ComboBox
        List<absence> absences = absenceService.getAll();
        absenceComboBoxAdd.getItems().addAll(absences);
    }

    @FXML
    public void handleAbsenceSelection(ActionEvent actionEvent) {
        absence selectedAbsence = absenceComboBoxAdd.getSelectionModel().getSelectedItem();
        if (selectedAbsence != null) {
            // Afficher le nombre d'absences
            int nbrAbs = selectedAbsence.getNbr_abs();
            int seuilAbs = nbrAbs / 2; // Calcul du seuil d'absences

            // Remplir le TextField avec la valeur du seuil d'absences
            seuilAbsFieldAdd.setText(String.valueOf(seuilAbs));
        }
    }

    @FXML
    public void handleAddPenalite() {
        try {
            if (typeComboBoxAdd.getValue() == null || seuilAbsFieldAdd.getText().isEmpty() || absenceComboBoxAdd.getValue() == null) {
                throw new Exception("Veuillez remplir tous les champs.");
            }

            absence selectedAbsence = absenceComboBoxAdd.getValue(); // Récupérer l'absence sélectionnée
            int absenceId = selectedAbsence.getId_abs(); // Obtenir l'ID de l'absence
            int cin = selectedAbsence.getCin(); // Récupérer le CIN de l'absence
            String type = typeComboBoxAdd.getValue();
            int seuilAbs = Integer.parseInt(seuilAbsFieldAdd.getText()); // Seuil des absences

            // Créer une nouvelle pénalité avec le CIN
            penalite newPenalite = new penalite(0, type, seuilAbs, cin); // Remplacer id_absence par cin
            penaliteService.add(newPenalite); // Appeler le service pour ajouter la pénalité

            // Associer la pénalité à l'absence
            absenceService.applyPenaliteToAbsence(absenceId, newPenalite);

            // Vérifier si le seuil d'absences dépasse 3 pour envoyer un SMS
            if (seuilAbs > 3) {
                // Envoi du SMS si le seuil dépasse 3
                String phoneNumber = "+21626404611"; // Remplacer par le numéro réel ou récupère depuis la base
                String message = "L'Employé avec CIN: " + cin + " a atteint le seuil de penalités";
                twilioService.sendSMS(phoneNumber, message); // Appel à Twilio pour envoyer un SMS
            }

            // Afficher un message de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Pénalité ajoutée.");

            // Réinitialiser les champs
            seuilAbsFieldAdd.clear();
            typeComboBoxAdd.getSelectionModel().clearSelection();
            absenceComboBoxAdd.getSelectionModel().clearSelection();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout : " + e.getMessage());
        }
    }


    @FXML
    public void handleUpdatePenalite() {
        try {
            if (typeComboBoxUpdate.getValue() == null || seuilAbsFieldUpdate.getText().isEmpty() || cinFieldUpdate.getText().isEmpty()) {
                throw new Exception("Veuillez remplir tous les champs.");
            }

            String type = typeComboBoxUpdate.getValue();
            int seuilAbs = Integer.parseInt(seuilAbsFieldUpdate.getText());
            int cin = Integer.parseInt(cinFieldUpdate.getText());

            // Mettre à jour la pénalité
            penalite updatedPenalite = new penalite(type, seuilAbs, cin);
            penaliteService.update(updatedPenalite);

            // Envoi du SMS après mise à jour de la pénalité
            String phoneNumber = "+21626404611"; // Remplace par un numéro réel ou récupère depuis la base
            String message = "Pénalité mise à jour pour le CIN: " + cin;
            twilioService.sendSMS(phoneNumber, message); // Appel à Twilio pour envoyer un SMS

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Pénalité mise à jour et SMS envoyé avec succès.");

            // Réinitialiser les champs
            typeComboBoxUpdate.getSelectionModel().clearSelection();
            seuilAbsFieldUpdate.clear();
            cinFieldUpdate.clear();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    // Fonction pour afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Supprimer une pénalité
    @FXML
    public void handleDeletePenalite() {
        try {
            // Vérifier si le champ CIN est bien rempli
            if (cinFieldDelete.getText().isEmpty()) {
                throw new Exception("Veuillez entrer un CIN.");
            }

            // Récupérer le CIN de l'utilisateur
            int cin = Integer.parseInt(cinFieldDelete.getText());

            // Créer un objet penalite avec le CIN (l'objet sera utilisé pour la suppression)
            penalite penaliteToDelete = new penalite(0, "", 0, cin);  // On met 0 pour id, type, et seuil_abs car ils ne sont pas nécessaires pour la suppression

            // Appeler le service pour supprimer la pénalité par CIN
            penaliteService.delete(penaliteToDelete);

            // Afficher un message de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Pénalité supprimée avec succès.");

            // Nettoyer le champ CIN après suppression
            cinFieldDelete.clear();

        } catch (Exception e) {
            // Afficher un message d'erreur en cas de problème
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression : " + e.getMessage());
        }
    }
    // Afficher toutes les pénalités
    @FXML
    public void handleViewPenalites() {
        try {
            List<penalite> penalites = penaliteService.getAll();
            penaliteGridView.getChildren().clear(); // Nettoyer les anciennes données

            // Ajouter les titres des colonnes
            penaliteGridView.add(new Label("Type"), 0, 0);  // Type à la première position
            penaliteGridView.add(new Label("Seuil"), 1, 0);
            penaliteGridView.add(new Label("CIN"), 2, 0);

            // Appliquer un style aux titres
            for (int i = 0; i < 3; i++) {  // Modifier de 4 à 3 car on n'affiche plus l'ID
                Label titleLabel = (Label) penaliteGridView.getChildren().get(i);
                titleLabel.setStyle("-fx-font-weight: bold; -fx-background-color: #007acc; -fx-text-fill: white; -fx-padding: 5px;");
            }

            // Remplir les lignes avec les données, sans afficher l'ID
            int rowIndex = 1;
            for (penalite penalite : penalites) {
                penaliteGridView.add(new Label(penalite.getType()), 0, rowIndex);       // Type
                penaliteGridView.add(new Label(String.valueOf(penalite.getSeuil_abs())), 1, rowIndex);
                penaliteGridView.add(new Label(String.valueOf(penalite.getCin())), 2, rowIndex);

                rowIndex++;
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }
    }
}
