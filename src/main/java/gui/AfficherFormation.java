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
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Callback;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private Button btnAjouterFormation;
    @FXML
    private Button btnListeFormations;
    @FXML
    private TextField searchField;
    @FXML
    private DatePicker searchDateField;
    @FXML
    private ComboBox<String> sortComboBox;

    private final FormationService formationService = new FormationService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Lier les colonnes aux bonnes propriétés
        colId.setCellValueFactory(new PropertyValueFactory<>("id_form"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colDateD.setCellValueFactory(new PropertyValueFactory<>("Date_D"));
        colDateF.setCellValueFactory(new PropertyValueFactory<>("Date_F"));
        colDuree.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getDuree()));
        colIdFormateur.setCellValueFactory(new PropertyValueFactory<>("id_Formateur"));

        // Pour la colonne Modifier
        colUpdate.setCellFactory(col -> {
            return new TableCell<Formation, Void>() {
                private final Button btnModifier = new Button("Modifier");

                {
                    btnModifier.getStyleClass().add("button-modifier");
                    btnModifier.setOnAction(event -> {
                        Formation formation = getTableRow().getItem();
                        if (formation != null) {
                            handleModifier(formation);
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : btnModifier);
                }
            };
        });

        // Pour la colonne Supprimer
        colDelete.setCellFactory(col -> {
            return new TableCell<Formation, Void>() {
                private final Button btnSupprimer = new Button("Supprimer");

                {
                    btnSupprimer.getStyleClass().add("button-supprimer");
                    btnSupprimer.setOnAction(event -> {
                        Formation formation = getTableRow().getItem();
                        if (formation != null) {
                            handleSupprimer(formation);
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : btnSupprimer);
                }
            };
        });

        // Lier le champ de recherche par texte et date
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());  // Garder seulement une seule occurrence
        searchDateField.valueProperty().addListener((observable, oldValue, newValue) -> handleSearch());
        sortComboBox.setOnAction(event -> handleSort());


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

    @FXML
    private void handleListeFormations(ActionEvent event) {
        switchScene(event, "AfficherFormation.fxml");
    }

    @FXML
    private void handleAjouterFormation(ActionEvent event) {
        switchScene(event, "/AjouterFormation.fxml");
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

    // Gérer la recherche par texte et date
    @FXML
    private void handleSearch() {
        final String searchText = searchField.getText().toLowerCase();  // Recherche insensible à la casse
        final String dateText = (searchDateField.getValue() != null) ? searchDateField.getValue().toString() : null;

        // Récupérer toutes les formations
        List<Formation> formations = formationService.getAll();

        // Filtrer les formations en fonction du titre et de la date
        List<Formation> filteredFormations = formations.stream()
                .filter(f -> f.getTitre().toLowerCase().contains(searchText))
                .filter(f -> {
                    if (dateText != null) {
                        return f.getDate_D().toString().contains(dateText);  // Comparer la date
                    }
                    return true;
                })
                .toList();

        ObservableList<Formation> observableList = FXCollections.observableArrayList(filteredFormations);
        tableFormations.setItems(observableList);
    }

    public void handleModifier(Formation formation) {
        System.out.println("Modification de la formation : " + formation);
        openModifierPage(formation);
    }

    public void handleSupprimer(Formation formation) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cette formation?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                formationService.delete(formation);
                tableFormations.getItems().remove(formation);
                tableFormations.refresh();
            }
        });
    }
    @FXML
    private void handleSearch2() {
        final String searchText = searchField.getText().toLowerCase();  // Recherche insensible à la casse
        final String dateText = (searchDateField.getValue() != null) ? searchDateField.getValue().toString() : null;

        // Récupérer toutes les formations
        List<Formation> formations = formationService.getAll();

        // Filtrer les formations en fonction du titre et de la date
        List<Formation> filteredFormations = formations.stream()
                .filter(f -> f.getTitre().toLowerCase().contains(searchText))
                .filter(f -> {
                    if (dateText != null) {
                        return f.getDate_D().toString().contains(dateText);  // Comparer la date
                    }
                    return true;
                })
                .toList();

        ObservableList<Formation> observableList = FXCollections.observableArrayList(filteredFormations);
        tableFormations.setItems(observableList);  // Mettre à jour la TableView
    }
    public void handleSort() {
        String selectedSortOption = sortComboBox.getValue();

        // Filtrer et trier les formations en fonction de la sélection dans le ComboBox
        List<Formation> formations = formationService.getAll();

        if (selectedSortOption != null) {
            switch (selectedSortOption) {
                case "Trier par date (plus proche)":
                    formations.sort((f1, f2) -> f1.getDate_D().compareTo(f2.getDate_D()));  // Trier par date de début
                    break;
                case "Trier par ID Formateur":
                    formations.sort((f1, f2) -> Integer.compare(f1.getId_Formateur(), f2.getId_Formateur()));  // Trier par ID du formateur
                    break;
            }
        }

        // Mettre à jour la TableView
        ObservableList<Formation> observableList = FXCollections.observableArrayList(formations);
        tableFormations.setItems(observableList);
    }




}
