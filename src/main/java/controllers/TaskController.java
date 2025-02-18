package controllers;

import Util.TaskUpdateListener;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import models.ProjectTask;
import services.ProjectTaskService;

public class TaskController {
    private TaskUpdateListener updateListener;
    public Text task_title;
    public Text task_statut;
    public Text task_user;

    private ProjectTask task;
    private final ProjectTaskService projectTaskService = new ProjectTaskService();

    public void setTask(ProjectTask task) {
        this.task = task;
        task_title.setText(task.getTitre());
        task_statut.setText(task.getStatut().toString());
    }

    public void setUpdateListener(TaskUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public ProjectTask getTask() {
        return task;
    }
    @FXML
    public void onTaskStatusChange(MouseEvent mouseEvent) {
        try {
            System.out.println("test");
            ProjectTask task = getTask();
            switch (task.getStatut()) {
                case TODO:
                    task.setStatut(ProjectTask.Statut.IN_PROGRESS);
                    break;
                case IN_PROGRESS:
                    task.setStatut(ProjectTask.Statut.DONE);
                    break;
                case DONE:
                    task.setStatut(ProjectTask.Statut.TODO);
                    break;
            }

            projectTaskService.update(task);
            if (updateListener != null) {
                updateListener.onTaskUpdated(task);
            }

        } catch (Exception e) {
            System.err.println("error: " + e.getMessage());
        }
    }
}