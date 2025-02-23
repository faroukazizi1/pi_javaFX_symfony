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

        if (nom_projet.getText().isEmpty()) {
            nom_projet_error.setText("This field is required");
            isError = true;
        }
        if (nom_projet.getText().matches("\\d+")) {
            nom_projet_error.setText("Project name cannot be only numbers.");
            isError = true;
        }

        if (description_projet.getText().isEmpty()) {
            description_projet_error.setText("This field is required");
            isError = true;
        }
        if (description_projet.getText().matches("\\d+")) {
            description_projet_error.setText("Description cannot be only numbers.");
            isError = true;
        }

        if (date_debut_projet.getValue() == null) {
            date_debut_projet_error.setText("This field is required");
            isError = true;
        }

        if (date_fin_projet.getValue() == null) {
            date_fin_projet_error.setText("This field is required");
            isError = true;
        }

        if (date_debut_projet.getValue() != null && date_fin_projet.getValue() != null) {
            if (date_fin_projet.getValue().isBefore(date_debut_projet.getValue())) {
                date_fin_projet_error.setText("This field should be after date debut");
                isError = true;
            }
        }
        // Si une erreur est trouvée, la fonction addProject() s'arrête.
        if (isError)
            return;

        //Récupération des valeurs des champs
        String titre = nom_projet.getText();
        String description = description_projet.getText();
        Date date_debut = Date.valueOf(date_debut_projet.getValue());
        Date date_fin = Date.valueOf(date_fin_projet.getValue());

        try {
            projectService.add(new Project(titre, description, "inactve", date_debut, date_fin));
            showAlert("Success", "Project added successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            showAlert("Error", "An error occurred while adding the project: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        add_project_btn.getScene().getWindow().hide();
    }
}
