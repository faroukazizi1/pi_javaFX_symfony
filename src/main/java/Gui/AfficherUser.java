package Gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import Model.user;
import Service.userService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
    void initialize() {
        try {
            List<user> tab_users = service.getAll();
            ObservableList<user> observableList = FXCollections.observableList(tab_users);
            tableView.setItems(observableList);

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

            // Add "Delete" button to each row
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
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            });

            // Add "Update" button to each row
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
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(updateButton);
                    }
                }
            });



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    @FXML
    private void handleAddEmploye(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterUser.fxml")); // Load AddUser.fxml
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Employee");
            stage.setScene(new Scene(root));
            //refresh tableView of Emplpyee
            stage.setOnHiding(e -> refreshTable());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to open the UpdateUser form and pass the selected user
    private void openUpdateUserForm(user selectedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateUser.fxml"));
            Parent root = loader.load();

            // pass the selected employe in tableview to update fields
            UpdateUser controller = loader.getController();
            controller.initData(selectedUser);

            Stage stage = new Stage();
            stage.setTitle("Update Employee");
            stage.setScene(new Scene(root));
            //refresh tableView of Employee
            stage.setOnHiding(e -> refreshTable());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
