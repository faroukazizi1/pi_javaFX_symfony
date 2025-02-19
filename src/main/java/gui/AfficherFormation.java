package gui;



import Model.Formation;
import Service.FormationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherFormation implements Initializable {

    @FXML
    private TableView<Formation> tableFormations;
    @FXML
    private TableColumn<Formation, Integer> colId;
    @FXML
    private TableColumn<Formation, String> colTitre;
    @FXML
    private TableColumn<Formation, String> colDescription;
    @FXML
    private TableColumn<Formation, String> colDateD;
    @FXML
    private TableColumn<Formation, String> colDateF;
    @FXML
    private TableColumn<Formation, Integer> colDuree;
    @FXML
    private TableColumn<Formation, String> colImage;
    @FXML
    private TableColumn<Formation, Integer> colIdFormateur;
    @FXML
    private TableColumn<Formation, Void> colUpdate;
    @FXML
    private TableColumn<Formation, Void> colDelete;

    private final FormationService formationService = new FormationService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_form"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colDateD.setCellValueFactory(new PropertyValueFactory<>("Date_D"));
        colDateF.setCellValueFactory(new PropertyValueFactory<>("Date_F"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("Duree"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("Image"));
        colIdFormateur.setCellValueFactory(new PropertyValueFactory<>("id_Formateur"));

        colUpdate.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("Modifier");
            {
                btnModifier.setOnAction(event -> {
                    Formation formation = getTableView().getItems().get(getIndex());
                    openModifierPage(formation);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnModifier);
            }
        });

        colDelete.setCellFactory(param -> new TableCell<>() {
            private final Button btnSupprimer = new Button("Supprimer");
            {
                btnSupprimer.setOnAction(event -> {
                    Formation formation = getTableView().getItems().get(getIndex());
                    supprimerFormation(formation);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnSupprimer);
            }
        });

        loadFormations();
    }

    private void loadFormations() {
        List<Formation> formations = formationService.getAll();
        ObservableList<Formation> observableList = FXCollections.observableArrayList(formations);
        tableFormations.setItems(observableList);
    }

    private void openModifierPage(Formation formation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifFormation.fxml"));
            Parent root = loader.load();
            ModifFormation controller = loader.getController();
            controller.setFormation(formation);
            Scene scene = new Scene(root);
            Stage stage = (Stage) tableFormations.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void supprimerFormation(Formation formation) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cette formation?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                formationService.delete(formation);
                loadFormations();
            }
        });
    }
}

