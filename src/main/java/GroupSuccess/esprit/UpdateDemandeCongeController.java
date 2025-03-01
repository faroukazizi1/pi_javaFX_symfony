package GroupSuccess.esprit;

import Util.DBconnection;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import models.DemandeConge;
import Services.GestionConge;
import models.TypeConge;

public class UpdateDemandeCongeController {

    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private DatePicker dateFinPicker;
    @FXML
    private ComboBox<TypeConge> typeCongeCombo;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnAnnuler;

    private DemandeConge demande;
    private GestionConge gestionConge = new GestionConge(); // Ensure proper instantiation

    public void setDemande(DemandeConge demande) {
        this.demande = demande;
        if (demande != null) {
            dateDebutPicker.setValue(java.time.LocalDate.parse(demande.getDebut()));
            dateFinPicker.setValue(java.time.LocalDate.parse(demande.getFin()));
            typeCongeCombo.setItems(FXCollections.observableArrayList(TypeConge.values()));
        }
    }

    @FXML
    private void handleUpdate() {
        if (demande != null) {
            // Set the fields from UI components
            demande.setDebut(dateDebutPicker.getValue().toString());
            demande.setFin(dateFinPicker.getValue().toString());

            // Get selected type as a string from ComboBox
            String selectedType = String.valueOf(typeCongeCombo.getValue());

            // Check if the value is valid, otherwise handle errors (Optional)
            if (selectedType != null && !selectedType.isEmpty()) {
                demande.setType(selectedType);
            } else {
                System.out.println("Invalid type selected");
                return;
            }

            // Ensure the type is updated correctly in the database
            boolean isUpdated = gestionConge.updateDemandeConge(
                    DBconnection.getInstance().getConn(),
                    demande.getId(),
                    demande.getDebut(),
                    demande.getFin(),
                    TypeConge.valueOf(demande.getType())  // Correct way to map string to enum
            );

            // Check if update was successful
            if (isUpdated) {
                System.out.println("Mise à jour réussie.");
            } else {
                System.out.println("Échec de la mise à jour.");
            }

            closeWindow();
        }
    }


    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnUpdate.getScene().getWindow();
        stage.close();
    }
}
