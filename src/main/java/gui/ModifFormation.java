package gui;

import Model.Formation;
import Service.FormationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.time.format.DateTimeParseException;

public class ModifFormation {

    @FXML
    private TextField tfTitre;
    @FXML
    private TextField tfDescription;
    @FXML
    private TextField tfDateD;
    @FXML
    private TextField tfDateF;
    @FXML
    private TextField tfDuree;
    @FXML
    private TextField tfImage;
    @FXML
    private TextField tfIdFormateur;

    private Formation formation;
    private FormationService formationService = new FormationService();

    @FXML
    public void initialize() {
        // Initialisation des champs si nécessaire
    }

    // Méthode pour définir la formation à modifier
    public void setFormation(Formation formation) {
        this.formation = formation;
        tfTitre.setText(formation.getTitre());
        tfDescription.setText(formation.getDescription());
        tfDateD.setText(formation.getDate_D() != null ? formation.getDate_D().toString() : "");
        tfDateF.setText(formation.getDate_F() != null ? formation.getDate_F().toString() : "");
        tfDuree.setText(String.valueOf(formation.getDuree()));
        tfImage.setText(formation.getImage());
        tfIdFormateur.setText(String.valueOf(formation.getId_Formateur()));
    }

    @FXML
    private void modifierFormation() {
        try {
            // Mettre à jour la formation avec les nouvelles valeurs
            formation.setTitre(tfTitre.getText());
            formation.setDescription(tfDescription.getText());

            if (!tfDateD.getText().isEmpty()) {
                formation.setDate_D(Date.valueOf(tfDateD.getText()));
            }

            if (!tfDateF.getText().isEmpty()) {
                formation.setDate_F(Date.valueOf(tfDateF.getText()));
            }

            formation.setDuree(Integer.parseInt(tfDuree.getText()));
            formation.setImage(tfImage.getText());
            formation.setId_Formateur(Integer.parseInt(tfIdFormateur.getText()));

            // Appeler le service pour mettre à jour la formation dans la base de données
            formationService.update(formation);

            // Afficher une confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("La formation a été modifiée avec succès !");
            alert.showAndWait();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Format invalide", "Veuillez entrer des valeurs correctes pour la durée et l'ID du formateur.");
        } catch (DateTimeParseException e) {
            showAlert("Erreur", "Format de date invalide", "Veuillez entrer la date au format YYYY-MM-DD.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue", "Vérifiez vos informations et réessayez.");
        }
    }

    @FXML
    private void handleSave() {
        modifierFormation();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
