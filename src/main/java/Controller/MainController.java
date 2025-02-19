package Controller;

import Service.absenceService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.absence;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class MainController {

    @FXML private TextField nbrAbsFieldAdd;
    @FXML private ComboBox<String> typeComboBoxAdd;
    @FXML private DatePicker datePickerAdd;

    @FXML private TextField nbrAbsFieldUpdate;
    @FXML private TextField nbrAbsFieldUpdateValue;
    @FXML private ComboBox<String> typeComboBoxUpdate;
    @FXML private DatePicker datePickerUpdate;
    @FXML
    private GridPane absenceGridView;

    @FXML private TextField nbrAbsFieldDelete;
    @FXML private TableView<absence> absenceTableView;
    @FXML private TableColumn<absence, Integer> idColumn;
    @FXML private TableColumn<absence, String> dateColumn;
    @FXML private TableColumn<absence, Integer> nbrAbsColumn;
    @FXML private TableColumn<absence, String> typeColumn;

    private final absenceService absenceService = new absenceService();

    @FXML
    public void initialize() {
        System.out.println("Initialisation du contrôleur...");

        if (typeComboBoxAdd != null) {
            typeComboBoxAdd.getItems().addAll("Justifiée", "Non justifiée");
        }
        if (typeComboBoxUpdate != null) {
            typeComboBoxUpdate.getItems().addAll("Justifiée", "Non justifiée");
        }
    }

    @FXML
    public void handleAddAbsence() {
        try {
            validateFields(nbrAbsFieldAdd, datePickerAdd, typeComboBoxAdd);

            absence newAbsence = new absence(
                    0,
                    Date.valueOf(datePickerAdd.getValue()),
                    Integer.parseInt(nbrAbsFieldAdd.getText()),
                    typeComboBoxAdd.getValue()
            );
            absenceService.add(newAbsence);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Absence ajoutée avec succès.");
            clearFields(nbrAbsFieldAdd, datePickerAdd, typeComboBoxAdd);
            handleViewAbsences(); // Rafraîchir la liste
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }
    }

    @FXML
    public void handleUpdateAbsence() {
        try {
            validateFields(nbrAbsFieldUpdateValue, datePickerUpdate, typeComboBoxUpdate);

            int idAbsence = Integer.parseInt(nbrAbsFieldUpdate.getText());
            absence updatedAbsence = new absence(
                    idAbsence,
                    Date.valueOf(datePickerUpdate.getValue()),
                    Integer.parseInt(nbrAbsFieldUpdateValue.getText()),
                    typeComboBoxUpdate.getValue()
            );
            absenceService.update(updatedAbsence);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Absence mise à jour avec succès.");
            clearFields(nbrAbsFieldUpdate, datePickerUpdate, typeComboBoxUpdate);
            handleViewAbsences(); // Rafraîchir la liste
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }
    }

    @FXML
    public void handleDeleteAbsence() {
        try {
            if (nbrAbsFieldDelete.getText().isEmpty()) {
                throw new Exception("Veuillez entrer l'ID de l'absence à supprimer.");
            }
            int idAbsence = Integer.parseInt(nbrAbsFieldDelete.getText());
            absenceService.delete(new absence(idAbsence, null, 0, null));
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Absence supprimée avec succès.");
            nbrAbsFieldDelete.clear();
            handleViewAbsences(); // Rafraîchir la liste
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }
    }

    @FXML
    public void handleViewAbsences() {
        try {
            List<absence> absences = absenceService.getAll();
            absenceGridView.getChildren().clear(); // Nettoyer les anciennes données

            // Ajouter les titres des colonnes
            absenceGridView.add(new Label("ID"), 0, 0);
            absenceGridView.add(new Label("Date"), 1, 0);
            absenceGridView.add(new Label("Nombre d'absences"), 2, 0);
            absenceGridView.add(new Label("Type"), 3, 0);

            // Appliquer un style aux titres
            for (int i = 0; i < 4; i++) {
                Label titleLabel = (Label) absenceGridView.getChildren().get(i);
                titleLabel.setStyle("-fx-font-weight: bold; -fx-background-color: #007acc; -fx-text-fill: white; -fx-padding: 5px;");
            }

            // Remplir les lignes avec les données
            int rowIndex = 1;
            for (absence absence : absences) {
                absenceGridView.add(new Label(String.valueOf(absence.getId_abs())), 0, rowIndex);
                absenceGridView.add(new Label(new SimpleDateFormat("dd/MM/yyyy").format(absence.getDate())), 1, rowIndex);
                absenceGridView.add(new Label(String.valueOf(absence.getNbr_abs())), 2, rowIndex);
                absenceGridView.add(new Label(absence.getType()), 3, rowIndex);

                rowIndex++;
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }
    }

    @FXML
    public void handleNavigateToPenalites() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ajouterpenalite.fxml"));
            Parent root = loader.load();


            // Récupérer la fenêtre via le GridPane
            Stage stage = (Stage) absenceGridView.getScene().getWindow();
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

    private void validateFields(TextField nbrAbsField, DatePicker datePicker, ComboBox<String> typeComboBox) throws Exception {
        if (nbrAbsField.getText().isEmpty() || datePicker.getValue() == null || typeComboBox.getValue() == null) {
            throw new Exception("Veuillez remplir tous les champs.");
        }
    }

    private void clearFields(TextField nbrAbsField, DatePicker datePicker, ComboBox<String> typeComboBox) {
        if (nbrAbsField != null) nbrAbsField.clear();
        if (datePicker != null) datePicker.setValue(null);
        if (typeComboBox != null) typeComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
