package Controller;

import Service.ReponseService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import models.Reponse;

import java.util.List;

public class ReponseController {

    // TableView for Reponses
    @FXML private TableView<Reponse> tableView;
    @FXML private TableColumn<Reponse, String> colDate;
    @FXML private TableColumn<Reponse, Double> colMontant;
    @FXML private TableColumn<Reponse, Double> colRevenus;
    @FXML private TableColumn<Reponse, Double> colTaux;
    @FXML private TableColumn<Reponse, Double> colMensualite;
    @FXML private TableColumn<Reponse, Double> colPotentiel;
    @FXML private TableColumn<Reponse, Integer> colDuree;
    @FXML private TableColumn<Reponse, Double> colAutorise;
    @FXML private TableColumn<Reponse, Double> colAssurance;
    @FXML private TableColumn<Reponse, Void> colActions;

    private ObservableList<Reponse> reponseList;

    @FXML private TextField idReponseFieldModif;
    @FXML private TextField montantReponseFieldModif;
    @FXML private TextField dureeReponseFieldModif;

    @FXML private Label dateReponseLabelModif;
    @FXML private Label tauxInteretLabelModif;
    @FXML private Label revenusBrutsLabelModif;
    @FXML private Label mensualiteLabelModif;
    @FXML private Label potentielCreditLabelModif;
    @FXML private Label montantAutoriseLabelModif;
    @FXML private Label assuranceLabelModif;

    private ReponseService reponseService;
    private Reponse reponseSelectionnee;

    @FXML private TabPane tabPane;

    @FXML
    public void initialize() {
        reponseService = new ReponseService();
        reponseList = FXCollections.observableArrayList();

        setupReponseTable();
        loadReponses();
    }

    private void setupReponseTable() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateReponse"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montantDemande"));
        colRevenus.setCellValueFactory(new PropertyValueFactory<>("revenusBruts"));
        colTaux.setCellValueFactory(new PropertyValueFactory<>("tauxInteret"));
        colMensualite.setCellValueFactory(new PropertyValueFactory<>("mensualiteCredit"));
        colPotentiel.setCellValueFactory(new PropertyValueFactory<>("potentielCredit"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("dureeRemboursement"));
        colAutorise.setCellValueFactory(new PropertyValueFactory<>("montantAutorise"));
        colAssurance.setCellValueFactory(new PropertyValueFactory<>("assurance"));

        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");
            private final HBox hbox = new HBox(10, updateButton, deleteButton);
            {
                updateButton.getStyleClass().add("update-button");
                deleteButton.getStyleClass().add("delete-button");

                updateButton.setOnAction(event -> {
                    Reponse reponse = getTableView().getItems().get(getIndex());
                    handleUpdateReponseFromTable(reponse);
                });

                deleteButton.setOnAction(event -> {
                    Reponse reponse = getTableView().getItems().get(getIndex());
                    handleDeleteReponseFromTable(reponse);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });

        tableView.setItems(reponseList);
    }

    @FXML
    private void loadReponses() {
        List<Reponse> reponses = reponseService.getAll();
        reponseList.setAll(reponses);
    }

    private void handleUpdateReponseFromTable(Reponse reponse) {
        reponseSelectionnee = reponse;
        montantReponseFieldModif.setText(String.format("%.2f", reponse.getMontantDemande()));
        dureeReponseFieldModif.setText(String.valueOf(reponse.getDureeRemboursement()));
        dateReponseLabelModif.setText(reponse.getDateReponse().toString());
        tauxInteretLabelModif.setText(String.format("%.2f", reponse.getTauxInteret()));
        revenusBrutsLabelModif.setText(String.format("%.2f", reponse.getRevenusBruts()));
        mensualiteLabelModif.setText(String.format("%.2f", reponse.getMensualiteCredit()));
        potentielCreditLabelModif.setText(String.format("%.2f", reponse.getPotentielCredit()));
        montantAutoriseLabelModif.setText(String.format("%.2f", reponse.getMontantAutorise()));
        assuranceLabelModif.setText(String.format("%.2f", reponse.getAssurance()));

        tabPane.getSelectionModel().select(1);
    }

    @FXML
    private void handleEditReponse() {
        if (reponseSelectionnee == null) {
            showAlert("Erreur", "Veuillez sélectionner une réponse à modifier depuis le tableau.");
            return;
        }

        double nouveauMontant;
        int nouvelleDuree;

        try {
            nouveauMontant = Double.parseDouble(montantReponseFieldModif.getText());
            nouvelleDuree = Integer.parseInt(dureeReponseFieldModif.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides pour le montant et la durée.");
            return;
        }

        reponseSelectionnee.setMontantDemande(nouveauMontant);
        reponseSelectionnee.setDureeRemboursement(nouvelleDuree);

        reponseService.update(reponseSelectionnee);
        showAlert("Succès", "Réponse mise à jour avec succès !");
        loadReponses();
    }

    private void handleDeleteReponseFromTable(Reponse reponse) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Voulez-vous vraiment supprimer la réponse ID " + reponse.getIdReponse() + " ?",
                ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait();

        if (confirmation.getResult() == ButtonType.YES) {
            reponseService.delete(reponse);
            loadReponses();
            showAlert("Succès", "Réponse supprimée !");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void refreshTable() {
        List<Reponse> reponses = reponseService.getAll();
        reponseList.setAll(reponses);
        tableView.refresh();
    }
}