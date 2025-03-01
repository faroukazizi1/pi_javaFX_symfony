package GroupSuccess.esprit;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.DemandeConge;
import Services.GestionConge;

import javafx.scene.input.MouseEvent; // Import MouseEvent

import java.io.IOException;

public class GestionCongeController {

    @FXML
    private TableView<DemandeConge> tableConge;

    @FXML
    private TableColumn<DemandeConge, Integer> employeIdColumn;
    @FXML
    private TableColumn<DemandeConge, String> typeColumn;
    @FXML
    private TableColumn<DemandeConge, String> debutColumn;
    @FXML
    private TableColumn<DemandeConge, String> finColumn;
    @FXML
    private TableColumn<DemandeConge, String> statutColumn;
    @FXML
    private TableColumn<DemandeConge, Void> actionColumn;

    @FXML
    public void initialize() {
        employeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeId"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        debutColumn.setCellValueFactory(new PropertyValueFactory<>("debut"));
        finColumn.setCellValueFactory(new PropertyValueFactory<>("fin"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));

        ajouterBoutonSupprimer();

        int userId = 1;
        chargerDemandes(userId);

        // Set double-click event for the TableView
        tableConge.setOnMouseClicked(this::handleTableRowDoubleClick);
    }

    private void chargerDemandes(int userId) {
        ObservableList<DemandeConge> demandesCongeList = GestionConge.getDemandesCongeList(userId);
        tableConge.setItems(demandesCongeList);
    }

    private void ajouterBoutonSupprimer() {
        actionColumn.setCellFactory(param -> new TableCell<DemandeConge, Void>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    DemandeConge demande = getTableView().getItems().get(getIndex());
                    supprimerDemande(demande);
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
    }

    private void handleTableRowDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {  // Double click detected
            DemandeConge selectedDemande = tableConge.getSelectionModel().getSelectedItem();
            if (selectedDemande != null) {
                openUpdateWindow(selectedDemande);
            } else {
                // Show alert if no item is selected
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Sélectionnez une demande");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner une demande de congé.");
                alert.showAndWait();
            }
        }
    }

    private void openUpdateWindow(DemandeConge selectedDemande) {
        try {
            // Assuming you're using FXMLLoader to load the update form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/update.fxml"));
            Parent root = loader.load();

            // Get the controller of the update window
            UpdateDemandeCongeController controller = loader.getController();
            controller.setDemande(selectedDemande);

            // Show the update window (in a new stage)
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Demande de Congé");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void supprimerDemande(DemandeConge demande) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cette demande?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                GestionConge.supprimerDemandeConge(demande.getId());
                chargerDemandes(1);
            }
        });
    }
}
