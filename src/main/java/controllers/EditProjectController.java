package controllers;

import Util.Modals;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        String title = nom_projet.getText();
        String description = description_projet.getText();
        Date date_debut = Date.valueOf(date_debut_projet.getValue());
        Date date_fin = Date.valueOf(date_fin_projet.getValue());

        try{
            projectService.update(new Project(selectedProject.getId(), title, description,"en cours" , date_debut, date_fin ));
        }catch (Exception e){
            System.out.println(e.getMessage());
            Modals.displayError("Error occurred while updating project", "An error occurred while updating project");
        }

        edit_project_btn.getScene().getWindow().hide();
    }


}

