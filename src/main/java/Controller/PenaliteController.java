package Controller;

import Service.penaliteService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.penalite;

import java.util.List;

public class PenaliteController {

    // Déclaration des éléments FXML
    @FXML
    private ComboBox<String> typeComboBoxAdd, typeComboBoxUpdate;
    @FXML
    private TextField seuilAbsFieldAdd, seuilAbsFieldUpdate, idFieldUpdate, idFieldDelete;
    @FXML
    private GridPane penaliteGridView;

    @FXML
    private penaliteService penaliteService = new penaliteService();

    @FXML
    public void initialize() {
        // Ajout des choix dans les ComboBox
        if (typeComboBoxAdd != null) {
            typeComboBoxAdd.getItems().addAll("Avertissement écrit", "Suspension temporaire", "Amende", "Démotion");
        }
        if (typeComboBoxUpdate != null) {
            typeComboBoxUpdate.getItems().addAll("Avertissement écrit", "Suspension temporaire", "Amende", "Démotion");
        }
    }

    // Ajouter une pénalité
    @FXML
    public void handleAddPenalite() {
        try {
            if (typeComboBoxAdd.getValue() == null || seuilAbsFieldAdd.getText().isEmpty()) {
                throw new Exception("Veuillez remplir tous les champs.");
            }

            String type = typeComboBoxAdd.getValue();
            int seuilAbs = Integer.parseInt(seuilAbsFieldAdd.getText());

            penalite newPenalite = new penalite(0, type, seuilAbs);
            penaliteService.add(newPenalite);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Pénalité ajoutée avec succès.");

            // Réinitialisation des champs
            seuilAbsFieldAdd.clear();
            typeComboBoxAdd.getSelectionModel().clearSelection();
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

            penalite updatedPenalite = new penalite(idPenalite, type, seuilAbs);
            penaliteService.update(updatedPenalite);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Pénalité mise à jour avec succès.");

            // Réinitialisation des champs
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

            penaliteService.delete(new penalite(idPenalite, null, 0));

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
        penaliteGridView.getChildren().clear(); // Nettoyer les anciennes données

        // Ajouter les titres des colonnes
        penaliteGridView.add(new Label("ID"), 0, 0);
        penaliteGridView.add(new Label("Type"), 1, 0);
        penaliteGridView.add(new Label("Seuil"), 2, 0);

        // Appliquer un style aux titres
        for (int i = 0; i < 3; i++) {
            Label titleLabel = (Label) penaliteGridView.getChildren().get(i);
            titleLabel.setStyle("-fx-font-weight: bold; -fx-background-color: #007acc; -fx-text-fill: white; -fx-padding: 5px;");
        }

        // Remplir les lignes avec les données
        int rowIndex = 1;
        for (penalite penalite : penalites) {
            penaliteGridView.add(new Label(String.valueOf(penalite.getId_pen())), 0, rowIndex);
            penaliteGridView.add(new Label(penalite.getType()), 1, rowIndex);
            penaliteGridView.add(new Label(String.valueOf(penalite.getSeuil_abs())), 2, rowIndex);

            rowIndex++;
        }
    }

    // Naviguer vers l'écran des absences
    @FXML
    public void handleNavigateToAbsence() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ajouterabsence.fxml"));
            Parent root = loader.load();


            // Récupérer la fenêtre via le GridPane
            Stage stage = (Stage) penaliteGridView.getScene().getWindow();
            if (stage == null) {
                throw new Exception("Fenêtre principale introuvable !");
            }

            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la navigation : " + e.getMessage());
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
