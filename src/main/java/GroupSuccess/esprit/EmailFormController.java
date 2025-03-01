package GroupSuccess.esprit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailFormController {

    @FXML
    private Button btnSend;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextArea txtMsgArea;

    @FXML
    void sendBtnOnAction(ActionEvent event) {
        String recipientEmail = txtEmail.getText();
        if (recipientEmail.isEmpty() || txtMsgArea.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs !").show();
            return;
        }

        try {
            sendEmail(recipientEmail);
            new Alert(Alert.AlertType.INFORMATION, "Email envoyé avec succès !").show();
        } catch (MessagingException e) {
            Logger.getLogger(EmailFormController.class.getName()).log(Level.SEVERE, "Erreur lors de l'envoi de l'email", e);
            new Alert(Alert.AlertType.ERROR, "Échec de l'envoi de l'email. Vérifiez vos informations.").show();
        }
    }

    private void sendEmail(String recipientEmail) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myAccountEmail = "laribiaziz092@gmail.com";
        String password = "vedy qedu qkcs cwgb";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        Message message = prepareMessage(session, myAccountEmail, recipientEmail, txtMsgArea.getText());
        if (message != null) {
            Transport.send(message);
        } else {
            throw new MessagingException("Le message est null, impossible de l'envoyer.");
        }
    }

    private Message prepareMessage(Session session, String myAccountEmail, String recipientEmail, String msg) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail)); // Corrected

            message.setSubject("Message important"); // Fixed method name
            message.setText(msg);
            return message;
        } catch (MessagingException e) {
            Logger.getLogger(EmailFormController.class.getName()).log(Level.SEVERE, "Erreur de préparation du message", e);
            return null;
        }
    }
}
