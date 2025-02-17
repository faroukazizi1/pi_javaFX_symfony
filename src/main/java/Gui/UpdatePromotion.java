package Gui;

import Model.AVS;
import Model.Type_promotion;
import Model.promotion;
import Model.user;
import Service.promotionService;
import Service.userService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Date;
import java.util.List;


public class UpdatePromotion {

    @FXML
    private ComboBox<String> Combo_avs;

    @FXML
    private ComboBox<String> Combo_user;

    @FXML
    private DatePicker Datepicker_date;

    @FXML
    private TextField TFposte;

    @FXML
    private TextField TFsalaire;

    @FXML
    private TextArea Textare_raison;

    @FXML
    private ComboBox<String> combo_type;

    private promotion currentPromotion;

    private user selectedUser;

    private final promotionService service = new promotionService();

    @FXML
    public void initialize() {
        loadusercombobox();
        for (AVS value : AVS.values()) {
            Combo_avs.getItems().add(value.name());
        }
        for(Type_promotion value : Type_promotion.values()) {
            combo_type.getItems().add(value.name());
        }
    }

    public void initData(promotion selectedPromotion) {
        this.currentPromotion = selectedPromotion;

        TFposte.setText(selectedPromotion.getPoste_promo());
        TFsalaire.setText(String.valueOf(selectedPromotion.getNouv_sal()));
        Textare_raison.setText(selectedPromotion.getRaison());
        combo_type.setValue(selectedPromotion.getType_promo());
        Combo_avs.setValue(selectedPromotion.getAvs());
        Datepicker_date.setValue(selectedPromotion.getDate_prom().toLocalDate());

    }

    @FXML
    private void handleUpdatePromotion() {
        try {
            // Vérifier que tous les champs sont remplis
            if (TFposte.getText().isEmpty() || TFsalaire.getText().isEmpty() || Textare_raison.getText().isEmpty() ||
                    Datepicker_date.getValue() == null || combo_type.getValue() == null || Combo_avs.getValue() == null) {

                afficherAlerte("Erreur", "Champs obligatoires", "Veuillez remplir tous les champs avant de mettre à jour la promotion.");
                return;
            }

            // Conversion de la date et du salaire
            Date nouvelleDate = Date.valueOf(Datepicker_date.getValue());
            double nouveauSalaire;

            try {
                nouveauSalaire = Double.parseDouble(TFsalaire.getText());
                if (nouveauSalaire <= 0) {
                    afficherAlerte("Erreur", "Salaire invalide", "Le salaire doit être un nombre positif.");
                    return;
                }
            } catch (NumberFormatException e) {
                afficherAlerte("Erreur", "Salaire invalide", "Veuillez entrer un nombre valide pour le salaire.");
                return;
            }

            // Vérification que la nouvelle date est après l'ancienne date
            if (nouvelleDate.before(currentPromotion.getDate_prom())) {
                afficherAlerte("Erreur", "Date invalide", "La nouvelle date de promotion doit être postérieure à l'ancienne.");
                return;
            }

            // Vérification que le nouveau salaire est supérieur à l'ancien
            if (nouveauSalaire <= currentPromotion.getNouv_sal()) {
                afficherAlerte("Erreur", "Salaire insuffisant", "Le nouveau salaire doit être supérieur à l'ancien.");
                return;
            }

            // Mettre à jour les informations de la promotion
            currentPromotion.setPoste_promo(TFposte.getText());
            currentPromotion.setNouv_sal(nouveauSalaire);
            currentPromotion.setRaison(Textare_raison.getText());
            currentPromotion.setDate_prom(nouvelleDate);
            currentPromotion.setType_promo(combo_type.getValue());
            currentPromotion.setAvs(Combo_avs.getValue());
            currentPromotion.setId_user(selectedUser.getId());

            // Mettre à jour en base de données
            service.update(currentPromotion);

            // Afficher un message de succès
            afficherAlerte("Succès", "Mise à jour réussie", "Les informations de la promotion ont été mises à jour avec succès.");

            // Recharger l'interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPromotion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) TFposte.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Échec de la mise à jour", "Une erreur est survenue lors de la mise à jour de la promotion.");
        }
    }

    // Méthode pour afficher des alertes
    private void afficherAlerte(String titre, String header, String contenu) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(header);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    public void loadusercombobox(){
        userService userService = new userService();
        List<user> users = userService.getAll();

        ObservableList<String> userNames = FXCollections.observableArrayList();
        for (user u : users) {
            userNames.add(u.getNom() + " " + u.getPrenom());
        }

        Combo_user.setItems(userNames);

        Combo_user.setOnAction(event -> handleUserSelection(users));
    }

    private void handleUserSelection(List<user> users) {
        String selectedName = Combo_user.getValue();

        for (user u : users) {
            if ((u.getNom() + " " + u.getPrenom()).equals(selectedName)) {
                selectedUser = u;
                System.out.println(selectedUser.getId());
                break;
            }
        }
    }
}
