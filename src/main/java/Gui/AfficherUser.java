package Gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import Model.user;
import Service.userService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Gui.UserSession;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.List;


public class AfficherUser {

    private final userService service = new userService();

    @FXML
    private TableColumn<user, String> Coladdresse;

    @FXML
    private TableColumn<user, Integer> Colcin;

    @FXML
    private TableColumn<user, String> Colemail;

    @FXML
    private TableColumn<user, String> Colnom;

    @FXML
    private TableColumn<user, Integer> Colnumero;

    @FXML
    private TableColumn<user, String> Colpassword;

    @FXML
    private TableColumn<user, String> Colprenom;

    @FXML
    private TableColumn<user, String> Colrole;

    @FXML
    private TableColumn<user, String> Colsexe;

    @FXML
    private TableColumn<user, String> Colusername;

    @FXML
    private TableView<user> tableView;

    @FXML
    private TableColumn<user, Void> Coldelete;

    @FXML
    private TableColumn<user, Void> Colupdate;

    @FXML
    private TableColumn<user, Integer> Colid;

    @FXML
    private Button button_ajouter;


    @FXML
    void initialize() {

        UserSession session = UserSession.getInstance();
        String userRole = session.getRole();

        if (!"RHR".equals(userRole)) { // Si ce n'est pas un Responsable RH
            tableView.setVisible(false); // Masquer la table des utilisateurs
            button_ajouter.setVisible(false);


            return;
        }

        // Initialisation normale si c'est un Responsable RH
        try {
            List<user> tab_users = service.getAll();
            ObservableList<user> observableList = FXCollections.observableList(tab_users);
            tableView.setItems(observableList);

            Colid.setVisible(false);
            Colid.setCellValueFactory(new PropertyValueFactory<>("id"));
            Colnom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            Colprenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            Colcin.setCellValueFactory(new PropertyValueFactory<>("cin"));
            Colusername.setCellValueFactory(new PropertyValueFactory<>("username"));
            Colpassword.setCellValueFactory(new PropertyValueFactory<>("password"));
            Colemail.setCellValueFactory(new PropertyValueFactory<>("email"));
            Colrole.setCellValueFactory(new PropertyValueFactory<>("role"));
            Colsexe.setCellValueFactory(new PropertyValueFactory<>("sexe"));
            Coladdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
            Colnumero.setCellValueFactory(new PropertyValueFactory<>("numero"));

            // Ajouter les boutons seulement si l'utilisateur est un Responsable RH
            if ("RHR".equals(userRole)) {
                addDeleteAndUpdateButtons();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Fonction pour ajouter les boutons Delete et Update
    private void addDeleteAndUpdateButtons() {
        Coldelete.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
                deleteButton.setOnAction(event -> {
                    user selectedUser = getTableView().getItems().get(getIndex());
                    deleteUser(selectedUser);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });

        Colupdate.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");

            {
                updateButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
                updateButton.setOnAction(event -> {
                    user selectedUser = getTableView().getItems().get(getIndex());
                    openUpdateUserForm(selectedUser);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : updateButton);
            }
        });
    }

    // Function to delete a user from the database and refresh the table
    private void deleteUser(user selectedUser) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Are you sure you want to delete this user?");
        confirmation.setContentText("This action cannot be undone.");

        confirmation.showAndWait().ifPresent(response -> {
            if (response.getText().equals("OK")) {
                try {
                    service.delete(selectedUser); // Call the delete method from userService
                    tableView.getItems().remove(selectedUser); // Remove user from TableView
                } catch (Exception e) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setContentText("Failed to delete user: " + e.getMessage());
                    error.showAndWait();
                }
            }
        });
    }


    private void refreshTable() {
        try {
            List<user> tab_users = service.getAll(); // Fetch updated user list from DB
            ObservableList<user> observableList = FXCollections.observableList(tab_users);
            tableView.setItems(observableList); // Update the TableView
            tableView.refresh(); // Force refresh
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Function to open interface ajouter user
    @FXML
    private void handleAddEmploye(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterUser.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Function to open the UpdateUser form and pass the selected user
    private void openUpdateUserForm(user selectedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateUser.fxml"));
            Parent root = loader.load();

            UpdateUser controller = loader.getController();
            controller.initData(selectedUser);

            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //function pour ouvrir l'interface d'affichage de promotion
    @FXML
    private void openPromotionForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPromotion.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openFinance(ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ajouterpret.fxml"));
            Parent root = loader.load();

            // If you want to pass data to the new controller, you can do so here:
            // Example: loader.getController().setData(someData);

            // Get the current stage (the window that triggered the event)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene on the stage
            stage.setScene(new Scene(root));

            // Optionally, you can set the title of the new window
            stage.setTitle("Finance - Ajouter Pret");

            // Show the new window
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // You can show an alert here for error handling if needed
        }
    }

    @FXML
    public void openProjects(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/projects.fxml"));
            Parent root = loader.load();

            // Récupérer la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène sur le stage
            stage.setScene(new Scene(root));

            // Définir le titre de la fenêtre
            stage.setTitle("Gestion des Projets");

            // Afficher la nouvelle scène
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Optionnel : Afficher une alerte en cas d'erreur
        }
    }






}
