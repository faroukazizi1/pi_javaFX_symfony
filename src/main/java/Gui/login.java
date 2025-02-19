package Gui;

import Model.user;
import Service.userService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class login {

    //button sign in
    @FXML
    private Button SignIn_Btn;

    //button forget password
    @FXML
    private Button SignIn_Btn1;

    //button sign up
    @FXML
    private Button SignIn_Btn2;


    @FXML
    private TextField tf_CheckEmailUser;

    @FXML
    private PasswordField tf_CheckPasswordUser;

    int sessionUserId;
    String sessionUsernom;
    String sessionUserPrenom;
    String sessionUserRole;

    userService u = new userService();


    @FXML
    void OnSignInClicked() {
        // Récupérer les données entrées par l'utilisateur
        String email = tf_CheckEmailUser.getText();
        String password = tf_CheckPasswordUser.getText();



        if ( u.authenticateUser(email, password)) {

            user utilisateur = u.HetUser(email);

            UserSession session = UserSession.getInstance(utilisateur.getId(), utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getRole());
            sessionUserId = session.getUserId();
            sessionUsernom = utilisateur.getNom();
            sessionUserPrenom = utilisateur.getPrenom();
            sessionUserRole = utilisateur.getRole();

            System.out.println("Connexion réussie : " + session.getUserName());
            System.out.println(sessionUserRole);
            redirigerSelonRole(session.getRole());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Échec de l'authentification");
            alert.setHeaderText(null);
            alert.setContentText("Email ou mot de passe incorrect !");
            alert.showAndWait();
        }
    }


    private void redirigerSelonRole(String role) {
        try {
            String view = "";
            if ("RHR".equals(role)) {
                view = "/AfficherUser.fxml"; // Page spécifique pour RHR
            } else if ("Employe".equals(role)) {
                view = "/AfficherPromotion.fxml"; // Page spécifique pour Employé
            }

            if (!view.isEmpty()) {
                // Charger la nouvelle interface
                FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
                Parent root = loader.load();
                Stage stage = (Stage) SignIn_Btn.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openRegistrationForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterUser.fxml"));
            Parent root = loader.load();

            AjouterUser controller = loader.getController();
            controller.initMode(true); // Active le mode inscription

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inscription Employé");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
