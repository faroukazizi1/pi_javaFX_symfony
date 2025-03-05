package Gui;

import Model.promotion;
import Model.user;
import Service.promotionService;
import Service.userService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class stats {

    @FXML
    private BarChart<String, Number> Barchart;

    @FXML
    private LineChart<String, Number> LineChart;

    @FXML
    private PieChart PieChart;

    private userService userService;
    private promotionService promotionService;

    public stats() {
        userService = new userService();
        promotionService = new promotionService();
    }

    @FXML
    public void initialize() {
        loadPieChartData();
        loadBarChartData();
        loadLineChartData();
    }

    private void loadPieChartData() {

        List<user> users = userService.getAll();
        int maleCount = 0, femaleCount = 0;

        for (user u : users) {
            if (u.getSexe().equalsIgnoreCase("Homme")) {
                maleCount++;
            } else if (u.getSexe().equalsIgnoreCase("Femme")) {
                femaleCount++;
            }
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Male", maleCount),
                new PieChart.Data("Female", femaleCount)
        );

        PieChart.setData(pieChartData);
    }

    private void loadBarChartData() {

        List<user> users = userService.getAll();
        ObservableList<BarChart.Data<String, Number>> barChartData = FXCollections.observableArrayList();

        for (user u : users) {
            List<promotion> promotions = promotionService.getPromotionsByUserId(u.getId());
            barChartData.add(new BarChart.Data<>(u.getNom(), promotions.size()));
        }

        Barchart.getData().add(new BarChart.Series<>("Promotions per Employee", barChartData));
    }

    private void loadLineChartData() {

        List<promotion> promotions = promotionService.getAll();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Promotions Over Time");

        // Count promotions by year
        for (promotion p : promotions) {
            String year = String.valueOf(p.getDate_prom().toLocalDate().getYear());
            boolean yearFound = false;

            for (XYChart.Data<String, Number> data : series.getData()) {
                if (data.getXValue().equals(year)) {
                    data.setYValue(data.getYValue().intValue() + 1);
                    yearFound = true;
                    break;
                }
            }

            if (!yearFound) {
                series.getData().add(new XYChart.Data<>(year, 1));
            }
        }

        LineChart.getData().add(series);
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
}
