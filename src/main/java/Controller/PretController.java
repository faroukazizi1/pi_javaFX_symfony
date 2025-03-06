package Controller;

import Service.PretService;
import Service.ReponseService;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Pret;
import models.Reponse;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PretController {

    // Existing fields
    @FXML private TextField montantPretField, tmmField, tauxField, revenusBrutsField, ageEmployeField, dureeRemboursementField;
    @FXML private DatePicker datePretPicker;
    @FXML private ComboBox<String> categorieField;
    @FXML private TextField montantPretFieldModif, tmmFieldModif, tauxFieldModif, revenusBrutsFieldModif, ageEmployeFieldModif, dureeRemboursementFieldModif;
    @FXML private DatePicker datePretPickerModif;
    @FXML private ComboBox<String> categorieFieldModif;
    @FXML private TableView<Pret> pretTableView;
    @FXML private TableColumn<Pret, Float> colMontant, colTmm, colTaux, colRevenus;
    @FXML private TableColumn<Pret, Integer> colAge, colDuree;
    @FXML private TableColumn<Pret, String> colCategorie;
    @FXML private TableColumn<Pret, LocalDate> colDate;
    @FXML private TableColumn<Pret, Void> colActions;
    @FXML private TabPane tabPane;
    @FXML private Tab calendarTab;
    @FXML private VBox calendarLayout;
    @FXML private HBox calendarHeader;
    @FXML private Button prevMonthButton;
    @FXML private Button nextMonthButton;
    @FXML private Label monthLabel;
    @FXML private ComboBox<Integer> yearComboBox;
    @FXML private GridPane calendarGrid;

    private final ObservableList<Pret> listePrets = FXCollections.observableArrayList();
    private final PretService pretService = new PretService();
    private final ReponseService reponseService = new ReponseService();
    private YearMonth currentYearMonth = YearMonth.now();
    private List<Pret> prets;

    @FXML
    public void initialize() {
        // Initialize existing components
        if (categorieField != null) {
            categorieField.setItems(FXCollections.observableArrayList("Fonctionnaire", "Cadre", "Ouvrier"));
        }
        if (categorieFieldModif != null) {
            categorieFieldModif.setItems(FXCollections.observableArrayList("Fonctionnaire", "Cadre", "Ouvrier"));
        }
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montantPret"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("datePret"));
        colTmm.setCellValueFactory(new PropertyValueFactory<>("tmm"));
        colTaux.setCellValueFactory(new PropertyValueFactory<>("taux"));
        colRevenus.setCellValueFactory(new PropertyValueFactory<>("revenusBruts"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("ageEmploye"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("dureeRemboursement"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));

        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");
            private final Button generateButton = new Button("Générer Réponse");
            private final HBox hbox = new HBox(10, updateButton, deleteButton, generateButton);

            {
                updateButton.getStyleClass().add("update-button");
                deleteButton.getStyleClass().add("delete-button");
                generateButton.getStyleClass().add("generate-button");

                updateButton.setOnAction(event -> {
                    Pret pret = getTableView().getItems().get(getIndex());
                    handleUpdatePret(pret);
                });

                deleteButton.setOnAction(event -> {
                    Pret pret = getTableView().getItems().get(getIndex());
                    handleDeletePretFromTable(pret);
                });

                generateButton.setOnAction(event -> {
                    Pret pret = getTableView().getItems().get(getIndex());
                    generateReponseForPret(pret);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });

        pretTableView.setItems(listePrets);

        // Initialize calendar when the "Calendrier" tab is selected
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == calendarTab) {
                prets = pretService.getAll();
                initializeCalendar();
            }
        });
    }

    private void initializeCalendar() {
        List<Integer> years = IntStream.rangeClosed(2000, 2030).boxed().collect(Collectors.toList());
        yearComboBox.setItems(FXCollections.observableArrayList(years));
        yearComboBox.setValue(currentYearMonth.getYear());
        yearComboBox.setOnAction(e -> {
            Integer selectedYear = yearComboBox.getValue();
            if (selectedYear != null) {
                currentYearMonth = YearMonth.of(selectedYear, currentYearMonth.getMonthValue());
                updateCalendar();
            }
        });

        monthLabel.setText(currentYearMonth.getMonth().toString());

        prevMonthButton.setOnAction(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateCalendar();
        });

        nextMonthButton.setOnAction(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateCalendar();
        });

        updateCalendar();
    }

    private void updateCalendar() {
        monthLabel.setText(currentYearMonth.getMonth().toString());
        yearComboBox.setValue(currentYearMonth.getYear());
        calendarGrid.getChildren().clear();
        createCalendarGrid();
    }

    private void createCalendarGrid() {
        String[] daysOfWeek = {"Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"};
        for (int i = 0; i < daysOfWeek.length; i++) {
            Label dayLabel = new Label(daysOfWeek[i]);
            dayLabel.getStyleClass().add("day-label");
            calendarGrid.add(dayLabel, i, 0);
        }

        LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue() - 1;
        int daysInMonth = currentYearMonth.lengthOfMonth();

        YearMonth prevMonth = currentYearMonth.minusMonths(1);
        int daysInPrevMonth = prevMonth.lengthOfMonth();
        int prevMonthStartDay = daysInPrevMonth - dayOfWeek + 1;
        for (int i = 0; i < dayOfWeek; i++) {
            VBox dayBox = new VBox(5);
            dayBox.getStyleClass().add("day-box");
            dayBox.getStyleClass().add("day-box-inactive");
            Label dayLabel = new Label(String.valueOf(prevMonthStartDay + i));
            dayLabel.getStyleClass().add("day-label");
            dayBox.getChildren().add(dayLabel);
            calendarGrid.add(dayBox, i, 1);
        }

        int row = 1;
        int col = dayOfWeek;
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            VBox dayBox = new VBox(5);
            dayBox.getStyleClass().add("day-box");

            Label dayLabel = new Label(String.valueOf(day));
            dayLabel.getStyleClass().add("day-label");

            if (date.equals(LocalDate.now())) {
                dayBox.getStyleClass().add("day-box-today");
            }

            List<Pret> matchingPrets = prets.stream()
                    .filter(pret -> pret.getDatePret().toLocalDate().equals(date))
                    .collect(Collectors.toList());

            if (!matchingPrets.isEmpty()) {
                dayBox.getStyleClass().add("day-box-highlighted");
                Label revenusLabel = new Label("Revenus: " + matchingPrets.get(0).getRevenusBruts());
                revenusLabel.getStyleClass().add("revenus-label");
                dayBox.getChildren().add(revenusLabel);

                dayBox.setOnMouseClicked(event -> showPretDetailsWindow(date, matchingPrets));
            }

            dayBox.getChildren().add(0, dayLabel);
            calendarGrid.add(dayBox, col, row);

            col++;
            if (col == 7) {
                col = 0;
                row++;
            }
        }

        int nextMonthDay = 1;
        while (col != 0 || row <= 5) {
            VBox dayBox = new VBox(5);
            dayBox.getStyleClass().add("day-box");
            dayBox.getStyleClass().add("day-box-inactive");
            Label dayLabel = new Label(String.valueOf(nextMonthDay));
            dayLabel.getStyleClass().add("day-label");
            dayBox.getChildren().add(dayLabel);
            calendarGrid.add(dayBox, col, row);

            nextMonthDay++;
            col++;
            if (col == 7) {
                col = 0;
                row++;
            }
        }
    }

    private void showPretDetailsWindow(LocalDate date, List<Pret> matchingPrets) {
        Stage detailsStage = new Stage();
        detailsStage.setTitle("Prêts du " + date.toString());

        VBox layout = new VBox(10);
        layout.getStyleClass().add("calendar-container");

        Label title = new Label("Revenus Bruts des Prêts du " + date.toString());
        title.getStyleClass().add("month-label");

        ListView<String> revenusList = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Pret pret : matchingPrets) {
            items.add("Revenus Bruts: " + pret.getRevenusBruts());
        }
        revenusList.setItems(items);
        revenusList.setPrefHeight(200);
        revenusList.setPrefWidth(300);

        layout.getChildren().addAll(title, revenusList);

        Scene scene = new Scene(layout, 350, 250);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        detailsStage.setScene(scene);
        detailsStage.show();
    }

    private void generateReponseForPret(Pret pret) {
        try {
            System.out.println("Starting generateReponseForPret for Pret ID: " + pret.getIdPret());

            double montantDemande = pret.getMontantPret();
            int duree = pret.getDureeRemboursement();
            System.out.println("Montant Demande: " + montantDemande + ", Durée: " + duree);

            double tauxInteret = 5.0;
            double tauxMensuel = tauxInteret / 12 / 100;
            double revenusBruts = montantDemande * 0.4;
            double mensualiteCredit = (tauxMensuel > 0) ?
                    (montantDemande * tauxMensuel) / (1 - Math.pow(1 + tauxMensuel, -duree)) :
                    montantDemande / duree;
            double potentielCredit = revenusBruts * 0.3;
            double montantAutorise = potentielCredit * 0.9;
            double assurance = montantDemande * 0.02;

            // Create the Reponse object
            Reponse reponse = new Reponse();
            int pretId = pret.getIdPret();
            System.out.println("Pret ID for Reponse: " + pretId);
            reponse.setIdPret(pretId);
            reponse.setDateReponse(Date.valueOf(LocalDate.now()));
            reponse.setMontantDemande(montantDemande);
            reponse.setRevenusBruts(revenusBruts);
            reponse.setTauxInteret(tauxInteret);
            reponse.setMensualiteCredit(mensualiteCredit);
            reponse.setPotentielCredit(potentielCredit);
            reponse.setDureeRemboursement(duree);
            reponse.setMontantAutorise(montantAutorise);
            reponse.setAssurance(assurance);

            // Log the Reponse object before saving
            System.out.println("Generating Reponse: " + reponse.toString());

            // Save the Reponse
            boolean isSaved = reponseService.saveReponse(reponse);
            System.out.println("Reponse saved successfully: " + isSaved);
            System.out.println("Saved Reponse with ID: " + reponse.getIdReponse());
            System.out.println("Saved Reponse: " + reponse.toString());

            // Verify by retrieving all responses immediately after saving
            List<Reponse> allReponses = reponseService.getAll();
            System.out.println("All Reponses after saving: " + allReponses);

            if (isSaved) {
                // Open a new window to display the response
                showResponseWindow(reponse);
            } else {
                showAlert("Erreur", "Erreur lors de l'enregistrement de la réponse.");
            }
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de la génération de la réponse : " + e.getMessage());
            System.err.println("Exception in generateReponseForPret: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showResponseWindow(Reponse reponse) {
        try {
            // Load the ResponseWindow FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResponseWindow.fxml"));
            Parent root = loader.load();

            // Get the controller and set the Reponse data
            ResponseWindowController controller = loader.getController();
            controller.setReponse(reponse);

            // Create a new stage (window)
            Stage stage = new Stage();
            stage.setTitle("Détails de la Réponse Générée");
            stage.setScene(new Scene(root, 400, 400));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture de la fenêtre de réponse : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleUpdatePret(Pret pret) {
        montantPretFieldModif.setText(String.valueOf(pret.getMontantPret()));
        datePretPickerModif.setValue(pret.getDatePret().toLocalDate());
        tmmFieldModif.setText(String.valueOf(pret.getTmm()));
        tauxFieldModif.setText(String.valueOf(pret.getTaux()));
        revenusBrutsFieldModif.setText(String.valueOf(pret.getRevenusBruts()));
        ageEmployeFieldModif.setText(String.valueOf(pret.getAgeEmploye()));
        dureeRemboursementFieldModif.setText(String.valueOf(pret.getDureeRemboursement()));
        categorieFieldModif.setValue(pret.getCategorie());

        tabPane.getSelectionModel().select(1);
    }

    private void handleDeletePretFromTable(Pret pret) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Voulez-vous vraiment supprimer le prêt ID " + pret.getIdPret() + " ?",
                ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait();

        if (confirmation.getResult() == ButtonType.YES) {
            pretService.delete(pret.getIdPret());
            listePrets.setAll(pretService.getAll());
            showAlert("Succès", "Prêt supprimé avec succès.");

            if (tabPane.getSelectionModel().getSelectedItem() == calendarTab) {
                prets = pretService.getAll();
                updateCalendar();
            }
        }
    }

    @FXML
    private void handleGoToReponse(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reponse.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de la page de réponse : " + e.getMessage());
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

            Pret newPret = new Pret();
            newPret.setMontantPret(montant);
            newPret.setDatePret(Date.valueOf(localDate));
            newPret.setTmm(tmm);
            newPret.setTaux(taux);
            newPret.setRevenusBruts(revenus);
            newPret.setAgeEmploye(age);
            newPret.setDureeRemboursement(duree);
            newPret.setCategorie(categorie);

            pretService.add(newPret);
            listePrets.setAll(pretService.getAll());

            clearAddForm();
            showAlert("Succès", "Prêt ajouté avec succès !");

            if (tabPane.getSelectionModel().getSelectedItem() == calendarTab) {
                prets = pretService.getAll();
                updateCalendar();
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez remplir correctement tous les champs.");
        }
    }

    @FXML
    private void handleEditPret() {
        try {
            Pret selectedPret = pretTableView.getSelectionModel().getSelectedItem();

            if (pretTableView.getSelectionModel().getSelectedItem() == null && !pretTableView.getItems().isEmpty()) {
                pretTableView.getSelectionModel().select(0);
            }
            if (selectedPret == null) {
                showAlert("Erreur", "Veuillez sélectionner un prêt à modifier depuis le tableau.");
                return;
            }

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

            float montant = Float.parseFloat(montantPretFieldModif.getText());
            if (montant <= 0) {
                showAlert("Erreur", "Le montant du prêt doit être supérieur à 0.");
                return;
            }
            selectedPret.setMontantPret(montant);

            LocalDate localDate = datePretPickerModif.getValue();
            if (localDate.isBefore(LocalDate.now())) {
                showAlert("Erreur", "La date du prêt doit être aujourd'hui ou dans le futur.");
                return;
            }
            selectedPret.setDatePret(Date.valueOf(localDate));

            float tmm = Float.parseFloat(tmmFieldModif.getText());
            if (tmm < 0) {
                showAlert("Erreur", "Le TMM ne peut pas être négatif.");
                return;
            }
            selectedPret.setTmm(tmm);

            float taux = Float.parseFloat(tauxFieldModif.getText());
            if (taux < 0) {
                showAlert("Erreur", "Le taux ne peut pas être négatif.");
                return;
            }
            selectedPret.setTaux(taux);

            float revenus = Float.parseFloat(revenusBrutsFieldModif.getText());
            if (revenus <= 0) {
                showAlert("Erreur", "Les revenus doivent être supérieurs à 0.");
                return;
            }
            selectedPret.setRevenusBruts(revenus);

            int age = Integer.parseInt(ageEmployeFieldModif.getText());
            if (age < 18) {
                showAlert("Erreur", "L'âge doit être d'au moins 18 ans.");
                return;
            }
            selectedPret.setAgeEmploye(age);

            int duree = Integer.parseInt(dureeRemboursementFieldModif.getText());
            if (duree <= 0) {
                showAlert("Erreur", "La durée de remboursement doit être supérieure à 0.");
                return;
            } else if (duree > 30) {
                showAlert("Erreur", "La durée de remboursement ne peut pas dépasser 30 ans.");
                return;
            }
            selectedPret.setDureeRemboursement(duree);

            selectedPret.setCategorie(categorieFieldModif.getValue());

            pretService.update(selectedPret);
            listePrets.setAll(pretService.getAll());
            pretTableView.refresh();

            showAlert("Succès", "Prêt modifié avec succès.");

            if (tabPane.getSelectionModel().getSelectedItem() == calendarTab) {
                prets = pretService.getAll();
                updateCalendar();
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides.");
        }
    }

    @FXML
    private void handleViewPrets() {
        List<Pret> pretList = pretService.getAll();
        ObservableList<Pret> pretObservableList = FXCollections.observableArrayList(pretList);
        pretTableView.setItems(pretObservableList);

        if (tabPane.getSelectionModel().getSelectedItem() == calendarTab) {
            prets = pretService.getAll();
            updateCalendar();
        }
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}