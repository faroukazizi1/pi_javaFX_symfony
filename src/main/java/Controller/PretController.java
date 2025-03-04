package Controller;

import Service.PretService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Pret;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class PretController {

    // Fields for adding a loan
    @FXML private TextField montantPretField, tmmField, tauxField, revenusBrutsField, ageEmployeField, dureeRemboursementField;
    @FXML private DatePicker datePretPicker;
    @FXML private ComboBox<String> categorieField;
    @FXML private Button ajouterPretButton;

    // Fields for modifying a loan
    @FXML private TextField idPretFieldModif, montantPretFieldModif, tmmFieldModif, tauxFieldModif, revenusBrutsFieldModif, ageEmployeFieldModif, dureeRemboursementFieldModif;
    @FXML private DatePicker datePretPickerModif;
    @FXML private ComboBox<String> categorieFieldModif;
    @FXML private Button modifierPretButton;

    // Fields for deleting a loan
    @FXML private TextField idPretFieldSupprimer;
    @FXML private Button supprimerPretButton;

    // TableView for displaying loans
    @FXML private TableView<Pret> pretTableView;
    @FXML private TableColumn<Pret, Float> colMontant, colTmm, colTaux, colRevenus;
    @FXML private TableColumn<Pret, Integer> colAge, colDuree;
    @FXML private TableColumn<Pret, String> colCategorie;
    @FXML private TableColumn<Pret, LocalDate> colDate;

    private final ObservableList<Pret> listePrets = FXCollections.observableArrayList();
    private final PretService pretService = new PretService();

    @FXML
    public void initialize() {
        categorieField.setItems(FXCollections.observableArrayList("Fonctionnaire", "Cadre", "Ouvrier"));
        categorieFieldModif.setItems(FXCollections.observableArrayList("Fonctionnaire", "Cadre", "Ouvrier"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montantPret"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("datePret"));
        colTmm.setCellValueFactory(new PropertyValueFactory<>("tmm"));
        colTaux.setCellValueFactory(new PropertyValueFactory<>("taux"));
        colRevenus.setCellValueFactory(new PropertyValueFactory<>("revenusBruts"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("ageEmploye"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("dureeRemboursement"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        pretTableView.setItems(listePrets);
    }
    @FXML
    private void handleGoToReponse(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reponse.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddPret() {
        try {
            float montant = Float.parseFloat(montantPretField.getText());
            if (montant <= 0) {
                showAlert("Erreur", "Le montant du prêt doit être supérieur à 0.");
                return;
            }

            LocalDate localDate = datePretPicker.getValue();
            if (localDate == null) {
                showAlert("Erreur", "Veuillez sélectionner une date.");
                return;
            }

            // Allow only today or future dates
            if (localDate.isBefore(LocalDate.now())) {
                showAlert("Erreur", "La date du prêt doit être aujourd'hui ou dans le futur.");
                return;
            }

            float tmm = Float.parseFloat(tmmField.getText());
            if (tmm < 0) {
                showAlert("Erreur", "Le TMM ne peut pas être négatif.");
                return;
            }

            float taux = Float.parseFloat(tauxField.getText());
            if (taux < 0) {
                showAlert("Erreur", "Le taux ne peut pas être négatif.");
                return;
            }

            float revenus = Float.parseFloat(revenusBrutsField.getText());
            if (revenus <= 0) {
                showAlert("Erreur", "Les revenus doivent être supérieurs à 0.");
                return;
            }

            int age = Integer.parseInt(ageEmployeField.getText());
            if (age < 18) {
                showAlert("Erreur", "L'âge doit être d'au moins 18 ans.");
                return;
            }

            int duree = Integer.parseInt(dureeRemboursementField.getText());
            if (duree <= 0) {
                showAlert("Erreur", "La durée de remboursement doit être supérieure à 0.");
                return;
            } else if (duree > 30) {
                showAlert("Erreur", "La durée de remboursement ne peut pas dépasser 30 ans.");
                return;
            }

            String categorie = categorieField.getValue();
            if (categorie == null) {
                showAlert("Erreur", "Veuillez sélectionner une catégorie.");
                return;
            }

            // Create the loan object
            Pret newPret = new Pret();
            newPret.setMontantPret(montant);
            newPret.setDatePret(Date.valueOf(localDate));
            newPret.setTmm(tmm);
            newPret.setTaux(taux);
            newPret.setRevenusBruts(revenus);
            newPret.setAgeEmploye(age);
            newPret.setDureeRemboursement(duree);
            newPret.setCategorie(categorie);

            // Save the loan to the database
            PretService pretService = new PretService();
            pretService.add(newPret);

            // Retrieve the latest loan list from the database
            listePrets.setAll(pretService.getAll());

            clearAddForm();
            showAlert("Succès", "Prêt ajouté avec succès !");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez remplir correctement tous les champs.");
        }
    }




    @FXML
    private void handleSearchPret() {
        String idText = idPretFieldModif.getText().trim();

        if (idText.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un ID de prêt valide.");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            Pret pret = pretService.getById(id);

            if (pret != null) {
                // Populate fields with the retrieved Pret data
                montantPretFieldModif.setText(String.valueOf(pret.getMontantPret()));
                datePretPickerModif.setValue(pret.getDatePret().toLocalDate());
                tmmFieldModif.setText(String.valueOf(pret.getTmm()));
                tauxFieldModif.setText(String.valueOf(pret.getTaux()));
                revenusBrutsFieldModif.setText(String.valueOf(pret.getRevenusBruts()));
                ageEmployeFieldModif.setText(String.valueOf(pret.getAgeEmploye()));
                dureeRemboursementFieldModif.setText(String.valueOf(pret.getDureeRemboursement()));
                categorieFieldModif.setValue(pret.getCategorie());
            } else {
                showAlert("Prêt introuvable", "Aucun prêt trouvé avec cet ID.");
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur de format", "L'ID du prêt doit être un nombre valide.");
        }
    }


    @FXML
    private void handleEditPret() {
        try {
            if (idPretFieldModif.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez entrer un ID de prêt valide.");
                return;
            }

            int id = Integer.parseInt(idPretFieldModif.getText());
            Pret pret = pretService.getById(id);

            if (pret == null) {
                showAlert("Erreur", "Aucun prêt trouvé avec cet ID.");
                return;
            }

            // Ensure all fields are filled
            if (montantPretFieldModif.getText().isEmpty() ||
                    datePretPickerModif.getValue() == null ||
                    tmmFieldModif.getText().isEmpty() ||
                    tauxFieldModif.getText().isEmpty() ||
                    revenusBrutsFieldModif.getText().isEmpty() ||
                    ageEmployeFieldModif.getText().isEmpty() ||
                    dureeRemboursementFieldModif.getText().isEmpty() ||
                    categorieFieldModif.getValue() == null) {

                showAlert("Erreur", "Tous les champs doivent être remplis.");
                return;
            }

            // Montant validation
            float montant = Float.parseFloat(montantPretFieldModif.getText());
            if (montant <= 0) {
                showAlert("Erreur", "Le montant du prêt doit être supérieur à 0.");
                return;
            }
            pret.setMontantPret(montant);

            // Date validation
            LocalDate localDate = datePretPickerModif.getValue();
            if (localDate.isBefore(LocalDate.now())) {
                showAlert("Erreur", "La date du prêt doit être aujourd'hui ou dans le futur.");
                return;
            }
            pret.setDatePret(java.sql.Date.valueOf(localDate));

            // TMM validation
            float tmm = Float.parseFloat(tmmFieldModif.getText());
            if (tmm < 0) {
                showAlert("Erreur", "Le TMM ne peut pas être négatif.");
                return;
            }
            pret.setTmm(tmm);

            // Taux validation
            float taux = Float.parseFloat(tauxFieldModif.getText());
            if (taux < 0) {
                showAlert("Erreur", "Le taux ne peut pas être négatif.");
                return;
            }
            pret.setTaux(taux);

            // Revenus validation
            float revenus = Float.parseFloat(revenusBrutsFieldModif.getText());
            if (revenus <= 0) {
                showAlert("Erreur", "Les revenus doivent être supérieurs à 0.");
                return;
            }
            pret.setRevenusBruts(revenus);

            // Âge validation
            int age = Integer.parseInt(ageEmployeFieldModif.getText());
            if (age < 18) {
                showAlert("Erreur", "L'âge doit être d'au moins 18 ans.");
                return;
            }
            pret.setAgeEmploye(age);

            // Durée de remboursement validation
            int duree = Integer.parseInt(dureeRemboursementFieldModif.getText());
            if (duree <= 0) {
                showAlert("Erreur", "La durée de remboursement doit être supérieure à 0.");
                return;
            } else if (duree > 30) {
                showAlert("Erreur", "La durée de remboursement ne peut pas dépasser 30 ans.");
                return;
            }
            pret.setDureeRemboursement(duree);

            // Categorie validation
            pret.setCategorie(categorieFieldModif.getValue());

            // Update in DB and refresh table
            pretService.update(pret);
            pretTableView.setItems(FXCollections.observableArrayList(pretService.getAll()));
            pretTableView.refresh();

            showAlert("Succès", "Prêt modifié avec succès.");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides.");
        }
    }






    @FXML
    private void handleDeletePret() {
        try {
            int id = Integer.parseInt(idPretFieldSupprimer.getText());
            Pret pret = pretService.getById(id);  // Fetch from database

            if (pret != null) {
                pretService.delete(id); // Delete from database
                listePrets.setAll(pretService.getAll()); // Refresh the list
                showAlert("Succès", "Prêt supprimé avec succès.");
            } else {
                showAlert("Erreur", "Aucun prêt trouvé avec cet ID.");
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un ID valide.");
        }
    }


    @FXML
    private void handleViewPrets() {
        List<Pret> pretList = pretService.getAll();
        ObservableList<Pret> pretObservableList = FXCollections.observableArrayList(pretList);
        pretTableView.setItems(pretObservableList);    }

    private Pret findPretById(int id) {
        return listePrets.stream().filter(p -> p.getIdPret() == id).findFirst().orElse(null);
    }

    private void clearAddForm() {
        montantPretField.clear();
        datePretPicker.setValue(null);
        tmmField.clear();
        tauxField.clear();
        revenusBrutsField.clear();
        ageEmployeField.clear();
        dureeRemboursementField.clear();
        categorieField.setValue(null);
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void openEmployeeListView(ActionEvent event) {
        try {
            // Charger la vue des employés
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherUser.fxml"));  // Assurez-vous que le chemin est correct
            Parent root = loader.load();

            // Récupérer la scène actuelle et la mettre à jour avec la vue des employés
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));  // Change la scène
            stage.setTitle("Liste des Employés");  // Vous pouvez définir un titre approprié

        } catch (IOException e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Erreur de chargement");
            error.setContentText("Échec du chargement de la vue des employés : " + e.getMessage());
            error.showAndWait();
        }
    }
}
