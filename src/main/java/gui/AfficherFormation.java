package gui;

import Model.Formation;
import Service.FormationService;
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
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    @FXML
    private TextField searchField;
    @FXML
    private DatePicker searchDateField;
    @FXML
    private ComboBox<String> sortComboBox;

    private final FormationService formationService = new FormationService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_form"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colDateD.setCellValueFactory(new PropertyValueFactory<>("Date_D"));
        colDateF.setCellValueFactory(new PropertyValueFactory<>("Date_F"));
        colDuree.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getDuree()));
        colIdFormateur.setCellValueFactory(new PropertyValueFactory<>("id_Formateur"));

        colImage.setCellFactory(col -> new TableCell<Formation, String>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);
                if (empty || imageUrl == null) {
                    setGraphic(null);
                } else {
                    try {
                        Image image = new Image(imageUrl);
                        imageView.setImage(image);
                        imageView.setFitHeight(50);
                        imageView.setFitWidth(50);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });

        colUpdate.setCellFactory(col -> new TableCell<Formation, Void>() {
            private final Button btnModifier = new Button("Modifier");
            {
                btnModifier.setOnAction(event -> handleModifier(getTableRow().getItem()));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnModifier);
            }
        });

        colDelete.setCellFactory(col -> new TableCell<Formation, Void>() {
            private final Button btnSupprimer = new Button("Supprimer");
            {
                btnSupprimer.setOnAction(event -> handleSupprimer(getTableRow().getItem()));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnSupprimer);
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());
        searchDateField.valueProperty().addListener((observable, oldValue, newValue) -> handleSearch());
        sortComboBox.setOnAction(event -> handleSort());

        loadFormations();
    }

    private void loadFormations() {
        ObservableList<Formation> formations = FXCollections.observableArrayList(formationService.getAll());
        tableFormations.setItems(formations);
    }

    private void handleModifier(Formation formation) {
        try {
            System.out.println("Chargement du fichier FXML pour modifier la formation...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifFormation.fxml")); // Fichier FXML pour la modification de formation
            Parent root = loader.load();
            System.out.println("FXML chargé avec succès");

            ModifFormation controller = loader.getController();
            if (controller == null) {
                System.out.println("Erreur : Le contrôleur est null !");
                return;
            }

            controller.setFormation(formation); // Passer la formation au contrôleur
            System.out.println("Données de la formation envoyées au contrôleur");

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Pour bloquer l'accès à la fenêtre principale jusqu'à la fermeture
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Formation");
            stage.showAndWait(); // Afficher la fenêtre de modification en mode modal

            loadFormations(); // Recharger la liste des formations après modification
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement du fichier FXML pour la modification de la formation !");
        }
    }


    private void handleSupprimer(Formation formation) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cette formation?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                formationService.delete(formation);
                loadFormations();
            }
        });
    }
    @FXML

    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        String dateText = (searchDateField.getValue() != null) ? searchDateField.getValue().toString() : null;
        List<Formation> formations = formationService.getAll();
        List<Formation> filteredFormations = formations.stream()
                .filter(f -> f.getTitre().toLowerCase().contains(searchText))
                .filter(f -> dateText == null || f.getDate_D().toString().contains(dateText))
                .toList();
        tableFormations.setItems(FXCollections.observableArrayList(filteredFormations));
    }
    @FXML

    private void handleSort() {
        String selectedSortOption = sortComboBox.getValue();
        List<Formation> formations = formationService.getAll();
        if (selectedSortOption != null) {
            switch (selectedSortOption) {
                case "Trier par date (plus proche)":
                    formations.sort((f1, f2) -> f1.getDate_D().compareTo(f2.getDate_D()));
                    break;
                case "Trier par ID Formateur":
                    formations.sort((f1, f2) -> Integer.compare(f1.getId_Formateur(), f2.getId_Formateur()));
                    break;
            }
        }
        tableFormations.setItems(FXCollections.observableArrayList(formations));
    }
    public void handleAfficherFormateur(ActionEvent event) {
        // Logic to show the formateur page, or open a new window
        System.out.println("Afficher Formateur clicked");
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
    private void handleAjouterFormation(ActionEvent event) {
        try {
            // Charger le fichier FXML pour ajouter une formation
            Parent parent = FXMLLoader.load(getClass().getResource("/AjouterFormation.fxml")); // Assurez-vous que le chemin du fichier FXML est correct
            Scene scene = new Scene(parent);

            // Obtenir la scène actuelle et changer la scène pour la nouvelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show(); // Afficher la fenêtre pour ajouter une formation
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la fenêtre d'ajout de formation.");
        }
    }


}
