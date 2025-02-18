package Controller;

import Service.PretService;
import Service.ReponseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import models.Pret;
import models.Reponse;

import java.io.IOException;
import java.util.List;

public class ReponseController {

    @FXML
    private ComboBox<Integer> pretIdComboBox;
    @FXML
    private Button generateButton;
    @FXML
    private TextField montantTextField;
    @FXML
    private TextField dureeTextField;
    @FXML
    private Label mensualiteLabel;

    private PretService pretService;
    private ReponseService reponseService;
    @FXML
    private Label tauxMensuelLabel;
    @FXML
    private Label revenusBrutsLabel;
    @FXML
    private Label potentielCreditLabel;
    @FXML
    private Label montantAutoriseLabel;
    @FXML
    private Label assuranceLabel;




    @FXML
    public void initialize() {
        pretService = new PretService();
        reponseService = new ReponseService(); // Initialize if needed

        loadPretIds();
    }

    private void loadPretIds() {
        List<Integer> pretIds = pretService.getAllPretIds();
        if (pretIds != null) {
            ObservableList<Integer> ids = FXCollections.observableArrayList(pretIds);
            pretIdComboBox.setItems(ids);
        } else {
            showAlert("Error", "Failed to load loan IDs.");
        }
    }

    @FXML
    private void generateReponse() {
        Integer selectedId = pretIdComboBox.getValue();
        if (selectedId == null) {
            showAlert("Error", "Please select a loan ID.");
            return;
        }

        Pret pret = pretService.getById(selectedId);
        if (pret == null) {
            showAlert("Error", "Loan not found.");
            return;
        }

        // Retrieve values from Pret
        double montantDemande = pret.getMontantPret();
        int duree = pret.getDureeRemboursement();

        // Set values in editable text fields
        montantTextField.setText(String.format("%.2f", montantDemande));
        dureeTextField.setText(String.valueOf(duree));

        // Financial Calculations
        double tauxInteret = 5.0;
        double tauxMensuel = tauxInteret / 12 / 100;
        double revenusBruts = montantDemande * 0.4;

        double mensualiteCredit = (tauxMensuel > 0) ?
                (montantDemande * tauxMensuel) / (1 - Math.pow(1 + tauxMensuel, -duree)) :
                montantDemande / duree;

        double potentielCredit = revenusBruts * 0.3;
        double montantAutorise = potentielCredit * 0.9;
        double assurance = montantDemande * 0.02;

        // Display calculated values in labels
        tauxMensuelLabel.setText(String.format("%.4f", tauxMensuel));
        revenusBrutsLabel.setText(String.format("%.2f", revenusBruts));
        mensualiteLabel.setText(String.format("%.2f", mensualiteCredit));
        potentielCreditLabel.setText(String.format("%.2f", potentielCredit));
        montantAutoriseLabel.setText(String.format("%.2f", montantAutorise));
        assuranceLabel.setText(String.format("%.2f", assurance));

        // Save the response to the database
        Reponse reponse = new Reponse();
        reponse.setDateReponse(java.sql.Date.valueOf(java.time.LocalDate.now()));
        reponse.setMontantDemande(montantDemande);
        reponse.setRevenusBruts(revenusBruts);
        reponse.setTauxInteret(tauxInteret);
        reponse.setMensualiteCredit(mensualiteCredit);
        reponse.setPotentielCredit(potentielCredit);
        reponse.setDureeRemboursement(duree);
        reponse.setMontantAutorise(montantAutorise);
        reponse.setAssurance(assurance);

        // Save using ReponseService
        boolean isSaved = reponseService.saveReponse(reponse);
        if (isSaved) {
            showAlert("Succès", "Réponse générée et enregistrée avec succès !");
        } else {
            showAlert("Erreur", "Erreur lors de l'enregistrement de la réponse.");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void acceptReponse() {
        showAlert("Succès", "Réponse acceptée !");
    }

    @FXML
    private void rejectReponse() {
        showAlert("Info", "Réponse rejetée !");
    }



}
