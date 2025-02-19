package gui;



import Model.Formation;
import Service.FormationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;

import java.sql.Date;

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

    private FormationService formationService = new FormationService();

    @FXML
    void ajouterFormation(ActionEvent event) {
        try {
            String titre = txtTitre.getText();
            String description = txtDescription.getText();
            Date dateD = Date.valueOf(dpDateD.getValue());
            Date dateF = Date.valueOf(dpDateF.getValue());
            int duree = Integer.parseInt(txtDuree.getText());
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
}
