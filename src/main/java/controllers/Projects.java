package controllers;

import GroupSuccess.esprit.Main;
import Util.KeyValuePair;
import Util.Modals;
import Util.TaskUpdateListener;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Project;
import models.ProjectTask;
import models.User;
import services.ProjectServices;
import services.ProjectTaskService;
import services.UserService;

import java.sql.Date;
import java.util.List;

public class Projects  implements TaskUpdateListener {
    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 700;

    UserService userService = new UserService();
    @FXML
    private Button Association_Btn, Event_Btn, Logout_Btn, Reclamation_Btn, User_Btn, Document_Btn;
    public Button participate_btn;
    public Button tasks_btn;
    ProjectServices projectService = new ProjectServices();
    ProjectTaskService projectTaskService = new ProjectTaskService();
    //ProjectParticipantService projectParticipantService = new ProjectParticipantService();
    //CitoyenService citoyenService = new CitoyenService();
    //UserService userService = new UserService();
    Project selectedProject = null;

    @FXML
    private MFXScrollPane done_tasks;

    @FXML
    private MFXScrollPane inprogress_tasks;

    @FXML
    private MFXScrollPane todo_tasks;

    @FXML
    private TableView<Project> Projects_Table;



    @FXML
    private TableColumn<Project, Date> date_debut_col, date_fin_col;

    @FXML
    private TableColumn<Project, String> title_col, description_col, statut_col;

    @FXML
    private Text numOfProjects, numOfActiveProjects, numOfInactiveProjects, statut_txt;

    @FXML
    private Button Home_Btn, statut_btn;

    @FXML
    private MFXComboBox<KeyValuePair<Integer>> user_select;

    String userRole;
    public void onHomeButtonClick(ActionEvent actionEvent) {
    }

    public void onUserButtonClick(ActionEvent actionEvent) {
    }

    public void onReclamationButtonClick(ActionEvent actionEvent) {
    }

    public void onDocumentsButtonClick(ActionEvent actionEvent) {
    }

    public void onEventButtonClick(ActionEvent actionEvent) {
    }

    public void onAssociationButtonClick(ActionEvent actionEvent) {
    }

    public void onLogoutButtonClick(ActionEvent actionEvent) {
    }

    @FXML
    void onOpenAddModal(ActionEvent event) throws Exception {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        Node source = (Node) event.getSource();
        Stage primaryStage = (Stage) source.getScene().getWindow();

        primaryStage.getScene().getRoot().setEffect(blur);

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/project/addProject.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.setScene(new Scene(root));

        stage.showAndWait();
        showProjects();
        primaryStage.getScene().getRoot().setEffect(null);
    }
    public void onOpenAddTacheModal(ActionEvent event) throws Exception {
        if (selectedProject == null) {
            Modals.displayError("Error occurred while adding task", "Please select a project to add task");
            return;
        }
        BoxBlur blur = new BoxBlur(3, 3, 3);
        Node source = (Node) event.getSource();
        Stage primaryStage = (Stage) source.getScene().getWindow();

        primaryStage.getScene().getRoot().setEffect(blur);

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/project/addTask.fxml"));
        Parent root = loader.load();
        AddTask addTaskController = loader.getController();
        addTaskController.setProjectId(selectedProject.getId());

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.setScene(new Scene(root));

        stage.showAndWait();
        showProjects();
        primaryStage.getScene().getRoot().setEffect(null);
    }

    public void openCalendar(ActionEvent actionEvent) {
    }

    @FXML
    void getProjectRow() {
        selectedProject = Projects_Table.getSelectionModel().getSelectedItem();
        if (statut_txt != null) {
            statut_txt.setText("Projet (" + selectedProject.getTitre() + "): " + selectedProject.getStatut());
            statut_btn.setDisable(false);
            statut_btn.setText(selectedProject.getStatut().equals("active") ? "Terminer" : "Activer");
        }
    }

    @FXML
    void onDeleteProject() throws Exception {
        // Check if a project is selected
        if (selectedProject == null) {
            Modals.displayError("Error occurred while deleting project", "Please select a project to delete");
            return;
        }

        // Show confirmation alert before deleting the project
        boolean confirmation = Modals.displayConfirmation(
                "Are you sure you want to delete this project?",
                "This action cannot be undone."
        );

        if (confirmation) {
            // Delete the project if the user confirms
            projectService.delete2(selectedProject.getId());

            // Show success alert after deletion
            Modals.displayInfo("Project Deleted", "The project has been deleted successfully.");

            // Refresh the project list after deletion
            showProjects();
        } else {
            // If user cancels, show cancellation message
            Modals.displayInfo("Delete Cancelled", "The project was not deleted.");
        }
    }


    @FXML
    public void showTasks() {
        System.out.println("show");
        VBox toDovBox = new VBox();
        toDovBox.setSpacing(10);
        toDovBox.setAlignment(Pos.CENTER);
        toDovBox.setPrefWidth(230);
        toDovBox.setMaxHeight(100);

        VBox inProgressvBox = new VBox();
        inProgressvBox.setSpacing(10);
        inProgressvBox.setAlignment(Pos.CENTER);
        inProgressvBox.setPrefWidth(230);
        inProgressvBox.setMaxHeight(100);

        VBox donevBox = new VBox();
        donevBox.setSpacing(10);
        donevBox.setAlignment(Pos.CENTER);
        donevBox.setPrefWidth(230);
        donevBox.setMaxHeight(100);

        try {
            List<ProjectTask> tasks = projectTaskService.getTasksByUser(user_select.getSelectionModel().getSelectedItem().getActualValue());

            for (ProjectTask task : tasks) {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/project/task.fxml"));
                Parent taskLoader = loader.load();
                TaskController taskController = loader.getController();

                taskController.setTask(task);
                taskController.setUpdateListener(this);
                switch (task.getStatut()) {
                    case TODO:
                        toDovBox.getChildren().add(taskLoader);
                        break;
                    case IN_PROGRESS:
                        inProgressvBox.getChildren().add(taskLoader);
                        break;
                    case DONE:
                        donevBox.getChildren().add(taskLoader);
                        break;
                }
            }

            todo_tasks.setContent(toDovBox);
            inprogress_tasks.setContent(inProgressvBox);
            done_tasks.setContent(donevBox);
        } catch (Exception e) {
            System.err.println("error: " + e.getMessage());
        }
    }

    public void changeStatut(ActionEvent actionEvent) {
    }

    public void initialize() throws Exception {
        List<User> users = userService.getAll();

        for (User user : users) {

            user_select.getItems().addAll(new KeyValuePair<>(user.getNom(), user.getId()));
        }
        showProjects();

    }
    @FXML
    void onUpdateProject(ActionEvent event) throws Exception {
        if (selectedProject == null) {
            Modals.displayError("Error occurred while updating project", "Please select a project to update");
            return;
        }

        BoxBlur blur = new BoxBlur(3, 3, 3);
        Node source = (Node) event.getSource();
        Stage primaryStage = (Stage) source.getScene().getWindow();

        primaryStage.getScene().getRoot().setEffect(blur);

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/project/editProject.fxml"));

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.setScene(new Scene(loader.load()));

        EditProjectController editProjectController = loader.getController();
        editProjectController.setSelectedProject(selectedProject);

        stage.showAndWait();
        showProjects();

        primaryStage.getScene().getRoot().setEffect(null);
    }


    public void showProjects() throws Exception {
        ObservableList<Project> projects = FXCollections.observableArrayList(ProjectServices.getAll2());

        Projects_Table.setItems(projects);
            title_col.setCellValueFactory(new PropertyValueFactory<>("titre"));
        date_debut_col.setCellValueFactory(new PropertyValueFactory<>("date_debut"));
        description_col.setCellValueFactory(new PropertyValueFactory<>("description"));
        date_fin_col.setCellValueFactory(new PropertyValueFactory<>("date_fin"));
        statut_col.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }
    @Override
    public void onTaskUpdated(ProjectTask task) {
        showTasks();
    }
}
