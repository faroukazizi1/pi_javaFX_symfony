package Controller;

import Service.absenceService;
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
import java.time.LocalDate;
import java.util.List;

public class MainController {

    @FXML private TextField cinFieldAdd; // CIN pour l'ajout
    @FXML private TextField nbrAbsFieldAdd;
    @FXML private ComboBox<String> typeComboBoxAdd;
    @FXML private DatePicker datePickerAdd;

    @FXML private TextField cinFieldUpdate; // CIN pour la mise à jour
    @FXML private TextField nbrAbsFieldUpdateValue;
    @FXML private ComboBox<String> typeComboBoxUpdate;
    @FXML private DatePicker datePickerUpdate;

    @FXML private TextField cinFieldDelete; // CIN pour la suppression
    @FXML private TableView<absence> absenceTableView;
    @FXML private TableColumn<absence, Integer> idColumn;
    @FXML private TableColumn<absence, Integer> cinColumn;
    @FXML private TableColumn<absence, String> dateColumn;
    @FXML private TableColumn<absence, Integer> nbrAbsColumn;
    @FXML private TableColumn<absence, String> typeColumn;

    @FXML private GridPane absenceGridView;

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
            // Validation des champs
            validateFields(nbrAbsFieldAdd, datePickerAdd, typeComboBoxAdd);
            // Vérification que la date n'est pas dans le futur
            LocalDate selectedDate = datePickerAdd.getValue();
            if (selectedDate.isAfter(LocalDate.now())) {
                throw new Exception("La date sélectionnée ne peut pas être dans le futur.");
            }

            absence newAbsence = new absence(
                    0, // ID auto-incrémenté dans la base
                    Integer.parseInt(cinFieldAdd.getText()),  // cin
                    Date.valueOf(datePickerAdd.getValue()), // date
                    Integer.parseInt(nbrAbsFieldAdd.getText()), // nbr_abs
                    typeComboBoxAdd.getValue() // type
            );
            absenceService.add(newAbsence);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Absence ajoutée avec succès.");
            clearFields(nbrAbsFieldAdd, datePickerAdd, typeComboBoxAdd, cinFieldAdd);
            handleViewAbsences(); // Rafraîchir la liste
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }
    }

    @FXML
    public void handleUpdateAbsence() {
        try {
            // Validation des champs
            validateFields(nbrAbsFieldUpdateValue, datePickerUpdate, typeComboBoxUpdate);
            // Vérification que la date n'est pas dans le futur
            LocalDate selectedDate = datePickerAdd.getValue();
            if (selectedDate.isAfter(LocalDate.now())) {
                throw new Exception("La date sélectionnée ne peut pas être dans le futur.");
            }

            int cin = Integer.parseInt(cinFieldUpdate.getText());

            // Récupérer l'absence par CIN pour mise à jour
            absence existingAbsence = absenceService.getByCin(cin);
            if (existingAbsence != null) {
                existingAbsence.setNbr_abs(Integer.parseInt(nbrAbsFieldUpdateValue.getText()));
                existingAbsence.setDate(Date.valueOf(datePickerUpdate.getValue()));
                existingAbsence.setType(typeComboBoxUpdate.getValue());

                absenceService.update(existingAbsence);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Absence mise à jour avec succès.");
                clearFields(nbrAbsFieldUpdateValue, datePickerUpdate, typeComboBoxUpdate, cinFieldUpdate);
                handleViewAbsences(); // Rafraîchir la liste
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Aucune absence trouvée pour ce CIN.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }
    }

    @FXML
    public void handleDeleteAbsence() {
        try {
            if (cinFieldDelete.getText().isEmpty()) {
                throw new Exception("Veuillez entrer le CIN de l'absence à supprimer.");
            }
            int cin = Integer.parseInt(cinFieldDelete.getText());

            // Récupérer l'absence à supprimer via CIN
            absence absenceToDelete = absenceService.getByCin(cin);
            if (absenceToDelete != null) {
                absenceService.delete(absenceToDelete);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Absence supprimée avec succès.");
                cinFieldDelete.clear();
                handleViewAbsences(); // Rafraîchir la liste
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Aucune absence trouvée avec ce CIN.");
            }
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
            absenceGridView.add(new Label("CIN"), 0, 0);  // Cin à la première position
            absenceGridView.add(new Label("Date"), 1, 0);
            absenceGridView.add(new Label("Nombre d'absences"), 2, 0);
            absenceGridView.add(new Label("Type"), 3, 0);

            // Appliquer un style aux titres
            for (int i = 0; i < 4; i++) {  // Modifier de 5 à 4 car on n'affiche plus ID
                Label titleLabel = (Label) absenceGridView.getChildren().get(i);
                titleLabel.setStyle("-fx-font-weight: bold; -fx-background-color: #007acc; -fx-text-fill: white; -fx-padding: 5px;");
            }

            // Remplir les lignes avec les données, sans afficher l'ID
            int rowIndex = 1;
            for (absence absence : absences) {
                absenceGridView.add(new Label(String.valueOf(absence.getCin())), 0, rowIndex);       // CIN
                absenceGridView.add(new Label(new SimpleDateFormat("dd/MM/yyyy").format(absence.getDate())), 1, rowIndex);
                absenceGridView.add(new Label(String.valueOf(absence.getNbr_abs())), 2, rowIndex);
                absenceGridView.add(new Label(absence.getType()), 3, rowIndex);

                rowIndex++;
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }
    }


    private void validateFields(TextField nbrAbsField, DatePicker datePicker, ComboBox<String> typeComboBox) throws Exception {
        if (nbrAbsField.getText().isEmpty() || datePicker.getValue() == null || typeComboBox.getValue() == null) {
            throw new Exception("Veuillez remplir tous les champs.");
        }
    }

    private void clearFields(TextField nbrAbsField, DatePicker datePicker, ComboBox<String> typeComboBox, TextField cinField) {
        if (nbrAbsField != null) nbrAbsField.clear();
        if (datePicker != null) datePicker.setValue(null);
        if (typeComboBox != null) typeComboBox.getSelectionModel().clearSelection();
        if (cinField != null) cinField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
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
}