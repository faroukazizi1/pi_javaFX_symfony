package Controller;

import GroupSuccess.esprit.TestFX;
import Service.PretService;
import Service.ReponseService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Pret;
import models.Reponse;
import java.util.List;

public class ReponseController {

    @FXML private ComboBox<Integer> pretIdComboBox;
    @FXML private Button generateButton;
    @FXML private TextField montantTextField;
    @FXML private TextField dureeTextField;
    @FXML private Label mensualiteLabel;
    @FXML private Label tauxMensuelLabel;
    @FXML private Label revenusBrutsLabel;
    @FXML private Label potentielCreditLabel;
    @FXML private Label montantAutoriseLabel;
    @FXML private Label assuranceLabel;
    @FXML private TextField idReponseFieldDelete;
    private PretService pretService;
    private ReponseService reponseService;

    // TableView
    @FXML private TableView<Reponse> tableView;
    @FXML private TableColumn<Reponse, Integer> colId;
    @FXML private TableColumn<Reponse, String> colDate;
    @FXML private TableColumn<Reponse, Double> colMontant;
    @FXML private TableColumn<Reponse, Double> colRevenus;
    @FXML private TableColumn<Reponse, Double> colTaux;
    @FXML private TableColumn<Reponse, Double> colMensualite;
    @FXML private TableColumn<Reponse, Double> colPotentiel;
    @FXML private TableColumn<Reponse, Integer> colDuree;
    @FXML private TableColumn<Reponse, Double> colAutorise;
    @FXML private TableColumn<Reponse, Double> colAssurance;

    @FXML private TextField montantField;
    @FXML private TextField dureeField;

    private ObservableList<Reponse> reponseList;
    @FXML
    private TextField editMontantTextField;
    @FXML
    private TextField editDureeTextField;
    @FXML
    private Label montantLabel;
    @FXML
    private Label dureeLabel;
    @FXML
    private TextField idReponseFieldModif;
    @FXML
    private TextField montantReponseFieldModif;
    @FXML
    private TextField dureeReponseFieldModif;

    @FXML
    private Label dateReponseLabelModif;
    @FXML
    private Label tauxInteretLabelModif;
    @FXML
    private Label revenusBrutsLabelModif;
    @FXML
    private Label mensualiteLabelModif;
    @FXML
    private Label potentielCreditLabelModif;
    @FXML
    private Label montantAutoriseLabelModif;
    @FXML
    private Label assuranceLabelModif;

    private Reponse reponseSelectionnee; // Stocke la réponse en cours de modification

    @FXML
    private void handleSearchReponse() {
        String idText = idReponseFieldModif.getText();
        if (idText.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un ID de réponse.");
            return;
        }

        int idReponse;
        try {
            idReponse = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID doit être un nombre.");
            return;
        }

        // Chercher la réponse parmi toutes les réponses disponibles
        reponseSelectionnee = reponseService.getAll().stream()
                .filter(r -> r.getIdReponse() == idReponse)
                .findFirst()
                .orElse(null);

        if (reponseSelectionnee == null) {
            showAlert("Erreur", "Aucune réponse trouvée avec cet ID.");
            return;
        }

        // Remplir les labels avec les données
        montantReponseFieldModif.setText(String.format("%.2f", reponseSelectionnee.getMontantDemande()));
        dureeReponseFieldModif.setText(String.valueOf(reponseSelectionnee.getDureeRemboursement()));

        dateReponseLabelModif.setText(reponseSelectionnee.getDateReponse().toString());
        tauxInteretLabelModif.setText(String.format("%.2f", reponseSelectionnee.getTauxInteret()));
        revenusBrutsLabelModif.setText(String.format("%.2f", reponseSelectionnee.getRevenusBruts()));
        mensualiteLabelModif.setText(String.format("%.2f", reponseSelectionnee.getMensualiteCredit()));
        potentielCreditLabelModif.setText(String.format("%.2f", reponseSelectionnee.getPotentielCredit()));
        montantAutoriseLabelModif.setText(String.format("%.2f", reponseSelectionnee.getMontantAutorise()));
        assuranceLabelModif.setText(String.format("%.2f", reponseSelectionnee.getAssurance()));
    }

    @FXML
    private void handleEditReponse() {
        if (reponseSelectionnee == null) {
            showAlert("Erreur", "Veuillez d'abord rechercher une réponse.");
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

        // Mise à jour des valeurs dans l'objet Reponse
        reponseSelectionnee.setMontantDemande(nouveauMontant);
        reponseSelectionnee.setDureeRemboursement(nouvelleDuree);

        // Appel à la méthode update() du service
        reponseService.update(reponseSelectionnee);
        showAlert("Succès", "Réponse mise à jour avec succès !");
    }

    @FXML
    private void saveModifications() {
        String newMontant = editMontantTextField.getText();
        String newDuree = editDureeTextField.getText();

        if (newMontant.isEmpty() || newDuree.isEmpty()) {
            // Gérer le cas où un champ est vide
            System.out.println("Veuillez remplir tous les champs.");
            return;
        }

        try {
            double montant = Double.parseDouble(newMontant);
            int duree = Integer.parseInt(newDuree);

            // Mettre à jour les labels
            montantLabel.setText(String.valueOf(montant));
            dureeLabel.setText(String.valueOf(duree));

            // Ajouter ici la logique pour sauvegarder dans la base de données si nécessaire

            System.out.println("Modifications enregistrées avec succès !");
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer des valeurs numériques valides.");
        }
    }

    @FXML
    public void initialize() {
        pretService = new PretService();
        reponseService = new ReponseService();
        reponseList = FXCollections.observableArrayList();

        loadPretIds();
        setupTable();
        loadReponses();
    }

    private void loadPretIds() {
        List<Integer> pretIds = pretService.getAllPretIds();
        if (pretIds != null) {
            ObservableList<Integer> ids = FXCollections.observableArrayList(pretIds);
            pretIdComboBox.setItems(ids);
        } else {
            showAlert("Erreur", "Échec du chargement des ID de prêt.");
        }
    }

    private void setupTable() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateReponse"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montantDemande"));
        colRevenus.setCellValueFactory(new PropertyValueFactory<>("revenusBruts"));
        colTaux.setCellValueFactory(new PropertyValueFactory<>("tauxInteret"));
        colMensualite.setCellValueFactory(new PropertyValueFactory<>("mensualiteCredit"));
        colPotentiel.setCellValueFactory(new PropertyValueFactory<>("potentielCredit"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("dureeRemboursement"));
        colAutorise.setCellValueFactory(new PropertyValueFactory<>("montantAutorise"));
        colAssurance.setCellValueFactory(new PropertyValueFactory<>("assurance"));

        tableView.setItems(reponseList);
    }

    @FXML
    private void loadReponses() {
        List<Reponse> reponses = reponseService.getAll();
        reponseList.setAll(reponses);
    }

    @FXML
    private void generateReponse() {
        Integer selectedId = pretIdComboBox.getValue();
        if (selectedId == null) {
            showAlert("Erreur", "Veuillez sélectionner un ID de prêt.");
            return;
        }

        Pret pret = pretService.getById(selectedId);
        if (pret == null) {
            showAlert("Erreur", "Prêt introuvable.");
            return;
        }

        double montantDemande = pret.getMontantPret();
        int duree = pret.getDureeRemboursement();
        montantLabel.setText(String.format("%.2f", montantDemande));
        dureeLabel.setText(String.valueOf(duree));

        double tauxInteret = 5.0;
        double tauxMensuel = tauxInteret / 12 / 100;
        double revenusBruts = montantDemande * 0.4;
        double mensualiteCredit = (tauxMensuel > 0) ?
                (montantDemande * tauxMensuel) / (1 - Math.pow(1 + tauxMensuel, -duree)) :
                montantDemande / duree;
        double potentielCredit = revenusBruts * 0.3;
        double montantAutorise = potentielCredit * 0.9;
        double assurance = montantDemande * 0.02;

        tauxMensuelLabel.setText(String.format("%.4f", tauxMensuel));
        revenusBrutsLabel.setText(String.format("%.2f", revenusBruts));
        mensualiteLabel.setText(String.format("%.2f", mensualiteCredit));
        potentielCreditLabel.setText(String.format("%.2f", potentielCredit));
        montantAutoriseLabel.setText(String.format("%.2f", montantAutorise));
        assuranceLabel.setText(String.format("%.2f", assurance));

        Reponse reponse = new Reponse();
        reponse.setDateReponse(java.sql.Date.valueOf(java.time.LocalDate.now()));
        reponse.setMontantDemande(montantDemande);
        reponse.setRevenusBruts(revenusBruts);
        reponse.setTauxInteret(tauxInteret);
        reponse.setMensualiteCredit(mensualiteCredit);
        reponse.setPotentielCredit(potentielCredit);
        reponse.setDureeRemboursement(duree);
        reponse.setMontantAutorise(montantAutorise);
        reponse.setAssurance(assurance);

        boolean isSaved = reponseService.saveReponse(reponse);
        if (isSaved) {
            showAlert("Succès", "Réponse générée et enregistrée !");
            loadReponses();
        } else {
            showAlert("Erreur", "Erreur lors de l'enregistrement.");
        }
    }


    @FXML
    private void updateReponse() {
        Reponse selectedReponse = tableView.getSelectionModel().getSelectedItem();
        if (selectedReponse == null) {
            showAlert("Erreur", "Sélectionnez une réponse à modifier.");
            return;
        }

        try {
            double newMontant = Double.parseDouble(montantField.getText());
            int newDuree = Integer.parseInt(dureeField.getText());

            selectedReponse.setMontantDemande(newMontant);
            selectedReponse.setDureeRemboursement(newDuree);

            reponseService.update(selectedReponse);
            loadReponses();
            showAlert("Succès", "Réponse mise à jour !");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Valeurs invalides.");
        }
    }
    @FXML
    private void deleteReponse() {
        String idText = idReponseFieldDelete.getText().trim();

        if (idText.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un ID de réponse à supprimer.");
            return;
        }

        try {
            int idReponse = Integer.parseInt(idText);
            Reponse reponse = new Reponse();
            reponse.setIdReponse(idReponse); // Assigner l'ID à un objet Reponse

            reponseService.delete(reponse); // Suppression via la méthode existante

            loadReponses(); // Recharger les données après suppression
            showAlert("Succès", "Réponse supprimée !");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID doit être un nombre valide.");
        }
    }


    @FXML
    private void acceptReponse() {
        showAlert("Succès", "Réponse acceptée !");
    }

    @FXML
    private void rejectReponse() {
        showAlert("Info", "Réponse rejetée !");
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
