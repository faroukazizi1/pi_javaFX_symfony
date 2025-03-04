package Gui;

import Service.BulletinPaieService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import Model.BulletinPaie;
import Model.TypeConge;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TestController {

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
        // Existing initialization logic...

        // Get the user session
        UserSession session = UserSession.getInstance();
        if (session != null) {
            String role = session.getRole();

            if ("RHR".equals(role)) {
                // Show only Traitement de demandes & Création Bulletin de Paie
                tabPane.getTabs().remove(tabConge);
                tabPane.getTabs().remove(tabBulletinPaie);
            } else if ("Employé".equals(role)) {
                // Show only Congé & Bulletin de Paie
                tabPane.getTabs().remove(tabTraitement);
                tabPane.getTabs().remove(tabCreationBulletin);
            }
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
}
