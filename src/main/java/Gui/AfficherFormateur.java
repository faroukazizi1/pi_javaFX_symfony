package Gui;

import Model.Formateur;
import Service.FormateurService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> sortComboBox;
    @FXML
    private ComboBox<String> searchTypeComboBox;
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

        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());
        searchTypeComboBox.setOnAction(event -> handleSearch());
        sortComboBox.setOnAction(event -> handleSort());

        sortComboBox.getItems().addAll("Trier par spécialité", "Trier par nom");
        searchTypeComboBox.getItems().addAll("Nom", "ID");

        loadFormateurs();
    }

    private void loadFormateurs() {
        List<Formateur> formateurs = formateurService.getAll();
        ObservableList<Formateur> observableList = FXCollections.observableArrayList(formateurs);
        tableFormateurs.setItems(observableList);
        setupUpdateAndDeleteButtons();
    }

    private void setupUpdateAndDeleteButtons() {
        colUpdate.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Modifier");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    updateButton.setOnAction(event -> handleUpdate(getTableView().getItems().get(getIndex())));
                    setGraphic(updateButton);
                }
            }
        });

        colDelete.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    deleteButton.setOnAction(event -> handleDelete(getTableView().getItems().get(getIndex())));
                    setGraphic(deleteButton);
                }
            }
        });
    }

    @FXML
    private void handleSearch() {
        String searchType = searchTypeComboBox.getValue();
        String searchText = searchField.getText().toLowerCase();
        List<Formateur> formateurs = formateurService.getAll();

        List<Formateur> filteredFormateurs = formateurs.stream()
                .filter(f -> "Nom".equals(searchType) ? f.getNom_F().toLowerCase().contains(searchText) :
                        "ID".equals(searchType) && String.valueOf(f.getId_Formateur()).equals(searchText))
                .collect(Collectors.toList());

        tableFormateurs.setItems(FXCollections.observableArrayList(filteredFormateurs));
    }

    @FXML
    private void handleSort() {
        String selectedSortOption = sortComboBox.getValue();
        List<Formateur> formateurs = formateurService.getAll();

        if (selectedSortOption != null) {
            formateurs.sort((f1, f2) -> "Trier par spécialité".equals(selectedSortOption) ?
                    f1.getSpecialite().compareToIgnoreCase(f2.getSpecialite()) :
                    f1.getNom_F().compareToIgnoreCase(f2.getNom_F()));
        }

        tableFormateurs.setItems(FXCollections.observableArrayList(formateurs));
    }

    @FXML
    private void handleAjouterFormateur(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/AjouetFormateur.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleUpdate(Formateur formateur) {
        try {
            System.out.println("Chargement du fichier FXML...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifFormateur.fxml"));
            Parent root = loader.load();
            System.out.println("FXML chargé avec succès");

            ModifFormateur controller = loader.getController();
            if (controller == null) {
                System.out.println("Erreur : Le contrôleur est null !");
                return;
            }

            controller.setFormateur(formateur);
            System.out.println("Données du formateur envoyées au contrôleur");

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Formateur");
            stage.showAndWait();

            loadFormateurs();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement du fichier FXML !");
        }
    }


    private void handleDelete(Formateur formateur) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer ce formateur ?");
        alert.setContentText("Nom: " + formateur.getNom_F());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            formateurService.delete(formateur);
            loadFormateurs();
        }
    }
    @FXML
    private void handleAfficherFormation(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormation.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et changer de scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'erreur en affichant la trace
        }
    }
    @FXML
    private void handleAfficherStatistiques(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StatistiquesFormation.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleBtnCertif(ActionEvent event) {
        try {
            // Charger la nouvelle interface FXML pour les certificats
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GenererCertification.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et mettre à jour son contenu
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'exception en affichant l'erreur dans la console
        }
    }

}
