package controllers;

import Util.Modals;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import models.Project;
import services.ProjectServices;

import java.sql.Date;

public class EditProjectController {

    private ProjectServices projectService = new ProjectServices();
    private Project selectedProject = null;

    @FXML
    public MFXButton edit_project_btn;


    @FXML
    private MFXDatePicker date_debut_projet;

    @FXML
    private MFXDatePicker date_fin_projet;

    @FXML
    private MFXTextField description_projet;

    @FXML
    private MFXTextField nom_projet;

    public void initialize() {

    }

    public Project getSelectedProject() {
        return selectedProject;
    }

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

    public void onClose() {
        edit_project_btn.getScene().getWindow().hide();
    }


    public void onMinimize() {
    }

    public void editProject(ActionEvent actionEvent) {
        // Validate fields
        if (nom_projet.getText().isEmpty() || description_projet.getText().isEmpty() || date_debut_projet.getValue() == null || date_fin_projet.getValue() == null) {
            showAlert("Error", "Please fill in all fields.", Alert.AlertType.ERROR);
            return; // Exit the method if validation fails
        }

        // Validate that the project name is not only numbers
        if (nom_projet.getText().matches("\\d+")) {
            showAlert("Invalid Input", "Project name cannot be only numbers.", Alert.AlertType.WARNING);
            return;
        }

        // Check if the end date is after the start date
        if (date_debut_projet.getValue() != null && date_fin_projet.getValue() != null) {
            if (date_fin_projet.getValue().isBefore(date_debut_projet.getValue())) {
                showAlert("Invalid Date", "End date should be after the start date.", Alert.AlertType.WARNING);
                return;
            }
        }

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

