package controllers;

import GroupSuccess.esprit.MainFormateurGUI;
import Gui.UserSession;
import Model.user;
import Service.userService;
import Util.KeyValuePair;
import Util.Modals;
import Util.TaskUpdateListener;
import com.sun.tools.javac.Main;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Project;
import models.ProjectTask;
import services.ProjectServices;
import services.ProjectTaskService;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class Projects  implements TaskUpdateListener {


    public Button showTaskButton;
    @FXML
    private Button Association_Btn, Event_Btn, Logout_Btn, Reclamation_Btn, User_Btn, Document_Btn;
    public Button participate_btn;
    public Button tasks_btn;

    userService userService = new userService();

    ProjectServices projectService = new ProjectServices();
    ProjectTaskService projectTaskService = new ProjectTaskService();

    Project selectedProject = null;

    @FXML private Text numOfProjects, numOfActiveProjects, numOfInactiveProjects, statut_txt;

    @FXML private MFXScrollPane todo_tasks;

    @FXML private MFXScrollPane done_tasks;

    @FXML private MFXScrollPane inprogress_tasks;

    @FXML private TableView<Project> Projects_Table;

    @FXML private TableColumn<Project, String> title_col, description_col, statut_col;

    @FXML private TableColumn<Project, Date> date_debut_col, date_fin_col;

    @FXML private Button Home_Btn, statut_btn;

    @FXML private MFXComboBox<KeyValuePair<Integer>> user_select;

    @FXML
    private HBox userSelectionContainer; // We'll create this in FXML
    @FXML
    private Text employeeNameText; // We'll create this in FXML

    @FXML
    private TabPane projectTabPane;

    public static final DataFormat TASK_FORMAT = new DataFormat("application/x-task");

    public void onHomeButtonClick(ActionEvent actionEvent) {
    }

    public void onUserButtonClick(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherUser.fxml"));
            Parent root = loader.load();

            // Récupérer la fenêtre actuelle
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Définir la nouvelle scène sur le stage
            stage.setScene(new Scene(root));

            // Définir le titre de la fenêtre
            stage.setTitle("Gestion des Absences");

            // Afficher la nouvelle scène
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onReclamationButtonClick(ActionEvent actionEvent) {
    }

    public void onDocumentsButtonClick(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));
            Parent root = loader.load();

            // Récupérer la fenêtre actuelle
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Définir la nouvelle scène sur le stage
            stage.setScene(new Scene(root));

            // Définir le titre de la fenêtre
            stage.setTitle("Gestion des Conges");

            // Afficher la nouvelle scène
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEventButtonClick(ActionEvent actionEvent) {
    }

    public void onLogoutButtonClick(ActionEvent actionEvent) {
    }
    // Méthode pour ouvrir la fenêtre d'ajout d'un projet
    @FXML
    void onOpenAddModal(ActionEvent event) throws Exception {
        // Application d'un effet de flou sur la fenêtre principale
        BoxBlur blur = new BoxBlur(3, 3, 3);
        Node source = (Node) event.getSource();
        Stage primaryStage = (Stage) source.getScene().getWindow();
        primaryStage.getScene().getRoot().setEffect(blur);

        FXMLLoader loader = new FXMLLoader(MainFormateurGUI.class.getResource("/project/addProject.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.setScene(new Scene(root));

        stage.showAndWait();
        showProjects();
        primaryStage.getScene().getRoot().setEffect(null);//retirer l'effet flou
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

        FXMLLoader loader = new FXMLLoader(MainFormateurGUI.class.getResource("/project/addTask.fxml"));
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
        //actualiser la liste affichée des projets dans la table.
       // numOfProjects.setText(String.valueOf(projectService.getAll().size()));
        primaryStage.getScene().getRoot().setEffect(null);
    }

    public void openCalendar()throws SQLException {
        CalendarController.openCalendar();
    }

    @FXML
    void getProjectRow() {
        selectedProject = Projects_Table.getSelectionModel().getSelectedItem();
        if (statut_txt != null) {
            statut_txt.setText("Projet (" + selectedProject.getTitre() + "): " + selectedProject.getStatut());
            statut_btn.setDisable(false);
            statut_btn.setText(selectedProject.getStatut().equals("active") ? "Disable" : "Active");
        }
    }
    @FXML
    void changeStatut() throws Exception {
        if (selectedProject == null) {
            Modals.displayError("Error occurred while changing statut", "Please select a project to change statut");
            return;
        }

        if (selectedProject.getStatut().equals("inactive")) {

            projectService.activateProject(selectedProject.getId());
        } else {
            projectService.deactivateProject(selectedProject.getId());
        }

        showProjects();

        selectedProject = null;
        statut_txt.setText("");
        statut_btn.setText("Statut");
        statut_btn.setDisable(true);
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
            selectedProject=null;
        } else {
            // If user cancels, show cancellation message
            Modals.displayInfo("Delete Cancelled", "The project was not deleted.");
        }
    }

    public void initialize() throws Exception {
        UserSession session = UserSession.getInstance();
        String userRole = session.getRole();
        String userName = session.getUserName();
        int userId = session.getUserId();

        // Set up UI components that need to be modified based on user role
        setupUserInterface(userRole, userName, userId);

        // Display the number of projects
        numOfProjects.setText(String.valueOf(projectService.getAll().size()));

        // Load projects if the user is allowed to view them
        if ("RHR".equals(userRole)) {
            showProjects();
        }
    }

    private void setupUserInterface(String userRole, String userName, int userId) {
        // Ensure the FXML is fully loaded before making UI changes
        Platform.runLater(() -> {
            if ("RHR".equals(userRole)) {
                // RHR users see the Projects tab
                projectTabPane.getSelectionModel().select(0);
               // projectTabPane.getTabs().get(1).setDisable(true); // Disable Tasks tab for RHR

                // RHR can see the user selection dropdown
                userSelectionContainer.setVisible(true);
                employeeNameText.setVisible(false);

                // Populate the user dropdown for RHR
                populateUserDropdown();
            } else {
                // Other users (employees) see the Tasks tab
                projectTabPane.getSelectionModel().select(1);
                projectTabPane.getTabs().get(0).setDisable(true); // Disable Projects tab for employees
                showTaskButton.setDisable(true); // Désactiver show all tasks pour les employés

                // Employees only see their own tasks - hide dropdown, show name
                userSelectionContainer.setVisible(false);
                employeeNameText.setVisible(true);
                employeeNameText.setText("Tasks for: " + userName);

                // Load tasks for the current employee automatically
                loadTasksForEmployee(userId);
            }
        });
    }
    private void populateUserDropdown() {
        try {
            // Populate the user dropdown
            List<user> users = userService.getAll();
            user_select.getItems().clear();

            for (user user : users) {
                user_select.getItems().add(new KeyValuePair<>(user.getNom(), user.getId()));
            }
        } catch (Exception e) {
            System.err.println("Error loading users: " + e.getMessage());
            Modals.displayError("Error", "Failed to load users: " + e.getMessage());
        }
    }
    private void loadTasksForEmployee(int userId) {
        try {
            // Simulate the selection in the background
            KeyValuePair<Integer> userPair = new KeyValuePair<>("", userId);
            user_select.setValue(userPair);

            // Show the tasks for this employee
            showTasksForUser(userId);
        } catch (Exception e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            Modals.displayError("Error", "Failed to load tasks: " + e.getMessage());
        }
    }
    private void showTasksForUser(int userId) {
        VBox toDovBox = new VBox();
        toDovBox.setSpacing(10);
        toDovBox.setAlignment(Pos.CENTER);
        toDovBox.setPrefWidth(230);
        toDovBox.setMinHeight(300);

        VBox inProgressvBox = new VBox();
        inProgressvBox.setSpacing(10);
        inProgressvBox.setAlignment(Pos.CENTER);
        inProgressvBox.setPrefWidth(230);
        inProgressvBox.setMinHeight(300);

        VBox donevBox = new VBox();
        donevBox.setSpacing(10);
        donevBox.setAlignment(Pos.CENTER);
        donevBox.setPrefWidth(230);
        donevBox.setMinHeight(300);

        try {
            List<ProjectTask> tasks = projectTaskService.getTasksByUser(userId);

            for (ProjectTask task : tasks) {
                FXMLLoader loader = new FXMLLoader(MainFormateurGUI.class.getResource("/project/task.fxml"));
                Parent taskLoader = loader.load();
                TaskController taskController = loader.getController();

                taskController.setTask(task);
                taskController.setUpdateListener(this);

                // Add the task to the appropriate container
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

            // Add placeholder messages for empty columns
            if (toDovBox.getChildren().isEmpty()) {
                Label placeholder = new Label("Drop tasks here");
                placeholder.setStyle("-fx-text-fill: #888888; -fx-font-style: italic;");
                toDovBox.getChildren().add(placeholder);
            }

            if (inProgressvBox.getChildren().isEmpty()) {
                Label placeholder = new Label("Drop tasks here");
                placeholder.setStyle("-fx-text-fill: #888888; -fx-font-style: italic;");
                inProgressvBox.getChildren().add(placeholder);
            }

            if (donevBox.getChildren().isEmpty()) {
                Label placeholder = new Label("Drop tasks here");
                placeholder.setStyle("-fx-text-fill: #888888; -fx-font-style: italic;");
                donevBox.getChildren().add(placeholder);
            }

            todo_tasks.setContent(toDovBox);
            inprogress_tasks.setContent(inProgressvBox);
            done_tasks.setContent(donevBox);

            setupDragAndDropForColumn(todo_tasks, ProjectTask.Statut.TODO);
            setupDragAndDropForColumn(inprogress_tasks, ProjectTask.Statut.IN_PROGRESS);
            setupDragAndDropForColumn(done_tasks, ProjectTask.Statut.DONE);

        } catch (Exception e) {
            System.err.println("error: " + e.getMessage());
            e.printStackTrace();
        }
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

        FXMLLoader loader = new FXMLLoader(MainFormateurGUI.class.getResource("/project/editProject.fxml"));

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
        selectedProject=null;
    }


    public void showProjects() throws Exception {
        ObservableList<Project> projects = FXCollections.observableArrayList(ProjectServices.getAll2());

        Projects_Table.setItems(projects);
        title_col.setCellValueFactory(new PropertyValueFactory<>("titre"));
        date_debut_col.setCellValueFactory(new PropertyValueFactory<>("date_debut"));
        description_col.setCellValueFactory(new PropertyValueFactory<>("description"));
        date_fin_col.setCellValueFactory(new PropertyValueFactory<>("date_fin"));
        statut_col.setCellValueFactory(new PropertyValueFactory<>("statut"));
        // Mise à jour du nombre de projets
        numOfProjects.setText(String.valueOf(projects.size()));
        numOfActiveProjects.setText(String.valueOf(projectService.readActiveProjects().size()));
        numOfInactiveProjects.setText(String.valueOf(projectService.readInactiveProjects().size()));
    }
    @Override
    public void onTaskUpdated(ProjectTask task) {
        showTasks();
    }

    // Ouvrir la fenêtre de gestion des tâches
    public void onOpenTask(ActionEvent event) {
        try {
            // Load the tasks window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/taskmanager.fxml"));
            Region root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Task List");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setupDragAndDropForColumn(MFXScrollPane scrollPane, ProjectTask.Statut targetStatus) {
        // Get or create the VBox content
        VBox content;
        if (scrollPane.getContent() instanceof VBox) {
            content = (VBox) scrollPane.getContent();
        } else {
            // If for some reason the content is not a VBox, create one
            content = new VBox();
            content.setSpacing(10);
            content.setAlignment(Pos.CENTER);
            content.setPrefWidth(230);
            scrollPane.setContent(content);
        }

        // First, make the ScrollPane itself a drop target
        scrollPane.setOnDragOver(event -> {
            if (event.getDragboard().hasContent(TASK_FORMAT)) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        scrollPane.setOnDragEntered(event -> {
            if (event.getDragboard().hasContent(TASK_FORMAT)) {
                scrollPane.setStyle("-fx-background-color: rgba(0, 191, 255, 0.1); -fx-border-color: #00BFFF; -fx-border-width: 2; -fx-border-style: dashed;");
            }
            event.consume();
        });

        scrollPane.setOnDragExited(event -> {
            scrollPane.setStyle("");
            event.consume();
        });

        scrollPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasContent(TASK_FORMAT)) {
                Integer taskId = (Integer) db.getContent(TASK_FORMAT);
                try {
                    projectTaskService.updateTaskStatus(taskId, targetStatus);
                    // Instead of calling showTasks(), call refreshTasksDisplay()
                    refreshTasksDisplay();
                    success = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    Modals.displayError("Error", "Failed to update task status: " + e.getMessage());
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });

        // Then also set up the VBox content as a drop target (for completeness)
        content.setOnDragOver(event -> {
            if (event.getDragboard().hasContent(TASK_FORMAT)) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        content.setOnDragEntered(event -> {
            if (event.getDragboard().hasContent(TASK_FORMAT)) {
                content.setStyle("-fx-background-color: rgba(0, 191, 255, 0.1); -fx-border-color: #00BFFF; -fx-border-width: 2; -fx-border-style: dashed;");
            }
            event.consume();
        });

        content.setOnDragExited(event -> {
            content.setStyle("");
            event.consume();
        });

        content.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasContent(TASK_FORMAT)) {
                Integer taskId = (Integer) db.getContent(TASK_FORMAT);
                try {
                    projectTaskService.updateTaskStatus(taskId, targetStatus);
                    // Instead of calling showTasks(), call refreshTasksDisplay()
                    refreshTasksDisplay();
                    success = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    Modals.displayError("Error", "Failed to update task status: " + e.getMessage());
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    // Add this new method to handle refreshing the task display
    private void refreshTasksDisplay() {
        // Get the current user's role
        UserSession session = UserSession.getInstance();
        String userRole = session.getRole();

        if ("RHR".equals(userRole)) {
            // If RHR, use the selected user from combobox
            showTasks();
        } else {
            // If employee, use their own ID
            int userId = session.getUserId();
            showTasksForUser(userId);
        }
    }
    // Update the showTasks method to set up drag-and-drop for each column
    @FXML
    public void showTasks() {
        System.out.println("show");


        VBox toDovBox = new VBox();
        toDovBox.setSpacing(10);
        toDovBox.setAlignment(Pos.CENTER);
        toDovBox.setPrefWidth(230);
        toDovBox.setMinHeight(300);

        VBox inProgressvBox = new VBox();
        inProgressvBox.setSpacing(10);
        inProgressvBox.setAlignment(Pos.CENTER);
        inProgressvBox.setPrefWidth(230);
        inProgressvBox.setMinHeight(300);

        VBox donevBox = new VBox();
        donevBox.setSpacing(10);
        donevBox.setAlignment(Pos.CENTER);
        donevBox.setPrefWidth(230);
        donevBox.setMinHeight(300);

        try {

            KeyValuePair<Integer> selectedUser = user_select.getSelectionModel().getSelectedItem();
            if (selectedUser == null) {
                Modals.displayInfo("No User Selected", "Please select a user to view their tasks.");
                return;
            }

            List<ProjectTask> tasks = projectTaskService.getTasksByUser(selectedUser.getActualValue());

            for (ProjectTask task : tasks) {
                FXMLLoader loader = new FXMLLoader(MainFormateurGUI.class.getResource("/project/task.fxml"));
                Parent taskLoader = loader.load();
                TaskController taskController = loader.getController();

                taskController.setTask(task);
                taskController.setUpdateListener(this);

                // Ajoute la tâche au conteneur approprié
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

            // Add placeholder messages for empty columns
            if (toDovBox.getChildren().isEmpty()) {
                Label placeholder = new Label("Drop tasks here");
                placeholder.setStyle("-fx-text-fill: #888888; -fx-font-style: italic;");
                toDovBox.getChildren().add(placeholder);
            }

            if (inProgressvBox.getChildren().isEmpty()) {
                Label placeholder = new Label("Drop tasks here");
                placeholder.setStyle("-fx-text-fill: #888888; -fx-font-style: italic;");
                inProgressvBox.getChildren().add(placeholder);
            }

            if (donevBox.getChildren().isEmpty()) {
                Label placeholder = new Label("Drop tasks here");
                placeholder.setStyle("-fx-text-fill: #888888; -fx-font-style: italic;");
                donevBox.getChildren().add(placeholder);
            }

            todo_tasks.setContent(toDovBox);
            inprogress_tasks.setContent(inProgressvBox);
            done_tasks.setContent(donevBox);

            setupDragAndDropForColumn(todo_tasks, ProjectTask.Statut.TODO);
            setupDragAndDropForColumn(inprogress_tasks, ProjectTask.Statut.IN_PROGRESS);
            setupDragAndDropForColumn(done_tasks, ProjectTask.Statut.DONE);

        } catch (Exception e) {
            System.err.println("error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    }
