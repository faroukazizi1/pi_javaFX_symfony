package gui;

import Model.Formateur;
import Service.FormateurService;
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

public class AfficherFormateur implements Initializable {

    @FXML
    private TableView<Formateur> tableFormateurs;
    @FXML
    private TableColumn<Formateur, Integer> colId;
    @FXML
    private TableColumn<Formateur, Integer> colNumero;
    @FXML
    private TableColumn<Formateur, String> colNom;
    @FXML
    private TableColumn<Formateur, String> colPrenom;
    @FXML
    private TableColumn<Formateur, String> colEmail;
    @FXML
    private TableColumn<Formateur, String> colSpecialite;
    @FXML
    private TableColumn<Formateur, Void> colUpdate;
    @FXML
    private TableColumn<Formateur, Void> colDelete;

    private final FormateurService formateurService = new FormateurService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_Formateur"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("Numero"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("Nom_F"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("Prenom_F"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colSpecialite.setCellValueFactory(new PropertyValueFactory<>("Specialite"));

        colUpdate.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("Modifier");
            {
                btnModifier.setOnAction(event -> {
                    Formateur formateur = getTableView().getItems().get(getIndex());
                    openModifierPage(formateur);
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
                    Formateur formateur = getTableView().getItems().get(getIndex());
                    supprimerFormateur(formateur);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnSupprimer);
            }
        });

        loadFormateurs();
    }

    private void loadFormateurs() {
        List<Formateur> formateurs = formateurService.getAll();
        ObservableList<Formateur> observableList = FXCollections.observableArrayList(formateurs);
        tableFormateurs.setItems(observableList);
    }

    private void openModifierPage(Formateur formateur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifFormateur.fxml"));
            Parent root = loader.load();
            ModifFormateur controller = loader.getController();
            controller.setFormateur(formateur);
            Scene scene = new Scene(root);
            Stage stage = (Stage) tableFormateurs.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void supprimerFormateur(Formateur formateur) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer ce formateur?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                formateurService.delete(formateur);
                loadFormateurs();
            }
        });
    }
}
