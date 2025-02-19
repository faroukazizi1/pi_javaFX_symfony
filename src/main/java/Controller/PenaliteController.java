package Controller;

import Service.penaliteService;
import Service.absenceService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import models.penalite;
import models.absence;

import java.util.List;

public class PenaliteController {

    @FXML
    private ComboBox<String> typeComboBoxAdd, typeComboBoxUpdate;
    @FXML
    private ComboBox<absence> absenceComboBoxAdd; // ComboBox pour sélectionner une absence
    @FXML
    private TextField seuilAbsFieldAdd, seuilAbsFieldUpdate, idFieldUpdate, idFieldDelete;
    @FXML
    private GridPane penaliteGridView;

    private penaliteService penaliteService = new penaliteService();
    private absenceService absenceService = new absenceService(); // Service pour récupérer les absences

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

    // Ajouter une pénalité
    // Ajouter une pénalité
    @FXML
    public void handleAddPenalite() {
        try {
            if (typeComboBoxAdd.getValue() == null || seuilAbsFieldAdd.getText().isEmpty() || absenceComboBoxAdd.getValue() == null) {
                throw new Exception("Veuillez remplir tous les champs.");
            }

            absence selectedAbsence = absenceComboBoxAdd.getValue(); // Récupérer l'absence sélectionnée
            int absenceId = selectedAbsence.getId_abs(); // Obtenir l'ID de l'absence
            String type = typeComboBoxAdd.getValue();
            int seuilAbs = Integer.parseInt(seuilAbsFieldAdd.getText());

            // Créer une nouvelle pénalité avec l'ID de l'absence
            penalite newPenalite = new penalite(0, type, seuilAbs, absenceId);
            penaliteService.add(newPenalite); // Ajouter la pénalité

            // Associer la pénalité à l'absence en appelant le service absence
            absenceService.applyPenaliteToAbsence(absenceId, newPenalite); // Cette méthode doit être implémentée

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Pénalité ajoutée et associée à l'absence avec succès.");

            // Réinitialisation des champs
            seuilAbsFieldAdd.clear();
            typeComboBoxAdd.getSelectionModel().clearSelection();
            absenceComboBoxAdd.getSelectionModel().clearSelection();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout : " + e.getMessage());
        }
    }


    // Mettre à jour une pénalité
    @FXML
    public void handleUpdatePenalite() {
        try {
            if (idFieldUpdate.getText().isEmpty() || typeComboBoxUpdate.getValue() == null || seuilAbsFieldUpdate.getText().isEmpty()) {
                throw new Exception("Veuillez remplir tous les champs.");
            }

            int idPenalite = Integer.parseInt(idFieldUpdate.getText());
            String type = typeComboBoxUpdate.getValue();
            int seuilAbs = Integer.parseInt(seuilAbsFieldUpdate.getText());

            penalite updatedPenalite = new penalite(idPenalite, type, seuilAbs, 0);
            penaliteService.update(updatedPenalite);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Pénalité mise à jour avec succès.");

            idFieldUpdate.clear();
            seuilAbsFieldUpdate.clear();
            typeComboBoxUpdate.getSelectionModel().clearSelection();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    // Supprimer une pénalité
    @FXML
    public void handleDeletePenalite() {
        try {
            if (idFieldDelete.getText().isEmpty()) {
                throw new Exception("Veuillez entrer l'ID de la pénalité à supprimer.");
            }

            int idPenalite = Integer.parseInt(idFieldDelete.getText());

            penaliteService.delete(new penalite(idPenalite, null, 0, 0));

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Pénalité supprimée avec succès.");
            idFieldDelete.clear();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression : " + e.getMessage());
        }
    }

    // Afficher toutes les pénalités
    @FXML
    public void handleViewPenalites() {
        List<penalite> penalites = penaliteService.getAll();
        penaliteGridView.getChildren().clear();

        penaliteGridView.add(new Label("ID"), 0, 0);
        penaliteGridView.add(new Label("Type"), 1, 0);
        penaliteGridView.add(new Label("Seuil"), 2, 0);
        penaliteGridView.add(new Label("ID Absence"), 3, 0);

        for (int i = 0; i < 4; i++) {
            Label titleLabel = (Label) penaliteGridView.getChildren().get(i);
            titleLabel.setStyle("-fx-font-weight: bold; -fx-background-color: #007acc; -fx-text-fill: white; -fx-padding: 5px;");
        }

        int rowIndex = 1;
        for (penalite penalite : penalites) {
            penaliteGridView.add(new Label(String.valueOf(penalite.getId_pen())), 0, rowIndex);
            penaliteGridView.add(new Label(penalite.getType()), 1, rowIndex);
            penaliteGridView.add(new Label(String.valueOf(penalite.getSeuil_abs())), 2, rowIndex);
            penaliteGridView.add(new Label(String.valueOf(penalite.getId_absence())), 3, rowIndex);

            rowIndex++;
        }
    }

    // Fonction pour afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
