package GroupSuccess.esprit;

import Services.GestionConge;
import Util.DBconnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.StatutDemande;
import models.DemandeConge;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TraitementDemandeController {

    @FXML
    private TableView<DemandeConge> tableDemandeConge;

    @FXML
    private TableColumn<DemandeConge, Integer> idColumn;

    @FXML
    private TableColumn<DemandeConge, Integer> employeColumn;

    @FXML
    private TableColumn<DemandeConge, String> typeCongeColumn;

    @FXML
    private TableColumn<DemandeConge, String> dateDebutColumn;

    @FXML
    private TableColumn<DemandeConge, String> dateFinColumn;

    @FXML
    private TableColumn<DemandeConge, String> dateDemandeColumn;

    @FXML
    private TableColumn<DemandeConge, String> statutColumn;

    @FXML
    private ComboBox<String> comboStatut;

    @FXML
    private Button btnTraiter;

    private ObservableList<DemandeConge> demandeList = FXCollections.observableArrayList();

    // Initialize the view components
    public void initialize() {
        // Set up TableView columns with correct property bindings
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        employeColumn.setCellValueFactory(cellData -> cellData.getValue().employeIdProperty().asObject());
        typeCongeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        dateDebutColumn.setCellValueFactory(cellData -> cellData.getValue().debutProperty());
        dateFinColumn.setCellValueFactory(cellData -> cellData.getValue().finProperty());
        dateDemandeColumn.setCellValueFactory(cellData -> cellData.getValue().dateDemandeProperty());
        statutColumn.setCellValueFactory(cellData -> cellData.getValue().statutProperty());

        // Load the Demandes data
        loadDemandes();

        // Initialize the ComboBox with the possible status options
        comboStatut.setItems(FXCollections.observableArrayList("APPROUVE", "REJETE", "EN_ATTENTE"));
    }

    // Load the DemandesConge from the database
    private void loadDemandes() {
        Connection conn = DBconnection.getInstance().getConn();
        if (conn == null) {
            showError("Erreur de connexion à la base de données.");
            return;
        }
        demandeList = afficherDemandesEnAttente(conn);
        tableDemandeConge.setItems(demandeList);
    }

    public static ObservableList<DemandeConge> afficherDemandesEnAttente(Connection conn) {
        ObservableList<DemandeConge> demandesEnAttente = FXCollections.observableArrayList();
        String query = "SELECT id, employe_id, type_conge, date_debut, date_fin, date_demande, statut " +
                "FROM DemandeConge WHERE statut = 'EN_ATTENTE'";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int employeId = rs.getInt("employe_id");
                String typeConge = rs.getString("type_conge");
                String dateDebut = rs.getString("date_debut");
                String dateFin = rs.getString("date_fin");
                String dateDemande = rs.getString("date_demande");
                String statut = rs.getString("statut");
                DemandeConge demandeConge = new DemandeConge(id, employeId, typeConge, dateDebut, dateFin, dateDemande, statut);
                demandesEnAttente.add(demandeConge);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return demandesEnAttente;
    }

    @FXML
    private void handleTraiterDemande() {
        // Get the selected DemandeConge from the TableView
        DemandeConge selectedDemande = tableDemandeConge.getSelectionModel().getSelectedItem();

        if (selectedDemande != null) {
            String selectedStatut = comboStatut.getValue();

            if (selectedStatut != null && !selectedStatut.isEmpty()) {
                // Show confirmation dialog before proceeding with the update
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation");
                confirmationAlert.setHeaderText("Êtes-vous sûr de vouloir mettre à jour la demande ?");
                confirmationAlert.setContentText("Le statut de la demande sera modifié en : " + selectedStatut);

                // Show the confirmation dialog and capture the result
                confirmationAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            // Ensure valid status before updating
                            StatutDemande statut;
                            try {
                                statut = StatutDemande.valueOf(selectedStatut);
                            } catch (IllegalArgumentException e) {
                                showError("Statut invalide : " + selectedStatut);
                                return;
                            }

                            Connection conn = DBconnection.getInstance().getConn();
                            if (conn == null) {
                                showError("Erreur de connexion à la base de données.");
                                return;
                            }

                            GestionConge.traiterDemandeConge(conn, selectedDemande.getId(), statut);

                            // Show success alert
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Traitement Réussi");
                            successAlert.setContentText("La demande a été mise à jour avec le statut : " + statut);
                            successAlert.showAndWait();

                            // Refresh the table data after update
                            loadDemandes();
                        } catch (Exception e) {
                            showError("Erreur lors du traitement de la demande.");
                            e.printStackTrace();
                        }
                    } else {
                        // If the user cancels, show a cancellation message
                        Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                        cancelAlert.setTitle("Annulation");
                        cancelAlert.setContentText("La mise à jour a été annulée.");
                        cancelAlert.showAndWait();
                    }
                });
            } else {
                showError("Veuillez choisir un statut pour la demande.");
            }
        } else {
            showError("Veuillez sélectionner une demande à traiter.");
        }
    }

    // Method to show error alerts
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
