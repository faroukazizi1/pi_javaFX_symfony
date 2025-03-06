package Controller;

import Service.absenceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.absence;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

public class MainController {

    @FXML private TextField cinFieldAdd, nbrAbsFieldAdd, cinFieldUpdate, nbrAbsFieldUpdateValue, cinFieldDelete;
    @FXML private ComboBox<String> typeComboBoxAdd, typeComboBoxUpdate;
    @FXML private DatePicker datePickerAdd, datePickerUpdate;
    @FXML private TableView<absence> absenceTableView;
    @FXML private TableColumn<absence, Integer> idColumn, cinColumn, nbrAbsColumn;
    @FXML private TableColumn<absence, String> dateColumn, typeColumn;
    @FXML private GridPane absenceGridView;
    @FXML private ImageView imageView; // ImageView pour afficher l'image sélectionnée

    private final absenceService absenceService = new absenceService();
    private String selectedImagePath = null; // Stocke le chemin de l'image choisie

    @FXML
    public void initialize() {
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
            LocalDate selectedDate = datePickerAdd.getValue();
            if (selectedDate.isAfter(LocalDate.now())) {
                throw new Exception("La date sélectionnée ne peut pas être dans le futur.");
            }

            int cin = Integer.parseInt(cinFieldAdd.getText());
            String type = typeComboBoxAdd.getValue();
            int nbrAbs = Integer.parseInt(nbrAbsFieldAdd.getText());
            Date dateAbsence = Date.valueOf(datePickerAdd.getValue());

            String imagePath = null;
            if ("Justifiée".equalsIgnoreCase(type)) {
                imagePath = handleChooseImage();
                if (imagePath == null) {
                    throw new Exception("Une image est requise pour une absence justifiée.");
                }
            }

            absence newAbsence = (imagePath != null) ?
                    new absence(0, cin, dateAbsence, nbrAbs, type, imagePath) :
                    new absence(0, cin, dateAbsence, nbrAbs, type);

            absenceService.add(newAbsence);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Absence ajoutée avec succès.");

            // Réinitialiser les champs
            clearFields(nbrAbsFieldAdd, datePickerAdd, typeComboBoxAdd, cinFieldAdd);

            // Réinitialiser l'image si nécessaire
            imageView.setImage(null);  // Ou remettre une image par défaut si vous en avez une

            // Rafraîchir la vue des absences
            handleViewAbsences();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }
    }


    @FXML
    public String handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", ".png", ".jpg", ".jpeg", ".gif"));

        // Définir le répertoire de départ sur le bureau de l'utilisateur
        String userDesktopPath = System.getProperty("user.home") + "/Desktop";
        File desktopDir = new File(userDesktopPath);
        if (desktopDir.exists() && desktopDir.isDirectory()) {
            fileChooser.setInitialDirectory(desktopDir);
        }

        // Ouvrir la boîte de dialogue pour sélectionner une image
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String selectedImagePath = selectedFile.getAbsolutePath();
            Image image = new Image("file:" + selectedImagePath);
            imageView.setImage(image);
            return selectedImagePath;
        }
        return null;
    }

    @FXML
    public void handleUpdateAbsence() {
        try {
            validateFields(nbrAbsFieldUpdateValue, datePickerUpdate, typeComboBoxUpdate);
            LocalDate selectedDate = datePickerUpdate.getValue();
            if (selectedDate.isAfter(LocalDate.now())) {
                throw new Exception("La date sélectionnée ne peut pas être dans le futur.");
            }

            int cin = Integer.parseInt(cinFieldUpdate.getText());
            absence existingAbsence = absenceService.getByCin(cin);
            if (existingAbsence != null) {
                existingAbsence.setNbr_abs(Integer.parseInt(nbrAbsFieldUpdateValue.getText()));
                existingAbsence.setDate(Date.valueOf(datePickerUpdate.getValue()));
                existingAbsence.setType(typeComboBoxUpdate.getValue());

                if ("Justifiée".equalsIgnoreCase(existingAbsence.getType())) {
                    if (selectedImagePath != null) {
                        existingAbsence.setImage_path(selectedImagePath);
                    }
                } else {
                    existingAbsence.setImage_path(null);
                }

                absenceService.update(existingAbsence);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Absence mise à jour avec succès.");
                clearFields(nbrAbsFieldUpdateValue, datePickerUpdate, typeComboBoxUpdate, cinFieldUpdate);
                selectedImagePath = null;
                handleViewAbsences();
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

            absence absenceToDelete = absenceService.getByCin(cin);
            if (absenceToDelete != null) {
                absenceService.delete(absenceToDelete);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Absence supprimée avec succès.");
                cinFieldDelete.clear();
                handleViewAbsences();
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
            absenceGridView.getChildren().clear();

            absenceGridView.add(new Label("CIN"), 0, 0);
            absenceGridView.add(new Label("Date"), 1, 0);
            absenceGridView.add(new Label("Nombre d'absences"), 2, 0);
            absenceGridView.add(new Label("Type"), 3, 0);
            absenceGridView.add(new Label("Image"), 4, 0);

            for (int i = 0; i < 5; i++) {
                Label titleLabel = (Label) absenceGridView.getChildren().get(i);
                titleLabel.setStyle("-fx-font-weight: bold; -fx-background-color: #007acc; -fx-text-fill: white; -fx-padding: 5px;");
            }

            int rowIndex = 1;
            for (absence absence : absences) {
                absenceGridView.add(new Label(String.valueOf(absence.getCin())), 0, rowIndex);
                absenceGridView.add(new Label(new SimpleDateFormat("dd/MM/yyyy").format(absence.getDate())), 1, rowIndex);
                absenceGridView.add(new Label(String.valueOf(absence.getNbr_abs())), 2, rowIndex);
                absenceGridView.add(new Label(absence.getType()), 3, rowIndex);
                absenceGridView.add(new Label(absence.getImage_path() != null ? absence.getImage_path() : "Aucune image"), 4, rowIndex);
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

    public void onUserButtonClick(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherUser.fxml"));
            Parent root = loader.load();

            // Récupérer la fenêtre actuelle
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Définir la nouvelle scène sur le stage
            stage.setScene(new Scene(root));

            // Définir le titre de la fenêtre
            stage.setTitle("Gestion des Absences");

            // Afficher la nouvelle scène
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}