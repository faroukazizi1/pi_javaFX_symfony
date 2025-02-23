package controllers;

import Util.KeyValuePair;
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

public class AddTask {
    private int project_id;

    @FXML private MFXComboBox<KeyValuePair<Integer>> user_input;
    @FXML private Label user_error;

    @FXML private MFXTextField titre_input;
    @FXML private Label titre_error;

    @FXML private MFXTextField description_input;
    @FXML private Label description_error;

    @FXML private MFXDatePicker date_input;
    @FXML private Label date_error;

    @FXML private MFXButton add_task_btn;

    @FXML
    void onClose() {
        add_task_btn.getScene().getWindow().hide();
    }
    public void onMinimize(MouseEvent mouseEvent) {}
    public void initialize() throws SQLException {
        List<User> users = UserService.getAll2();
        for (User user : users) {
            user_input.getItems().addAll(new KeyValuePair<>(user.getNom(), user.getId()));
        }
    }
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
    private void resetInputsErrors() {
        titre_error.setText("");
        description_error.setText("");
        date_error.setText("");
        user_error.setText("");
    }



    @FXML
    void addTask() {
        boolean isError = false;
        resetInputsErrors();

        if (titre_input.getText().isEmpty()) {
            titre_error.setText("Title is required");
            isError = true;
        }
        if (titre_input.getText().matches("\\d+")) {
            titre_error.setText("Title cannot be only numbers.");
            isError = true;
        }

        if (description_input.getText().isEmpty()) {
            description_error.setText("Description is required");
            isError = true;
        }
        if (description_input.getText().matches("\\d+")) {
            description_error.setText("Description cannot be only numbers.");
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

        if (isError)
            return;

        try {
            ProjectTaskService.create(new ProjectTask(
                    titre_input.getText(),
                    description_input.getText(),
                    Date.valueOf(date_input.getValue()),
                    ProjectTask.Statut.TODO,
                    project_id,
                    user_input.getSelectionModel().getSelectedItem().getActualValue()
                    )
            );
            add_task_btn.getScene().getWindow().hide();
            showAlert("Success", "Task added successfully!", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            showAlert("Error", "An error occurred while adding the task: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void setProjectId(int project_id) {
        this.project_id = project_id;
    }
}

