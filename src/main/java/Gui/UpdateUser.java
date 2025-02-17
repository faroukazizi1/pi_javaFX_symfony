package Gui;

import Model.user;
import Service.userService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class UpdateUser {

    @FXML
    private Label LBancienRole;

    @FXML
    private Label LBancienpassword;

    @FXML
    private TextField TFadresse;

    @FXML
    private TextField TFcin;

    @FXML
    private TextField TFemail;

    @FXML
    private TextField TFnom;

    @FXML
    private TextField TFnumero;

    @FXML
    private PasswordField TFpassword;

    @FXML
    private TextField TFprenom;

    @FXML
    private ComboBox<String> TFrole;

    @FXML
    private TextField TFusername;

    private user currentUser;
    private userService service = new userService();

    @FXML
    public void initialize() {
        TFrole.getItems().addAll("RHR", "Employe");  // Add roles to the ComboBox
    }

    public void initData(user selectedUser) {
        this.currentUser = selectedUser;

        TFnom.setText(selectedUser.getNom());
        TFprenom.setText(selectedUser.getPrenom());
        TFusername.setText(selectedUser.getUsername());
        TFemail.setText(selectedUser.getEmail());

        TFrole.setValue(selectedUser.getRole());
        LBancienRole.setText(selectedUser.getRole());

        TFadresse.setText(selectedUser.getAdresse());
        TFnumero.setText(String.valueOf(selectedUser.getNumero()));
        TFcin.setText(String.valueOf(selectedUser.getCin()));

        TFpassword.setText(selectedUser.getPassword());
        LBancienpassword.setText(selectedUser.getPassword());
    }

    @FXML
    private void handleUpdateUser() {
        // Récupérer les données des champs
        String nom = TFnom.getText();
        String prenom = TFprenom.getText();
        String username = TFusername.getText();
        String email = TFemail.getText();
        String role = TFrole.getValue();
        String adresse = TFadresse.getText();
        String password = TFpassword.getText();

        // Vérifier que les champs texte ne sont pas vides
        if (nom.isEmpty() || prenom.isEmpty() || username.isEmpty() || email.isEmpty() || adresse.isEmpty() || role == null || password.isEmpty()) {
            afficherAlerte("Erreur", "Champs manquants", "Tous les champs doivent être remplis.");
            return;
        }

        // Vérifier que le numéro de téléphone et le CIN sont des nombres valides
        int numero = 0, cin = 0;
        try {
            numero = Integer.parseInt(TFnumero.getText());
            cin = Integer.parseInt(TFcin.getText());
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "Numéro ou CIN invalide", "Le numéro de téléphone et le CIN doivent être des nombres valides.");
            return;
        }

        // Vérifier que l'email est valide
        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            afficherAlerte("Erreur", "Email invalide", "Veuillez entrer un email valide.");
            return;
        }

        // Mettre à jour l'utilisateur avec les nouvelles données
        currentUser.setNom(nom);
        currentUser.setPrenom(prenom);
        currentUser.setUsername(username);
        currentUser.setEmail(email);
        currentUser.setRole(role);
        currentUser.setAdresse(adresse);
        currentUser.setNumero(numero);
        currentUser.setCin(cin);
        currentUser.setPassword(password);

        try {
            // Appeler le service pour mettre à jour l'utilisateur dans la base de données
            service.update(currentUser);

            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Utilisateur mis à jour avec succès");
            alert.setContentText("Les informations de l'utilisateur ont été mises à jour.");
            alert.showAndWait();

            // Revenir à l'interface AfficherUser
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherUser.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) TFnom.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Échec de la mise à jour");
            alert.setContentText("Une erreur est survenue lors de la mise à jour de l'utilisateur.");
            alert.showAndWait();
        }
    }

    // Fonction pour afficher des alertes
    private void afficherAlerte(String titre, String header, String contenu) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(header);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

}
