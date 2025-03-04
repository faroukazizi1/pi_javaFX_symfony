package Gui;

import Model.Formation;
import Service.FormationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import javafx.scene.image.ImageView;


public class AjouterFormation {

    @FXML
    private TextField txtTitre;

    @FXML
    private TextField txtDescription;

    @FXML
    private DatePicker dpDateD;

    @FXML
    private DatePicker dpDateF;

    @FXML
    private TextField txtDuree;

    @FXML
    private TextField txtImage;

    @FXML
    private TextField txtIdFormateur;

    @FXML
    private Button btnAjouter;
    @FXML
    private ImageView imagePreview;


    private FormationService formationService = new FormationService();

    @FXML
    void ajouterFormation(ActionEvent event) {
        try {
            String titre = txtTitre.getText();
            String description = txtDescription.getText();

            LocalDate dateDebut = dpDateD.getValue();
            LocalDate dateFin = dpDateF.getValue();

            // Vérification que la date de début n'est pas après la date de fin
            if (dateDebut != null && dateFin != null && dateDebut.isAfter(dateFin)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText(null);
                alert.setContentText("La date de début ne peut pas être après la date de fin.");
                alert.showAndWait();
                return;
            }

            // Vérification que la date de début n'est pas une date passée
            if (dateDebut != null && dateDebut.isBefore(LocalDate.now())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText(null);
                alert.setContentText("La date de début ne peut pas être une date passée.");
                alert.showAndWait();
                return;
            }

            Date dateD = Date.valueOf(dateDebut);
            Date dateF = Date.valueOf(dateFin);

            // Vérification de la durée
            String dureeText = txtDuree.getText();
            int duree = 0;
            try {
                duree = Integer.parseInt(dureeText);
                // Vérification si la durée dépasse 999
                if (duree < 0 || duree > 999) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText(null);
                alert.setContentText("La durée doit être un nombre entier entre 0 et 999.");
                alert.showAndWait();
                return;
            }

            String image = txtImage.getText();
            int idFormateur = Integer.parseInt(txtIdFormateur.getText());

            Formation formation = new Formation(titre, description, dateD, dateF, duree, image, idFormateur);
            formationService.add(formation);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Formation ajoutée avec succès!");
            alert.showAndWait();

            clearFields();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir des données valides.");
            alert.showAndWait();
        }
    }

    private void clearFields() {
        txtTitre.clear();
        txtDescription.clear();
        dpDateD.setValue(null);
        dpDateF.setValue(null);
        txtDuree.clear();
        txtImage.clear();
        txtIdFormateur.clear();
    }

    // New method to show the formation list screen
    @FXML
    void showFormationList(ActionEvent event) {
        try {
            // Load the Formation List screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormation.fxml"));
            Stage stage = (Stage) btnAjouter.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Impossible de charger la liste des formations.");
            alert.showAndWait();
        }
    }



}
