package gui;

import Model.Formateur;
import Service.FormateurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AjouterFormateur {

    @FXML
    private TextField txtNumero;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtSpecialite;

    @FXML
    private Button btnAjouter;

    private FormateurService formateurService = new FormateurService();

    @FXML
    void ajouterFormateur(ActionEvent event) {
        try {
            int numero = Integer.parseInt(txtNumero.getText());
            String nom = txtNom.getText();
            String prenom = txtPrenom.getText();
            String email = txtEmail.getText();
            String specialite = txtSpecialite.getText();

            Formateur formateur = new Formateur(numero, nom, prenom, email, specialite);
            formateurService.add(formateur);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Formateur ajouté avec succès!");
            alert.showAndWait();

            clearFields();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir un numéro valide.");
            alert.showAndWait();
        }
    }

    private void clearFields() {
        txtNumero.clear();
        txtNom.clear();
        txtPrenom.clear();
        txtEmail.clear();
        txtSpecialite.clear();
    }
}
