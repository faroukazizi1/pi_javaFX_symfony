package Gui;

import Model.user;
import Service.userService;
import javafx.fxml.FXML;
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

        // Set the current user data to the TextFields
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
        // Update current user with the new details entered
        currentUser.setNom(TFnom.getText());
        currentUser.setPrenom(TFprenom.getText());
        currentUser.setUsername(TFusername.getText());
        currentUser.setEmail(TFemail.getText());

        currentUser.setRole(TFrole.getValue());

        currentUser.setAdresse(TFadresse.getText());
        currentUser.setNumero(Integer.parseInt(TFnumero.getText()));
        currentUser.setCin(Integer.parseInt(TFcin.getText()));
        currentUser.setPassword(TFpassword.getText());

        try {
            // Call the service to update the employee in the database
            service.update(currentUser);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("User Updated Successfully");
            alert.setContentText("The user information has been successfully updated.");
            alert.showAndWait();

            // Close the UpdateUser window
            Stage stage = (Stage) TFnom.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Update Failed");
            alert.setContentText("An error occurred while updating the user.");
            alert.showAndWait();
        }
    }

}
