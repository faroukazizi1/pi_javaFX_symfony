package Gui;

import Model.promotion;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import Service.promotionService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
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
    private TableColumn<promotion, Void> ColQr;

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

            ColQr.setCellFactory(new Callback<TableColumn<promotion, Void>, TableCell<promotion, Void>>() {
                @Override
                public TableCell<promotion, Void> call(TableColumn<promotion, Void> param) {
                    return new TableCell<promotion, Void>() {
                        private final Button btnQR = new Button("QR Code");

                        {
                            btnQR.setOnAction(event -> {
                                promotion selectedPromotion = getTableView().getItems().get(getIndex());
                                if (selectedPromotion != null) {
                                    generateQRCode(selectedPromotion);
                                }
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(btnQR);
                            }
                        }
                    };
                }
            });


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

    private void generateQRCode(promotion promo) {
        String data = "Promotion: " + promo.getType_promo() +
                "\nPoste: " + promo.getPoste_promo() +
                "\nSalaire: " + promo.getNouv_sal() +
                "\nDate: " + promo.getDate_prom();

        String filePath = "qr_promotion.png";
        int width = 300;
        int height = 300;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("QR Code généré");
            info.setHeaderText("Le QR Code a été généré !");
            info.setContentText("Vous pouvez scanner le fichier 'qr_promotion.png'.");
            info.showAndWait();

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Logout(ActionEvent event) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Logout");
        confirmation.setHeaderText("Are you sure you want to logout?");
        confirmation.setContentText("This action cannot be undone.");

        // Adding explicit OK and CANCEL buttons
        confirmation.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {  // More reliable check
                try {
                    // Clearing the user session safely
                    UserSession session = UserSession.getInstance();
                    if (session != null) {
                        session.cleanUserSession();
                    }

                    // Switch to the login screen
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Login");

                } catch (IOException e) {
                    System.err.println("Error loading Login.fxml: " + e.getMessage()); // Logging
                    showErrorDialog("Failed to load login screen. Please try again.");
                } catch (Exception e) {
                    System.err.println("Unexpected error during logout: " + e.getMessage()); // Logging
                    showErrorDialog("An unexpected error occurred. Please restart the application.");
                }
            }
        });
    }

    // Helper method for error messages
    private void showErrorDialog(String message) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText(null);
        error.setContentText(message);
        error.showAndWait();
    }



}