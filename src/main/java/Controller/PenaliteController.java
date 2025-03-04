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
    private ComboBox<absence> absenceComboBoxAdd;
    @FXML
    private TextField seuilAbsFieldAdd, seuilAbsFieldUpdate, idFieldUpdate, idFieldDelete;
    @FXML
    private GridPane penaliteGridView;
    @FXML
    private TextField cinFieldUpdate, cinFieldDelete;

    private penaliteService penaliteService = new penaliteService();
    private absenceService absenceService = new absenceService();

    @FXML
    public void initialize() {
        loadTypeComboBoxes();
        loadAbsences();

        // Ajouter un gestionnaire d'événements pour la sélection de l'absence dans le ComboBox
        absenceComboBoxAdd.setOnAction(event -> handleAbsenceSelection());
    }

    // Charger les types de pénalité dans les ComboBox
    private void loadTypeComboBoxes() {
        List<String> types = List.of("Avertissement écrit", "Suspension temporaire", "Amende", "Démotion");
        typeComboBoxAdd.getItems().addAll(types);
        typeComboBoxUpdate.getItems().addAll(types);
    }

    // Charger les absences dans le ComboBox
    private void loadAbsences() {
        List<absence> absences = absenceService.getAll();
        absenceComboBoxAdd.getItems().addAll(absences);
    }

    // Ajouter une pénalité
    @FXML
    public void handleAddPenalite() {
        try {
            // Validation des champs
            if (typeComboBoxAdd.getValue() == null || seuilAbsFieldAdd.getText().isEmpty() || absenceComboBoxAdd.getValue() == null) {
                throw new Exception("Veuillez remplir tous les champs.");
            }

            // Création de l'objet pénalité
            absence selectedAbsence = absenceComboBoxAdd.getValue();
            int cin = selectedAbsence.getCin();  // Récupérer le CIN de l'absence sélectionnée
            String type = typeComboBoxAdd.getValue();
            int seuilAbs = Integer.parseInt(seuilAbsFieldAdd.getText());

            // Création de la pénalité avec le CIN de l'absence
            penalite newPenalite = new penalite(0, type, seuilAbs, cin);  // Utiliser le CIN de l'absence ici
            penaliteService.add(newPenalite); // Ajouter la pénalité à la base de données

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Pénalité ajoutée avec succès.");

            // Réinitialiser les champs
            resetFields();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout : " + e.getMessage());
        }
    }


    // Mettre à jour une pénalité
    @FXML
    public void handleUpdatePenalite() {
        try {
            if (typeComboBoxUpdate.getValue() == null || seuilAbsFieldUpdate.getText().isEmpty() || cinFieldUpdate.getText().isEmpty()) {
                throw new Exception("Veuillez remplir tous les champs.");
            }

            String type = typeComboBoxUpdate.getValue();
            int seuilAbs = Integer.parseInt(seuilAbsFieldUpdate.getText());
            int cin = Integer.parseInt(cinFieldUpdate.getText());

            penalite updatedPenalite = new penalite(type, seuilAbs, cin);
            penaliteService.update(updatedPenalite);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Pénalité mise à jour avec succès.");

            // Réinitialiser les champs
            resetFields();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    // Supprimer une pénalité
    @FXML
    public void handleDeletePenalite() {
        try {
            if (cinFieldDelete.getText().isEmpty()) {
                throw new Exception("Veuillez entrer un CIN.");
            }

            int cin = Integer.parseInt(cinFieldDelete.getText());

            penalite penaliteToDelete = new penalite(0, "", 0, cin);
            penaliteService.delete(penaliteToDelete);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Pénalité supprimée avec succès.");

            // Réinitialiser le champ CIN
            cinFieldDelete.clear();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression : " + e.getMessage());
        }
    }

    // Afficher toutes les pénalités
    @FXML
    public void handleViewPenalites() {
        try {
            List<penalite> penalites = penaliteService.getAll();
            penaliteGridView.getChildren().clear(); // Nettoyer les anciennes données

            // Titrer les colonnes
            penaliteGridView.add(new Label("Type"), 0, 0);
            penaliteGridView.add(new Label("Seuil"), 1, 0);
            penaliteGridView.add(new Label("CIN"), 2, 0);

            // Remplir les lignes avec les données
            int rowIndex = 1;
            for (penalite penalite : penalites) {
                penaliteGridView.add(new Label(penalite.getType()), 0, rowIndex);
                penaliteGridView.add(new Label(String.valueOf(penalite.getSeuil_abs())), 1, rowIndex);
                penaliteGridView.add(new Label(String.valueOf(penalite.getCin())), 2, rowIndex);
                rowIndex++;
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }
    }

    // Réinitialiser les champs du formulaire
    private void resetFields() {
        seuilAbsFieldAdd.clear();
        typeComboBoxAdd.getSelectionModel().clearSelection();
        absenceComboBoxAdd.getSelectionModel().clearSelection();
        seuilAbsFieldUpdate.clear();
        cinFieldUpdate.clear();
        typeComboBoxUpdate.getSelectionModel().clearSelection();
    }

    // Afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Méthode appelée lors de la sélection d'une absence dans la ComboBox
    @FXML
    private void handleAbsenceSelection() {
        try {
            absence selectedAbsence = absenceComboBoxAdd.getValue();
            if (selectedAbsence != null) {
                // Calculer le seuil d'absence (nombre d'absences * 4)
                int nombreAbsences = selectedAbsence.getNombreAbsences(); // Assure-toi que cette méthode existe dans la classe 'absence'
                int seuilAbsence = nombreAbsences / 2;

                // Mettre à jour le champ seuilAbsFieldAdd avec le résultat
                seuilAbsFieldAdd.setText(String.valueOf(seuilAbsence));
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour du seuil d'absence : " + e.getMessage());
        }
    }
}
