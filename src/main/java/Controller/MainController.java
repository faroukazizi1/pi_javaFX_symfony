package Controller;

import Service.absenceService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    public ImageView imageView;

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

    // Méthode pour sélectionner une image
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

            // Récupération des valeurs
            int cin = Integer.parseInt(cinFieldAdd.getText());
            Date date = Date.valueOf(datePickerAdd.getValue());
            int nbrAbs = Integer.parseInt(nbrAbsFieldAdd.getText());
            String type = typeComboBoxAdd.getValue();
            String imagePath = null; // Par défaut, aucune image

            System.out.println("Type d'absence sélectionné : " + type);

            // Cas où l'absence est non justifiée
            if ("Non justifiée".equals(type)) {
                imagePath = null; // S'assurer qu'aucune image n'est enregistrée
                System.out.println("Absence non justifiée : aucune image requise.");
            }

            // Cas où l'absence est justifiée → demander une image
            if ("Justifiée".equals(type)) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));

                Stage stage = (Stage) imageView.getScene().getWindow();
                File selectedFile = fileChooser.showOpenDialog(stage);

                if (selectedFile != null) {
                    Image image = new Image(selectedFile.toURI().toString());
                    imageView.setImage(image);
                    imagePath = selectedFile.getAbsolutePath();
                    System.out.println("Image sélectionnée : " + imagePath);
                } else {
                    throw new Exception("Vous devez sélectionner une image pour une absence justifiée.");
                }
            }

            // Création de l'objet Absence
            absence newAbsence = new absence(0, cin, date, nbrAbs, type, imagePath);
            System.out.println("Nouvelle absence créée : " + newAbsence);

            // Ajout à la base de données
            absenceService.add(newAbsence);

            // Afficher un message de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Absence ajoutée avec succès.");

            // Réinitialisation des champs
            clearFields(nbrAbsFieldAdd, datePickerAdd, typeComboBoxAdd, cinFieldAdd);
            imageView.setImage(null);
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
            LocalDate selectedDate = datePickerUpdate.getValue();
            if (selectedDate.isAfter(LocalDate.now())) {
                throw new Exception("La date sélectionnée ne peut pas être dans le futur.");
            }

            int cin = Integer.parseInt(cinFieldUpdate.getText());

            // Récupérer toutes les absences par CIN
            List<absence> existingAbsences = absenceService.getByCin(cin);
            if (!existingAbsences.isEmpty()) {
                // Prendre la première absence de la liste pour mise à jour
                absence existingAbsence = existingAbsences.get(0);  // Sélectionne la première absence trouvée

                existingAbsence.setNbr_abs(Integer.parseInt(nbrAbsFieldUpdateValue.getText()));
                existingAbsence.setDate(Date.valueOf(datePickerUpdate.getValue()));
                existingAbsence.setType(typeComboBoxUpdate.getValue());

                String imagePath = existingAbsence.getImagePath();  // Conserver le chemin de l'image actuel (ou mettre à jour si nécessaire)

                // Si le type devient "Justifiée", on permet la mise à jour de l'image
                if ("Justifiée".equals(typeComboBoxUpdate.getValue())) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

                    Stage stage = (Stage) imageView.getScene().getWindow();
                    File selectedFile = fileChooser.showOpenDialog(stage);

                    if (selectedFile != null) {
                        Image image = new Image(selectedFile.toURI().toString());
                        imageView.setImage(image);
                        imagePath = selectedFile.getAbsolutePath();  // Mettre à jour le chemin de l'image
                    }
                }

                // Mettre à jour l'absence dans la base de données
                absenceService.update(existingAbsence);  // Appel de la méthode avec uniquement l'objet absence
                // Passer l'image si nécessaire

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

            // Récupérer toutes les absences par CIN
            List<absence> absencesToDelete = absenceService.getByCin(cin);
            if (!absencesToDelete.isEmpty()) {
                // Supprimer la première absence trouvée (ou tu peux ajouter une logique pour sélectionner une absence spécifique)
                absence absenceToDelete = absencesToDelete.get(0);

                absenceService.delete(absenceToDelete);  // Appel à la méthode de suppression
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
            absenceGridView.add(new Label("Image"), 4, 0);  // Ajouter une colonne pour l'image

            // Appliquer un style aux titres
            for (int i = 0; i < 5; i++) {  // Modifier de 5 à 4 car on n'affiche plus ID
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

                // Ajouter l'image si le chemin est disponible
                if (absence.getImagePath() != null && !absence.getImagePath().isEmpty()) {
                    // Créer un ImageView pour afficher l'image
                    Image image = new Image(new File(absence.getImagePath()).toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(50);  // Ajuster la taille de l'image
                    imageView.setFitHeight(50); // Ajuster la taille de l'image
                    absenceGridView.add(imageView, 4, rowIndex);  // Ajouter l'image à la colonne 4
                } else {
                    absenceGridView.add(new Label("Aucune image"), 4, rowIndex); // Si pas d'image, afficher un label
                }

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
