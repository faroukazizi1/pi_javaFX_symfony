package gui;

import Model.Formateur;
import Service.FormateurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ModifFormateur {

    @FXML
    private TextField tfNom;
    @FXML
    private TextField tfPrenom;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfNumero;
    @FXML
    private TextField tfSpecialite;

    private Formateur formateur;
    private FormateurService formateurService = new FormateurService();

    @FXML
    public void initialize() {
        // Initialisation des champs si nécessaire
    }

    // Méthode pour définir le formateur à modifier
    public void setFormateur(Formateur formateur) {
        this.formateur = formateur;
        tfNom.setText(formateur.getNom_F());
        tfPrenom.setText(formateur.getPrenom_F());
        tfEmail.setText(formateur.getEmail());
        tfNumero.setText(String.valueOf(formateur.getNumero()));
        tfSpecialite.setText(formateur.getSpecialite());
    }

    @FXML
    private void modifierFormateur() {
        // Mettre à jour le formateur avec les nouvelles valeurs
        formateur.setNom_F(tfNom.getText());
        formateur.setPrenom_F(tfPrenom.getText());
        formateur.setEmail(tfEmail.getText());
        formateur.setNumero(Integer.parseInt(tfNumero.getText()));
        formateur.setSpecialite(tfSpecialite.getText());

        // Appeler le service pour mettre à jour le formateur dans la base de données
        formateurService.update(formateur);

        // Afficher une confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setContentText("Le formateur a été modifié avec succès !");
        alert.showAndWait();
    }

    @FXML
    private void handleSave() {
        modifierFormateur();
    }
}
