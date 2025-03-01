package Gui;

import Service.userService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

public class ResetPassword {

    @FXML
    private TextField TF_code, TF_newPassword;
    @FXML
    private Button btn_ResetPassword;

    private final userService userService = new userService();
    private int verificationCode;
    private int userId;

    // Récupère le code et l'ID utilisateur depuis ForgetPasswordController
    public void setVerificationCode(int code, int id) {
        this.verificationCode = code;
        this.userId = id;
    }

    @FXML
    private void resetPassword() {
        try {
            int enteredCode = Integer.parseInt(TF_code.getText().trim());
            String newPassword = TF_newPassword.getText().trim();

            if (enteredCode != verificationCode) {
                showAlert("Erreur", "Code incorrect !");
                return;
            }

            if (newPassword.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer un nouveau mot de passe.");
                return;
            }


            // Mettre à jour le mot de passe dans la base de données
            userService.modifyPassword(userId, newPassword);
            showAlert("Succès", "Mot de passe mis à jour avec succès.");

            // Fermer la fenêtre après succès
            btn_ResetPassword.getScene().getWindow().hide();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un code valide.");
        }
        // Charger la scène de login après la réinitialisation du mot de passe
        try {
            // Charger le fichier FXML du login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène pour le login
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer la scène actuelle
            btn_ResetPassword.getScene().getWindow().hide();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page de connexion.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}
