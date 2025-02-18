package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.ProjectTask;
import services.ProjectTaskService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Taskmanager {
    @FXML
private TableView<ProjectTask> taskTable;

    @FXML
    private TableColumn<ProjectTask, Integer> idColumn;
    @FXML
    private TableColumn<ProjectTask, String> titreColumn;
    @FXML
    private TableColumn<ProjectTask, String> descriptionColumn;
    @FXML
    private TableColumn<ProjectTask, String> dateColumn;
    @FXML
    private TableColumn<ProjectTask, String> statutColumn;

    @FXML
    private AnchorPane taskPane;

    private ProjectTaskService taskService = new ProjectTaskService();

    @FXML
    public void onOpenTask() {
        try {
            List<ProjectTask> tasks = taskService.readAll();
            populateTable(tasks);
            taskPane.setVisible(true); // Show the task pane when the button is clicked
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onUpdateTask(ActionEvent event) {
        ProjectTask selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            try {
                // Load the update FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/UpdateTask.fxml"));
                Parent root = loader.load();

                // Get the controller of the update page
                UpdateTaskController updateTaskController = loader.getController();

                // Pass the selected task to the update page
                updateTaskController.fillTaskData(selectedTask);

                // Create a new scene and show the update page
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No task selected.");
        }
    }


    @FXML
    public void onDeleteTask() {
        ProjectTask selectedTask = taskTable.getSelectionModel().getSelectedItem();

        // Check if a task is selected
        if (selectedTask != null) {
            try {
                // Delete the task from the database
                taskService.delete(selectedTask.getId());

                // Remove the task from the table view
                taskTable.getItems().remove(selectedTask);

                // Show success alert
                showAlert("Success", "Task '" + selectedTask.getTitre() + "' deleted successfully!", Alert.AlertType.INFORMATION);

                System.out.println("Deleted task: " + selectedTask.getTitre());
            } catch (SQLException e) {
                e.printStackTrace();

                // Show error alert
                showAlert("Error", "Error deleting task: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            // Show alert if no task is selected
            showAlert("No Selection", "Please select a task to delete.", Alert.AlertType.WARNING);
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


    private void populateTable(List<ProjectTask> tasks) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));

        taskTable.getItems().clear();
        taskTable.getItems().addAll(tasks);
    }

    public void showTasks(ActionEvent event) {
    }
    public void initialize(){
        List<ProjectTask> tasks = null;
        try {
            tasks = taskService.readAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        populateTable(tasks);


    }
}