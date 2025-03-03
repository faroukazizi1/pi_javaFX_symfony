package controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Project;
import services.ProjectServices;

import java.sql.Date;
import java.time.LocalDate;
//import java.time.LocalDate;

public class AddProject {

    ProjectServices projectService = new ProjectServices();

    @FXML private MFXTextField nom_projet;
    @FXML private Label nom_projet_error;

    @FXML private MFXTextField description_projet;
    @FXML private Label description_projet_error;

    @FXML private MFXDatePicker date_debut_projet;
    @FXML private Label date_debut_projet_error;

    @FXML private MFXDatePicker date_fin_projet;
    @FXML private Label date_fin_projet_error;

    @FXML public MFXButton add_project_btn;

    public void onClose() {
        add_project_btn.getScene().getWindow().hide();
    }
    public void onMinimize() { ((Stage) add_project_btn.getScene().getWindow()).setIconified(true);}

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void resetInputsErrors() {
        nom_projet_error.setText("");
        description_projet_error.setText("");
        date_debut_projet_error.setText("");
        date_fin_projet_error.setText("");
    }


    public void addProject() {
        boolean isError = false;
        resetInputsErrors();

        String titre = nom_projet.getText().trim();
        String description = description_projet.getText().trim();
        LocalDate dateDebut = date_debut_projet.getValue();
        LocalDate dateFin = date_fin_projet.getValue();

        // Vérifier si le champ est vide ou contient seulement des espaces
        if (titre.isEmpty()) {
            nom_projet_error.setText("This field is required");
            isError = true;
        } else if (titre.matches("\\d+")) { // Vérifier si c'est uniquement des chiffres
            nom_projet_error.setText("Project name cannot be only numbers.");
            isError = true;
        }

        if (description.isEmpty()) {
            description_projet_error.setText("This field is required");
            isError = true;
        } else if (description.matches("\\d+")) { // Vérifier si c'est uniquement des chiffres
            description_projet_error.setText("Description cannot be only numbers.");
            isError = true;
        }

        if (dateDebut == null) {
            date_debut_projet_error.setText("This field is required");
            isError = true;
        }

        if (dateFin == null) {
            date_fin_projet_error.setText("This field is required");
            isError = true;
        } else if (dateDebut != null && dateFin.isBefore(dateDebut)) {
            date_fin_projet_error.setText("End date should be after start date");
            isError = true;
        }

        // Stopper l'exécution si une erreur est détectée
        if (isError) return;

        // Conversion des dates pour l'insertion
        Date sqlDateDebut = Date.valueOf(dateDebut);
        Date sqlDateFin = Date.valueOf(dateFin);

        try {
            projectService.add(new Project(titre, description, "inactive", sqlDateDebut, sqlDateFin));
            showAlert("Success", "Project added successfully!", Alert.AlertType.INFORMATION);
            add_project_btn.getScene().getWindow().hide();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            showAlert("Error", "An error occurred while adding the project: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

}
