package Gui;

import Model.user;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import Service.userService;

import java.net.URL;
import java.util.ResourceBundle;

public class AjouterUser implements Initializable {

    private final userService userService = new userService();

    @FXML
    private TextField TFusername;

    @FXML
    private PasswordField TFpassword;

    @FXML
    private TextField TFemail;

    @FXML
    private TextField TFnom;

    @FXML
    private TextField TFprenom;

    @FXML
    private TextField TFnumero;

    @FXML
    private CheckBox TFhomme;

    @FXML
    private CheckBox TFfemme;

    @FXML
    private TextField TFcin;

    @FXML
    private ComboBox<String> TFrole;

    @FXML
    private TextField TFadresse;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add choices to the ChoiceBox
        TFrole.getItems().addAll("RHR", "Employe");
    }

    @FXML
    void ajouterUser(ActionEvent event) {

        String username = TFusername.getText();
        String password = TFpassword.getText();
        String email = TFemail.getText();
        String nom = TFnom.getText();
        String prenom = TFprenom.getText();
        int numero = Integer.parseInt(TFnumero.getText());
        int cin = Integer.parseInt(TFcin.getText());
        String role = TFrole.getValue();
        String adresse = TFadresse.getText();

        String sexe = "";
        if(TFhomme.isSelected()){
            sexe = "Homme";
            TFfemme.setSelected(false);
        }else if(TFfemme.isSelected()){
            sexe = "Femme";
            TFfemme.setSelected(false);
        }

        user u = new user(cin,nom,email,prenom,username,password,role,sexe,adresse,numero);

        try {
            userService.add(u);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }


}
