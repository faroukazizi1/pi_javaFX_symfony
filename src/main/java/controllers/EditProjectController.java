package controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import models.Project;
import services.ProjectServices;
import javafx.event.ActionEvent;

import java.sql.Date;

public class EditProjectController {

    ProjectServices projectService = new ProjectServices();
    private Project selectedProject = null;

    @FXML private MFXTextField nom_projet;
    @FXML private Label nom_projet_error;

    @FXML private MFXTextField description_projet;
    @FXML private Label description_projet_error;

    @FXML private MFXDatePicker date_debut_projet;
    @FXML private Label date_debut_projet_error;

    @FXML private MFXDatePicker date_fin_projet;
    @FXML private Label date_fin_projet_error;

    @FXML public MFXButton edit_project_btn;

    public void onClose() { edit_project_btn.getScene().getWindow().hide(); }
    public void onMinimize() {}
    public void initialize() {}

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;

        if (selectedProject != null) {
            nom_projet.setText(selectedProject.getTitre());
            description_projet.setText(selectedProject.getDescription());
            date_debut_projet.setValue(selectedProject.getDate_debut().toLocalDate());
            date_fin_projet.setValue(selectedProject.getDate_fin().toLocalDate());
        }
    }

    private void resetInputsErrors() {
        nom_projet_error.setText("");
        description_projet_error.setText("");
        date_debut_projet_error.setText("");
        date_fin_projet_error.setText("");
    }


    public void editProject(ActionEvent actionEvent) {
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
                date_fin_projet_error.setText("This field should be after date debut.");
                isError = true;
            }
        }

        // If there are validation errors, stop execution
        if (isError)
            return;

        // Proceed with project update if validation passed
        String title = nom_projet.getText();
        String description = description_projet.getText();
        Date date_debut = Date.valueOf(date_debut_projet.getValue());
        Date date_fin = Date.valueOf(date_fin_projet.getValue());

        Project y = projectService.getProjectById(selectedProject.getId());

        try {
            projectService.update(new Project(selectedProject.getId(), title, description, y.getStatut(), date_debut, date_fin));

            // Show success alert
            showAlert("Success", "Project updated successfully!", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            System.out.println(e.getMessage());

            // Show error alert
            showAlert("Error", "An error occurred while updating the project: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        edit_project_btn.getScene().getWindow().hide();
    }
}

