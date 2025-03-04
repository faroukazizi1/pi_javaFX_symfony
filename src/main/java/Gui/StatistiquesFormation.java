package Gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import Service.FormationService;
import javafx.stage.Stage;


import java.io.IOException;

public class StatistiquesFormation {

    @FXML
    private BarChart<String, Number> chart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    public void initialize() {
        // Exemple de données, à remplacer par des données réelles du service
        FormationService service = new FormationService();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Récupérer les statistiques (mois et nombre de formations)
        var statistiques = service.getStatistiquesParMois();

        for (var entry : statistiques.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Ajouter la série de données au graphique
        chart.getData().add(series);
    }
    // Méthode générique pour gérer les boutons de la sidebar

    @FXML
    private void handleAfficherFormateur(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormateur.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et changer de scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'erreur en affichant la trace
        }
    }
    @FXML
    private void handleAfficherFormation(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormation.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et changer de scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'erreur en affichant la trace
        }
    }
    @FXML
    private void handleBtnCertif(ActionEvent event) {
        try {
            // Charger la nouvelle interface FXML pour les certificats
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GenererCertification.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et mettre à jour son contenu
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'exception en affichant l'erreur dans la console
        }
    }

}
