package Gui;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import Service.userService;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;




public class ForgetPassword {

    @FXML
    private TextField TF_email;
    @FXML
    private Button btnSendCode;

    private final userService userService = new userService();
    private int verificationCode;
    private static int userId; // Stocke l'ID de l'utilisateur pour l'étape suivante

    @FXML
    private void sendCode() {
        String email = TF_email.getText().trim();

        // Vérifier si l'email existe
        userId = userService.getUserIdByEmail(email);
        if (userId == -1) {
            showAlert("Erreur", "Cet email n'existe pas.");
            return;
        }

        // Générer un code de vérification aléatoire
        verificationCode = new Random().nextInt(900000) + 100000;

        // Envoyer le code par email
        boolean emailSent = sendEmail(email, "Réinitialisation du mot de passe",
                "Votre code de vérification est : " + verificationCode);

        if (emailSent) {
            showAlert("Succès", "Un code de vérification a été envoyé à votre email.");
            // Charger la scène ResetPassword
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResetPassword.fxml"));
                Parent root = loader.load();

                ResetPassword resetPasswordController = loader.getController();
                resetPasswordController.setVerificationCode(verificationCode, userId);

                // Créer une nouvelle scène
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

                // Fermer la scène actuelle (si nécessaire)
                btnSendCode.getScene().getWindow().hide();

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir la fenêtre de réinitialisation du mot de passe.");
            }
            // Ici, tu peux ouvrir la fenêtre ResetPassword
        } else {
            showAlert("Erreur", "Échec de l'envoi du code.");
        }

    }


    private boolean sendEmail(String recipientEmail, String subject, String messageText) {
        final String senderEmail = "azizifarouk2@gmail.com";  // Remplace par ton email
        final String senderPassword = "qlzi oruu ndwa svwn";   // Remplace par ton mot de passe

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(messageText);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fonction pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
