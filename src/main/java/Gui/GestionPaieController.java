package Gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Model.BulletinPaie;
import Service.BulletinPaieService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class GestionPaieController {

    @FXML
    private TableView<BulletinPaie> tablePaie;

    @FXML
    private TableColumn<BulletinPaie, String> moisColumn;

    @FXML
    private TableColumn<BulletinPaie, Integer> anneeColumn;

    @FXML
    private TableColumn<BulletinPaie, BigDecimal> salaireBrutColumn;

    @FXML
    private TableColumn<BulletinPaie, BigDecimal> deductionsColumn;

    @FXML
    private TableColumn<BulletinPaie, BigDecimal> salaireNetColumn;

    @FXML
    private DatePicker datePicker;
    @FXML
    public void initialize() {
        moisColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getMois()));
        anneeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAnnee()));
        salaireBrutColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSalaireBrut()));
        deductionsColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDeductions()));
        salaireNetColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSalaireNet()));
    }

    @FXML
    public void handleAfficherAction() {
        LocalDate selectedDate = datePicker.getValue();

        if (selectedDate != null) {
            int month = selectedDate.getMonthValue();
            int year = selectedDate.getYear();
            System.out.println("Selected month: " + month + ", year: " + year);

            List<BulletinPaie> bulletins = BulletinPaieService.getBulletinsPaieByMonthAndYear(month, year);

            if (bulletins.isEmpty()) {
                System.out.println("No bulletins found for the selected month and year.");
            } else {
                System.out.println("Found " + bulletins.size() + " bulletins for the selected month and year.");
            }

            ObservableList<BulletinPaie> observableBulletins = FXCollections.observableArrayList(bulletins);
            tablePaie.setItems(observableBulletins);
        } else {
            showDateSelectionAlert();
        }
    }



    private void showDateSelectionAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Missing Date");
        alert.setHeaderText(null);
        alert.setContentText("Veuillez sélectionner une date!");

        alert.showAndWait();
    }

    @FXML
    public void handleRetourAction() {

        datePicker.setValue(null);
        BulletinPaie selectedBulletin = tablePaie.getSelectionModel().getSelectedItem();

        if (selectedBulletin != null) {
            tablePaie.getItems().remove(selectedBulletin);
            System.out.println("Selected BulletinPaie removed from the TableView.");
        } else {
            System.out.println("No item selected to remove.");
        }
    }
}
