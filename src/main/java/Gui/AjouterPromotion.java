package Gui;

import Model.AVS;
import Model.Type_promotion;
import Model.promotion;
import Model.user;
import Service.promotionService;
import Service.userService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Date;
import java.util.List;

public class AjouterPromotion {

    @FXML
    private ComboBox<String> TFavs;

    @FXML
    private DatePicker TFdate;

    @FXML
    private TextField TFposte;

    @FXML
    private TextArea TFraison;

    @FXML
    private TextField TFsalaire;

    @FXML
    private ComboBox<String> TFtype_promo;


    @FXML
    private ComboBox<String> TFuser;


    private user  currentUser;
    promotionService service = new promotionService();

    @FXML
    private void initialize() {
        loadusercombobox();
        for (AVS value : AVS.values()) {
            TFavs.getItems().add(value.name()); // Convertir l'Enum en String
        }
        for(Type_promotion value : Type_promotion.values()) {
            TFtype_promo.getItems().add(value.name());
        }
        this.currentUser = new user();
    }

    @FXML
    void ajouterPromotion(ActionEvent event) {

        // Vérification que tous les champs sont remplis
        if (TFposte.getText().isEmpty() || TFsalaire.getText().isEmpty() || TFraison.getText().isEmpty() ||
                TFtype_promo.getValue() == null || TFavs.getValue() == null ||
                TFdate.getValue() == null || TFuser.getValue() == null) {

            afficherAlerte("Erreur", "Tous les champs sont obligatoires.", "Veuillez remplir tous les champs avant d'ajouter la promotion.");
            return;
        }

        double salaire;
        try {
            salaire = Double.parseDouble(TFsalaire.getText());
            if (salaire <= 0) {
                afficherAlerte("Erreur", "Salaire invalide.", "Le salaire doit être un nombre positif.");
                return;
            }
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "Salaire invalide.", "Veuillez entrer un nombre valide pour le salaire.");
            return;
        }

        if (currentUser == null) {
            afficherAlerte("Erreur", "Aucun utilisateur sélectionné.", "Veuillez sélectionner un utilisateur avant d'ajouter la promotion.");
            return;
        }

        Date date = java.sql.Date.valueOf(TFdate.getValue());
        if (date.before(new Date(System.currentTimeMillis()))) {
            afficherAlerte("Erreur", "Date invalide.", "La date de promotion ne peut pas être dans le passé.");
            return;
        }

        // Récupération des valeurs
        String poste = TFposte.getText();
        String raison = TFraison.getText();
        String type_promo = TFtype_promo.getValue();
        String avs = TFavs.getValue();
        int user_id = currentUser.getId();

        // Création et ajout de la promotion
        promotion p = new promotion(type_promo, raison, poste, salaire, date, avs, user_id);

        try {
            service.add(p);
            afficherAlerte("Succès", "Promotion ajoutée.", "La promotion a été ajoutée avec succès.");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPromotion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            afficherAlerte("Erreur", "Une erreur s'est produite.", "Impossible d'ajouter la promotion.");
            e.printStackTrace();
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

        TFuser.setItems(userNames);

        TFuser.setOnAction(event -> handleUserSelection(users));
    }

    private void handleUserSelection(List<user> users) {
        String selectedName = TFuser.getValue();

        // Trouver l'utilisateur correspondant
        for (user u : users) {
            if ((u.getNom() + " " + u.getPrenom()).equals(selectedName)) {
                currentUser = u;
                System.out.println("Utilisateur sélectionné: " + u.getNom() + " (ID: " + u.getId() + ")");
                break;
            }
        }
    }







}
