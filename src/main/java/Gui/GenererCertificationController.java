package Gui;

import Service.CertificationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class GenererCertificationController {

    @FXML
    private TextField txtUserId;

    @FXML
    private TextField txtFormationId;

    @FXML
    private Label lblMessage;

    private CertificationService certificationService = new CertificationService();

    @FXML
    void genererCertification() {
        try {
            int userId = Integer.parseInt(txtUserId.getText());
            int formationId = Integer.parseInt(txtFormationId.getText());

            // Récupérer le nom de l'utilisateur depuis la base de données
            String userName = getUserNameFromDb(userId);
            if (userName == null) {
                lblMessage.setText("Utilisateur non trouvé !");
                return;
            }

            // Générer la certification dans la BDD
            certificationService.genererCertification(userId, formationId);

            // Simuler récupération des données pour la formation
            // Récupérer les informations de la formation depuis la base de données
            String[] formationData = getFormationInfo(formationId);
            if (formationData == null) {
                lblMessage.setText("Formation non trouvée !");
                return;
            }

            String titreFormation = formationData[0];
            int dureeFormation = Integer.parseInt(formationData[1]);
            String nomFormateur = formationData[2];
            ; // Récupérer depuis la BDD

            // Générer le fichier PDF
            String certifPath = certificationService.genererCertificatPDF(userId, titreFormation, dureeFormation, nomFormateur);

            if (certifPath != null) {
                lblMessage.setText("Certification générée avec succès !\nTéléchargez-la ici : " + certifPath);
            } else {
                lblMessage.setText("Erreur lors de la génération.");
            }
        } catch (NumberFormatException e) {
            lblMessage.setText("Veuillez entrer des ID valides.");
        }
    }

    private String getUserNameFromDb(int userId) {
        String userName = null;
        String query = "SELECT nom FROM user WHERE id = ?";

        try (Connection conn = certificationService.getConn();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                userName = rs.getString("nom");
                System.out.println("Nom récupéré : " + userName);  // Debugging
            } else {
                System.out.println("Aucun utilisateur trouvé avec l'ID : " + userId);  // Debugging
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur de connexion à la base de données");  // Debugging
        }

        return userName;
    }


    @FXML
    void telechargerCertification() {
        String certifPath = lblMessage.getText(); // Récupérer le chemin du fichier à partir de l'étiquette
        try {
            File file = new File(certifPath);
            if (file.exists()) {
                Desktop.getDesktop().open(file); // Ouvre le fichier avec l'application par défaut
            } else {
                lblMessage.setText("Le fichier n'existe pas.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            lblMessage.setText("Erreur lors de l'ouverture du fichier.");
        }
    }
    private String[] getFormationInfo(int formationId) {
        String query = "SELECT f.Titre, f.Duree, formateur.Nom_F FROM formation f " +
                "JOIN formateur ON f.id_Formateur = formateur.id_formateur " +
                "WHERE f.id_form = ?";

        try (Connection conn = certificationService.getConn();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, formationId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new String[]{
                        rs.getString("Titre"),
                        String.valueOf(rs.getInt("Duree")),
                        rs.getString("Nom_F")
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @FXML
    private void handleBtnCertif(ActionEvent event) {
        try {
            // Charger la nouvelle interface FXML pour les certificats
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GenererCertification.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et mettre à jour son contenu
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'exception en affichant l'erreur dans la console
        }


    }@FXML
    private void handleAfficherFormation(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormation.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et changer de scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'erreur en affichant la trace
        }
    }
    @FXML
    private void handleAfficherStatistiques(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StatistiquesFormation.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleAfficherFormateur(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormateur.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et changer de scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'erreur en affichant la trace
        }
    }

}

