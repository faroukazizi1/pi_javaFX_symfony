package Gui;

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

    private final String senderEmail = "flexirh.equipe@gmail.com";  // Updated sender email

    @FXML
    void sendBtnOnAction(ActionEvent event) {
        String recipientEmail = txtEmail.getText();
        if (recipientEmail.isEmpty() || txtMsgArea.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs !").show();
            return;
        }

        try {
            sendEmail(senderEmail, recipientEmail, txtMsgArea.getText());
            new Alert(Alert.AlertType.INFORMATION, "Email envoyé avec succès !").show();
        } catch (MessagingException e) {
            Logger.getLogger(EmailFormController.class.getName()).log(Level.SEVERE, "Erreur lors de l'envoi de l'email", e);
            new Alert(Alert.AlertType.ERROR, "Échec de l'envoi de l'email. Vérifiez vos informations.").show();
        }
    }

    private void sendEmail(String senderEmail, String recipientEmail, String msg) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String password = "dflq fhuu gpsc rbzb";  // Sender email password

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        Message message = prepareMessage(session, senderEmail, recipientEmail, msg);
        if (message != null) {
            Transport.send(message);
        } else {
            throw new MessagingException("Le message est null, impossible de l'envoyer.");
        }
    }

    private Message prepareMessage(Session session, String senderEmail, String recipientEmail, String msg) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));  // Updated sender
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));  // Employee's email as recipient
            message.setSubject("Message important");
            message.setText(msg);
            return message;
        } catch (MessagingException e) {
            Logger.getLogger(EmailFormController.class.getName()).log(Level.SEVERE, "Erreur de préparation du message", e);
            return null;
        }
    }
}
