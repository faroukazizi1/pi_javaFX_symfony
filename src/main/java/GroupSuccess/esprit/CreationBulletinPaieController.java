package GroupSuccess.esprit;

import Services.BulletinPaieService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.BulletinPaie;

import java.math.BigDecimal;

public class CreationBulletinPaieController {

    @FXML
    private TextField employeIdField;
    @FXML
    private TextField moisField;
    @FXML
    private TextField anneeField;
    @FXML
    private TextField salaireBrutField;
    @FXML
    private TextField deductionsField;
    @FXML
    private Label salaireNetLabel;
    @FXML
    private Button submitButton;
    @FXML
    private Button retourButton;
    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        salaireBrutField.textProperty().addListener((observable, oldValue, newValue) -> calculerSalaireNet());
        deductionsField.textProperty().addListener((observable, oldValue, newValue) -> calculerSalaireNet());
    }

    @FXML
    private void createBulletin() {
        try {
            if (!isValidInteger(employeIdField.getText())) {
                showAlert("Erreur", "L'ID de l'employé doit être un entier valide.");
                return;
            }
            int employeId = Integer.parseInt(employeIdField.getText());

            if (!isValidInteger(moisField.getText())) {
                showAlert("Erreur", "Le mois doit être un nombre entre 1 et 12.");
                return;
            }
            int mois = Integer.parseInt(moisField.getText());
            if (mois < 1 || mois > 12) {
                showAlert("Erreur", "Le mois doit être compris entre 1 et 12.");
                return;
            }

            if (!isValidInteger(anneeField.getText())) {
                showAlert("Erreur", "L'année doit être un nombre entier valide.");
                return;
            }
            int annee = Integer.parseInt(anneeField.getText());
            if (annee < 1900 || annee > 2100) {
                showAlert("Erreur", "Veuillez saisir une année valide entre 1900 et 2100.");
                return;
            }

            if (!isValidBigDecimal(salaireBrutField.getText()) || !isValidBigDecimal(deductionsField.getText())) {
                showAlert("Erreur", "Les montants doivent être des nombres valides.");
                return;
            }

            BigDecimal salaireBrut = new BigDecimal(salaireBrutField.getText());
            BigDecimal deductions = new BigDecimal(deductionsField.getText());

            // Calcul du salaire net
            BigDecimal salaireNet = BulletinPaieService.calculerSalaireNet(salaireBrut, deductions);

            // Création du bulletin de paie
            BulletinPaie bulletin = new BulletinPaie();
            bulletin.setEmployeId(employeId);
            bulletin.setMois(String.valueOf(mois)); // Convertir le mois en String
            bulletin.setAnnee(annee);
            bulletin.setSalaireBrut(salaireBrut);
            bulletin.setDeductions(deductions);
            bulletin.setSalaireNet(salaireNet);

            int generatedId = BulletinPaieService.createBulletinPaie(bulletin);

            if (generatedId != -1) {
                showAlert("Succès", "✅ Le bulletin de paie a été enregistré avec succès !");
                clearFields();
            } else {
                showAlert("Erreur", "Une erreur s'est produite lors de la création du bulletin.");
            }
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur inattendue est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void clearFields() {
        employeIdField.clear();
        moisField.clear();
        anneeField.clear();
        salaireBrutField.clear();
        deductionsField.clear();
        salaireNetLabel.setText("0.00");
        statusLabel.setText("");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidBigDecimal(String value) {
        try {
            new BigDecimal(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void calculerSalaireNet() {
        try {
            BigDecimal salaireBrut = isValidBigDecimal(salaireBrutField.getText()) ? new BigDecimal(salaireBrutField.getText()) : BigDecimal.ZERO;
            BigDecimal deductions = isValidBigDecimal(deductionsField.getText()) ? new BigDecimal(deductionsField.getText()) : BigDecimal.ZERO;
            BigDecimal salaireNet = BulletinPaieService.calculerSalaireNet(salaireBrut, deductions);
            salaireNetLabel.setText(salaireNet.toString());
        } catch (Exception e) {
            salaireNetLabel.setText("Erreur");
        }
    }

}
