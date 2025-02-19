package controllers;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Project;
import models.ProjectTask;
import models.User;
import services.ProjectServices;
import services.ProjectTaskService;
import services.UserService;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class UpdateTaskController {

    @FXML
    private TextField titreField;
    @FXML
    private Label titre_task_error;

    @FXML
    private MFXTextField descriptionField;
    @FXML
    private Label description_error;

    @FXML
    private MFXDatePicker dateField;
    @FXML
    private Label datefield_error;

    @FXML
    private TextField projectIdField;
    @FXML
    private TextField userIdField;
    @FXML private MFXComboBox<String> projectComboBox;
    @FXML private MFXComboBox<String> userComboBox;

    private ProjectServices x = new ProjectServices();
    private UserService y = new UserService();


    private ProjectTask selectedTask;

    // Method to pre-fill the task data
    public void fillTaskData(ProjectTask task) {
        this.selectedTask = task;

        titreField.setText(task.getTitre());
        descriptionField.setText(task.getDescription());
        dateField.setValue(task.getDate().toLocalDate()); // Convert java.sql.Date to LocalDate

        Project project = x.getProjectById(task.getProject_id());
        projectComboBox.selectItem(project.getTitre());
        User user = y.getUserById(task.getUser_test_id());
        userComboBox.selectItem(user.getNom());

    }
    private void resetInputsErrors() {
        titre_task_error.setText("");
        description_error.setText("");
        datefield_error.setText("");

    }
    // Method to handle the update when the "Update" button is clicked
    @FXML
    public void onUpdateTask() {
        boolean isError = false;
        resetInputsErrors();

        // Validate fields
        if (titreField.getText().isEmpty()) {
            titre_task_error.setText("Title is required");
            isError = true;
        }
        if (descriptionField.getText().isEmpty()) {
            description_error.setText("Description is required");
            isError = true;
        }
        if (dateField.getValue() == null) {
            datefield_error.setText("Date is required");
            isError = true;
        } else {
            // Check if the date is in the past
            if (dateField.getValue().isBefore(java.time.LocalDate.now())) {
                datefield_error.setText("The date cannot be in the past");
                isError = true;
            }
        }

        // Validate that the title is not only numbers
        if (titreField.getText().matches("\\d+")) {
            titre_task_error.setText("Invalid Input: The title should not be only numbers.");
            isError = true;
        }

        // If there are validation errors, stop execution
        if (isError) {
            return;
        }
        if (selectedTask != null) {
            try {
                // Update the task with the new values
                selectedTask.setTitre(titreField.getText());
                selectedTask.setDescription(descriptionField.getText());
                selectedTask.setDate(java.sql.Date.valueOf(dateField.getValue())); // Convert LocalDate to java.sql.Date
                selectedTask.setProject_id(x.getProjectIdByTitre(projectComboBox.getValue()));
                selectedTask.setUser_test_id(y.getUserIdByName(userComboBox.getValue()));

                // Update the task in the database
                ProjectTaskService taskService = new ProjectTaskService();
                taskService.update(selectedTask);

                // Show success alert
                showAlert("Success", "Task updated successfully!", Alert.AlertType.INFORMATION);

                // Close the update window (optional)
                Stage stage = (Stage) titreField.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error updating task: " + e.getMessage());

                // Show error alert
                showAlert("Error", "Error updating task: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }


    // Helper method to show alerts
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void initialize(){
        List<Project> projectNames = x.getAll(); // Get all projects
        for (Project project : projectNames) {
            projectComboBox.getItems().add(project.getTitre()); // Add project titles to the combo box
        }
        List<User> users = y.getAll(); // Get all users
        for (User user : users) {
            userComboBox.getItems().add(user.getNom()); // Add user names to the combo box
        }

    }

    public void onClose(MouseEvent mouseEvent) {
    }
}
