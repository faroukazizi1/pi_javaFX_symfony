package controllers;

import Gui.UserSession;
import Util.TaskUpdateListener;
import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.ProjectTask;
import services.ProjectTaskService;

public class TaskController {
    private TaskUpdateListener updateListener;
    public Text task_title;
    public Text task_statut;
    public Text task_user;
    private ProjectTask task;
    @FXML
    private VBox taskRoot; // Root container for the task
    private final ProjectTaskService projectTaskService = new ProjectTaskService();

    public void setTask(ProjectTask task) {
        this.task = task;
        task_title.setText(task.getTitre());
        task_statut.setText(task.getStatut().toString());
    }

    @FXML
    public void initialize() {
        // Initialize drag source
        setupDragAndDrop();
    }

    private void setupDragAndDrop() {
        // Make task draggable
        taskRoot.setOnDragDetected(event -> {
            Dragboard db = taskRoot.startDragAndDrop(TransferMode.MOVE);

            ClipboardContent content = new ClipboardContent();
            content.put(Projects.TASK_FORMAT, task.getId());
            db.setContent(content);

            taskRoot.setOpacity(0.7);
            event.consume();
        });

        taskRoot.setOnDragDone(event -> {
            taskRoot.setOpacity(1.0); // Restore opacity

            if (event.getTransferMode() == TransferMode.MOVE) {}
            event.consume();
        });
    }

    public void setUpdateListener(TaskUpdateListener updateListener) {
        this.updateListener = updateListener;
    }
    public ProjectTask getTask() {
        return task;
    }

}