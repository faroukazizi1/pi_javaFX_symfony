package Gui;

import Controller.ChatBotController;
import Model.promotion;
import Service.AbsencePenaliteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import Service.promotionService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class AfficherPromotion {

    @FXML
    private TableColumn<promotion, String> Colavantage;

    @FXML
    private TableColumn<promotion, Date> Coldate;

    @FXML
    private TableColumn<promotion, Void> Coldelete;

    @FXML
    private TableColumn<promotion, Integer> Colid;

    @FXML
    private TableColumn<promotion, Integer> Colid_user;

    @FXML
    private TableColumn<promotion, Void> Colmodifier;

    @FXML
    private TableColumn<promotion, String> Colraison;

    @FXML
    private TableColumn<promotion, Double> Colsalaire;

    @FXML
    private TableColumn<promotion, String> Coltype;

    @FXML
    private TableColumn<promotion, String> Colposte;

    @FXML
    private TableColumn<promotion, Void> Colupdate;


    @FXML
    private Button button_back_employe;

    @FXML
    private Button button_ajoutPro;



    @FXML
    private TableView<promotion> Tableview;

    private final promotionService service = new promotionService();

    @FXML
    void initialize() {

        UserSession session = UserSession.getInstance();
        String userRole = session.getRole();
        List<promotion> tab_promotion;

        if ("RHR".equals(userRole)) {
            // Si c'est un Responsable RH, récupérer toutes les promotions
            tab_promotion = service.getAll();
        } else {
            button_back_employe.setVisible(false);
            // Si c'est un Employé, récupérer uniquement ses promotions
            tab_promotion = service.getPromotionsByUserId(session.getUserId());

            // Masquer les colonnes delete et update pour les employés
            Coldelete.setVisible(false);
            Colmodifier.setVisible(false);
            button_ajoutPro.setVisible(false);
        }

        try {
            ObservableList<promotion> observableList = FXCollections.observableList(tab_promotion);
            Tableview.setItems(observableList);
            Colid.setVisible(false);
            Colid_user.setVisible(false);

            Colid.setCellValueFactory(new PropertyValueFactory<>("id"));
            Coltype.setCellValueFactory(new PropertyValueFactory<>("type_promo"));
            Colraison.setCellValueFactory(new PropertyValueFactory<>("raison"));
            Colposte.setCellValueFactory(new PropertyValueFactory<>("poste_promo"));
            Coldate.setCellValueFactory(new PropertyValueFactory<>("date_prom"));
            Colsalaire.setCellValueFactory(new PropertyValueFactory<>("nouv_sal"));
            Colavantage.setCellValueFactory(new PropertyValueFactory<>("avs"));
            Colid_user.setCellValueFactory(new PropertyValueFactory<>("id_user"));

            System.out.println( tab_promotion.size());

        } catch (Exception e) {
            System.out.println( e.getMessage());
        }

        if ("RHR".equals(userRole)) { // Seulement si c'est un Responsable RH
            // Ajouter le bouton de suppression
            Coldelete.setCellFactory(new Callback<TableColumn<promotion, Void>, TableCell<promotion, Void>>() {
                @Override
                public TableCell<promotion, Void> call(TableColumn<promotion, Void> param) {
                    return new TableCell<promotion, Void>() {
                        private final Button btnDelete = new Button("Delete");

                        {
                            btnDelete.setOnAction(event -> {
                                promotion selectedPromotion = getTableView().getItems().get(getIndex());
                                if (selectedPromotion != null) {
                                    deletePromotion(selectedPromotion);
                                }
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(btnDelete);
                            }
                        }
                    };
                }
            });

            // Ajouter le bouton de modification
            Colmodifier.setCellFactory(new Callback<TableColumn<promotion, Void>, TableCell<promotion, Void>>() {
                @Override
                public TableCell<promotion, Void> call(TableColumn<promotion, Void> param) {
                    return new TableCell<promotion, Void>() {
                        private final Button btnUpdate = new Button("Update");

                        {
                            btnUpdate.setOnAction(event -> {
                                promotion selectedPromotion = getTableView().getItems().get(getIndex());
                                if (selectedPromotion != null) {
                                    openUpdatePromotionForm(selectedPromotion);
                                }
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(btnUpdate);
                            }
                        }
                    };
                }
            });
        }

    }


    @FXML
    private void openAddPromotionForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPromotion.fxml")); // Vérifie le bon chemin
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Promotion");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deletePromotion(promotion promotionToDelete) {


        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Are you sure you want to delete this user?");
        confirmation.setContentText("This action cannot be undone.");

        confirmation.showAndWait().ifPresent(response -> {
            if (response.getText().equals("OK")) {
                try {
                    service.delete(promotionToDelete); // Call the delete method from userService
                    Tableview.getItems().remove(promotionToDelete); // Remove user from TableView
                } catch (Exception e) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setContentText("Failed to delete user: " + e.getMessage());
                    error.showAndWait();
                }
            }
        });
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


    private void openUpdatePromotionForm(promotion selectedPromotion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdatePromotion.fxml"));
            Parent root = loader.load();


            UpdatePromotion controller = loader.getController();
            controller.initData(selectedPromotion);

            Stage stage = (Stage) Tableview.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void openFinance(ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ajouterpret.fxml"));
            Parent root = loader.load();

            // If you want to pass data to the new controller, you can do so here:
            // Example: loader.getController().setData(someData);

            // Get the current stage (the window that triggered the event)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene on the stage
            stage.setScene(new Scene(root));

            // Optionally, you can set the title of the new window
            stage.setTitle("Finance - Ajouter Pret");

            // Show the new window
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // You can show an alert here for error handling if needed
        }
    }

    @FXML
    public void openProjects(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/projects.fxml"));
            Parent root = loader.load();

            // Récupérer la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène sur le stage
            stage.setScene(new Scene(root));

            // Définir le titre de la fenêtre
            stage.setTitle("Gestion des Projets");

            // Afficher la nouvelle scène
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Optionnel : Afficher une alerte en cas d'erreur
        }
    }

    @FXML
    public void openGestionAbsence(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatBotInterface.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur du fichier FXML
            ChatBotController controller = loader.getController();

            // Injecter le service dans le contrôleur
            AbsencePenaliteService service = new AbsencePenaliteService();  // Créez votre service
            controller.setService(service);  // Injecter le service dans le contrôleur

            // Récupérer la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène sur le stage
            stage.setScene(new Scene(root));

            // Définir le titre de la fenêtre
            stage.setTitle("Gestion des Absences");

            // Afficher la nouvelle scène
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}