package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import Model.Formateur;
import Service.FormateurService;

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
    private TableColumn<Formateur, String> colUpdate;
    @FXML
    private TableColumn<Formateur, String> colDelete;

    @FXML
    private Button btnAjouterFormateur;

    private final FormateurService formateurService = new FormateurService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_Formateur"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("Numero"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("Nom_F"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("Prenom_F"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colSpecialite.setCellValueFactory(new PropertyValueFactory<>("Specialite"));

        colUpdate.setCellFactory(param -> {
            TableCell<Formateur, String> cell = new TableCell<Formateur, String>() {
                private final Button btn = new Button("Modifier");

                {
                    btn.getStyleClass().add("btn-modifier");  // Ajout de la classe CSS
                    btn.setOnAction(event -> {
                        Formateur formateur = getTableView().getItems().get(getIndex());
                        openModifierPage(formateur);  // Appel de la méthode pour ouvrir le formulaire de modification
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
            return cell;
        });

        colDelete.setCellFactory(param -> {
            TableCell<Formateur, String> cell = new TableCell<Formateur, String>() {
                private final Button btn = new Button("Supprimer");

                {
                    btn.getStyleClass().add("btn-supprimer");  // Ajout de la classe CSS
                    btn.setOnAction(event -> {
                        Formateur formateur = getTableView().getItems().get(getIndex());
                        supprimerFormateur(formateur);  // Appel de la méthode pour supprimer le formateur
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
            return cell;
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

    @FXML
    private void handleAjouterFormateur(ActionEvent event) {
        switchScene(event, "/AjouetFormateur.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
