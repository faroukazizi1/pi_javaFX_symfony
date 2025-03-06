package Gui;

import Service.BulletinPaieService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import Model.BulletinPaie;
import Model.TypeConge;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TestController {

    public Button Reclamation_Btn;
    public Button Document_Btn;
    public Button Event_Btn;
    public Button Projects_Btn;
    public Button User_Btn;
    public Button Home_Btn;
    public Button Logout_Btn;

    // Demande de Congé
    @FXML private ComboBox<TypeConge> typeCongeComboBox;
    @FXML private DatePicker debutDatePicker;
    @FXML private DatePicker finDatePicker;
    @FXML private Button btnAjouterConge;
    @FXML private Button btnValider;
    @FXML private Button btnAnnuler;

    // Bulletin de Paie (ListeView)
    @FXML private TextField salaireBaseField;
    @FXML private TextField primeField;
    @FXML private TextField deductionsField;
    @FXML private Button btnGenererPaie;
    @FXML private ListView<String> listeBulletins;

    // Bulletin de Paie (TableView)
    @FXML private DatePicker moisPicker;
    @FXML private Button btnAfficherPaie;
    @FXML private TableView<BulletinPaie> tableBulletinPaie;
    @FXML private TableColumn<BulletinPaie, String> colSalaireBrut;
    @FXML private TableColumn<BulletinPaie, String> colDeductions;
    @FXML private TableColumn<BulletinPaie, String> colSalaireNet;

    // Tabs
    @FXML private TabPane tabPane;
    @FXML private Tab tabConge;
    @FXML private Tab tabBulletinPaie;
    @FXML private Tab tabTraitement;
    @FXML private Tab tabCreationBulletin;

    @FXML
    public void initialize() {
        if (tabPane == null) {
            System.err.println("Error: tabPane is null. Ensure the FXML is properly loaded.");
            return; // Stop execution to avoid NullPointerException
        }

        UserSession session = UserSession.getInstance();
        if (session != null) {
            String role = session.getRole();

            if ("RHR".equals(role)) {
                tabPane.getTabs().removeIf(tab -> tab == tabConge || tab == tabBulletinPaie);
            } else if ("Employe".equals(role)) {
                tabPane.getTabs().removeIf(tab -> tab == tabTraitement || tab == tabCreationBulletin);
            }
        } else {
            System.err.println("No active session found!");
        }



        // Configure ComboBox for Type de Congé
        if (typeCongeComboBox != null) {
            ObservableList<TypeConge> typesConges = FXCollections.observableArrayList(TypeConge.values());
            typeCongeComboBox.setItems(typesConges);
        }

        // Set up Table Columns for Bulletin de Paie
        if (colSalaireBrut != null) {
            colSalaireBrut.setCellValueFactory(new PropertyValueFactory<>("salaireBrut"));
            colDeductions.setCellValueFactory(new PropertyValueFactory<>("deductions"));
            colSalaireNet.setCellValueFactory(new PropertyValueFactory<>("salaireNet"));

            // Formatting for the columns
            colSalaireBrut.setCellFactory(cell -> new TableCell<BulletinPaie, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        BigDecimal value = new BigDecimal(item);
                        setText(value.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }
                }
            });

            colDeductions.setCellFactory(cell -> new TableCell<BulletinPaie, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        BigDecimal value = new BigDecimal(item);
                        setText(value.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }
                }
            });

            colSalaireNet.setCellFactory(cell -> new TableCell<BulletinPaie, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        BigDecimal value = new BigDecimal(item);
                        setText(value.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }
                }
            });
        }
    }

    @FXML
    public void handleValiderAction() {
        System.out.println("Validation triggered!");  // Debugging line
        TypeConge type = typeCongeComboBox.getValue();
        LocalDate debut = debutDatePicker.getValue();
        LocalDate fin = finDatePicker.getValue();

        if (type == null || debut == null || fin == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
            return;
        }

        if (debut.isAfter(fin)) {
            showAlert("Erreur", "La date de début doit être avant la date de fin.", Alert.AlertType.ERROR);
            return;
        }

        showAlert("Succès", "Demande de congé ajoutée avec succès. Type de congé : " + type, Alert.AlertType.INFORMATION);
    }

    @FXML
    public void handleAnnulerAction() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void genererBulletinPaie() {
        String salaireBase = salaireBaseField.getText();
        String prime = primeField.getText();
        String deductions = deductionsField.getText();

        if (salaireBase.isEmpty() || prime.isEmpty() || deductions.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
            return;
        }

        String bulletin = "Salaire: " + salaireBase + " | Prime: " + prime + " | Déductions: " + deductions;
        listeBulletins.getItems().add(bulletin);
        showAlert("Succès", "Bulletin de paie généré.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void afficherBulletinPaie() {
        if (moisPicker.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner un mois.", Alert.AlertType.ERROR);
            return;
        }

        LocalDate selectedDate = moisPicker.getValue();
        int month = selectedDate.getMonthValue();
        int year = selectedDate.getYear();

        // Replace with actual method to fetch bulletins from your service or database
        List<BulletinPaie> bulletins = BulletinPaieService.getBulletinsPaieByMonthAndYear(month, year);
        ObservableList<BulletinPaie> bulletinList = FXCollections.observableArrayList(bulletins);

        tableBulletinPaie.setItems(bulletinList);
        showAlert("Info", "Bulletin de paie affiché pour : " + selectedDate.format(DateTimeFormatter.ofPattern("MM-yyyy")), Alert.AlertType.INFORMATION);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void onHomeButtonClick(ActionEvent actionEvent) {
    }

    public void onUserButtonClick(ActionEvent actionEvent) {
    }

    public void onReclamationButtonClick(ActionEvent actionEvent) {

    }

    public void onDocumentsButtonClick(ActionEvent actionEvent) {
    }

    public void onEventButtonClick(ActionEvent actionEvent) {
    }

    public void onLogoutButtonClick(ActionEvent actionEvent) {
    }

    public void onProjectsButtonClick(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/projects.fxml"));
            Parent root = loader.load();

            // Récupérer la fenêtre actuelle
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

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


}