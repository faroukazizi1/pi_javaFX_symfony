package controllers;

import Util.KeyValuePair;
import Util.Modals;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import models.ProjectTask;
import models.User;
import services.ProjectTaskService;
import services.UserService;


import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AddTask {
    private int project_id;

    @FXML
    private MFXComboBox<KeyValuePair<Integer>> user_input;
    @FXML
    private Label user_error;

    @FXML
    private MFXTextField titre_input;
    @FXML
    private Label titre_error;

    @FXML
    private MFXTextField description_input;
    @FXML
    private Label description_error;

    @FXML
    private MFXDatePicker date_input;
    @FXML
    private Label date_error;

    @FXML
    private MFXButton add_task_btn;


    @FXML
    void addTask() {
        boolean isError = false;

        resetInputsErrors();

        if (titre_input.getText().isEmpty()) {
            titre_error.setText("Title is required");
            isError = true;
        }
        if (titre_input.getText().matches("\\d+")) {
            titre_error.setText("Invalid Input"+ "Project name cannot be only numbers.");
            return;
        }

        if (description_input.getText().isEmpty()) {
            description_error.setText("Description is required");
            isError = true;
        }

        if (date_input.getValue() == null) {
            date_error.setText("Date is required");
            isError = true;
        }

        if (date_input.getValue() != null && date_input.getValue().isBefore(LocalDate.now())) {
            date_error.setText("Date should be in the future");
            isError = true;
        }

        if (user_input.getSelectionModel().getSelectedItem() == null) {
            user_error.setText("User is required");
            isError = true;
        }

        if (isError) {
            return;
        }
        try {
            ProjectTaskService.create2(
                    new ProjectTask(
                            titre_input.getText(),
                            description_input.getText(),
                            Date.valueOf(date_input.getValue()),
                            ProjectTask.Statut.TODO,
                            project_id,
                            user_input.getSelectionModel().getSelectedItem().getActualValue()
                    )
            );
            add_task_btn.getScene().getWindow().hide();

            // Display success alert
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText("Task added successfully");
            successAlert.setContentText("Task has been added successfully.");
            successAlert.showAndWait();

        } catch (Exception e) {
            // Display error alert
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Failed to add task");
            errorAlert.setContentText("An error occurred while adding the task. Please try again.");
            errorAlert.showAndWait();

            System.err.println(e.getMessage());
        }
    }




    @FXML
    void onClose() {
        add_task_btn.getScene().getWindow().hide();
    }
    public void setProjectId(int project_id) {
        this.project_id = project_id;
    }

    public void initialize() throws SQLException {  ;
        List<User> users = UserService.getAll2();

        for (User user : users) {

            user_input.getItems().addAll(new KeyValuePair<>(user.getNom(), user.getId()));
        }
    }


    private void resetInputsErrors() {
        titre_error.setText("");
        description_error.setText("");
        date_error.setText("");
        user_error.setText("");
    }

    public void onMinimize(MouseEvent mouseEvent) {
    }
}

