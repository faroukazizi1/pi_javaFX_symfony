package controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import models.Project;
import services.ProjectServices;

import java.sql.Date;
import java.time.LocalDate;

public class AddProject {
    ProjectServices projectService = new ProjectServices();
    @FXML
    public MFXButton add_project_btn;



    @FXML
    private MFXDatePicker date_debut_projet;

    @FXML
    private MFXDatePicker date_fin_projet;

    @FXML
    private MFXTextField description_projet;

    @FXML
    private MFXTextField nom_projet;

    @FXML
    private Label date_debut_projet_error;

    @FXML
    private Label date_fin_projet_error;

    @FXML
    private Label description_projet_error;

    @FXML
    private Label nom_projet_error;

    public void onClose() {
        add_project_btn.getScene().getWindow().hide();
    }


    public void onMinimize() {
    }

    public void addProject() {
        boolean isError = false;
        resetInputsErrors();
        if (nom_projet.getText().isEmpty()) {
            nom_projet_error.setText("This field is required");
            isError = true;
        }

        if (description_projet.getText().isEmpty()) {
            description_projet_error.setText("This field is required");
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

        if (isError)
            return;

        String titre = nom_projet.getText();
        String description = description_projet.getText();
        Date date_debut = Date.valueOf(date_debut_projet.getValue());
        Date date_fin = Date.valueOf(date_fin_projet.getValue());

        try {
            projectService.add(new Project(titre, description, "inactve", date_debut, date_fin));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            displayError("Error occurred while adding project", e.getMessage());
        }
        add_project_btn.getScene().getWindow().hide();
    }

    private void resetInputsErrors() {
        nom_projet_error.setText("");
        description_projet_error.setText("");
        date_debut_projet_error.setText("");
        date_fin_projet_error.setText("");
    }

    public void initialize() {

    }

    public static void displayError(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void displaySuccess(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
