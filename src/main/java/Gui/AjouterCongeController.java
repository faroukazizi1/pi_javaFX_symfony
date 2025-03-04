package Gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Model.TypeConge;
import Service.GestionConge;

import java.io.IOException;
import java.time.LocalDate;

public class AjouterCongeController {

    @FXML private ComboBox<TypeConge> typeCongeComboBox;
    @FXML private DatePicker debutDatePicker;
    @FXML private DatePicker finDatePicker;

    @FXML
    public void initialize() {

        if (typeCongeComboBox != null) {
            ObservableList<TypeConge> typesConges = FXCollections.observableArrayList(TypeConge.values());
            typeCongeComboBox.setItems(typesConges);
            typesConges.forEach(type -> System.out.println(type));
        }
    }

    @FXML
    public void handleValiderAction(ActionEvent event) {
        TypeConge selectedType = typeCongeComboBox.getValue();
        LocalDate dateDebut = debutDatePicker.getValue();
        LocalDate dateFin = finDatePicker.getValue();

        int employeId = 1;

        if (selectedType == null || dateDebut == null || dateFin == null) {
            showAlert(AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        if (dateDebut.isAfter(dateFin)) {
            showAlert(AlertType.ERROR, "Erreur", "La date de début doit être avant la date de fin.");
            return;
        }

        System.out.println("Type Congé: " + selectedType);
        System.out.println("Date Début: " + dateDebut);
        System.out.println("Date Fin: " + dateFin);

        boolean isAdded = GestionConge.ajouterConge(selectedType, dateDebut.toString(), dateFin.toString(), employeId);

        if (isAdded) {
            showAlert(AlertType.INFORMATION, "Succès", "Le congé a été ajouté avec succès.");
        } else {
            showAlert(AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du congé.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleAnnulerAction(ActionEvent event) {
        System.out.println("Annuler button clicked");
        typeCongeComboBox.setValue(null);
        debutDatePicker.setValue(null);
        finDatePicker.setValue(null);
    }

    @FXML
    public void handleAfficherAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DemandedeConge.fxml"));
            AnchorPane root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Demandes de Congé");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}