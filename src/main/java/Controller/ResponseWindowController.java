package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Reponse;

public class ResponseWindowController {

    @FXML private Label montantLabel;
    @FXML private Label dureeLabel;
    @FXML private Label tauxMensuelLabel;
    @FXML private Label revenusBrutsLabel;
    @FXML private Label mensualiteLabel;
    @FXML private Label potentielCreditLabel;
    @FXML private Label montantAutoriseLabel;
    @FXML private Label assuranceLabel;
    @FXML private Button acceptButton;
    @FXML private Button rejectButton;

    private Reponse reponse;

    // Method to set the Reponse data and update the UI
    public void setReponse(Reponse reponse) {
        this.reponse = reponse;
        updateUI();
    }

    private void updateUI() {
        if (reponse == null) {
            showAlert("Erreur", "Aucune réponse à afficher.");
            return;
        }

        montantLabel.setText(String.format("%.2f", reponse.getMontantDemande()));
        dureeLabel.setText(String.valueOf(reponse.getDureeRemboursement()));
        tauxMensuelLabel.setText(String.format("%.4f", reponse.getTauxMensuel()));
        revenusBrutsLabel.setText(String.format("%.2f", reponse.getRevenusBruts()));
        mensualiteLabel.setText(String.format("%.2f", reponse.getMensualiteCredit()));
        potentielCreditLabel.setText(String.format("%.2f", reponse.getPotentielCredit()));
        montantAutoriseLabel.setText(String.format("%.2f", reponse.getMontantAutorise()));
        assuranceLabel.setText(String.format("%.2f", reponse.getAssurance()));
    }

    @FXML
    private void acceptReponse() {
        showAlert("Succès", "Réponse acceptée !");
        closeWindow();
    }

    @FXML
    private void rejectReponse() {
        showAlert("Info", "Réponse rejetée !");
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) acceptButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}