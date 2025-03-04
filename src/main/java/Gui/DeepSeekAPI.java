package Gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import javafx.stage.Stage;

import java.io.StringReader;

public class DeepSeekAPI {

    private static final String API_URL = "http://localhost:11434/api/generate";

    @FXML
    private TextArea promptInput;

    @FXML
    private TextArea responseOutput;

    @FXML
    private Button askButton;

    @FXML
    private void handleAskAI() {
        String prompt = promptInput.getText().trim();
        if (prompt.isEmpty()) {
            responseOutput.setText("Please enter a prompt!");
            return;
        }

        askButton.setDisable(true); // Disable button while processing
        responseOutput.setText("Thinking...");

        new Thread(() -> {
            String response = askAI(prompt);
            javafx.application.Platform.runLater(() -> {
                responseOutput.setText(response);
                askButton.setDisable(false);
            });
        }).start();
    }

    private String askAI(String prompt) {
        StringBuilder fullResponse = new StringBuilder();
        try {
            String json = "{ \"model\": \"deepseek-r1:1.5b\", \"prompt\": \"" + prompt + "\" }";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            // To handle streaming responses
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Create a Gson object with lenient mode enabled
            Gson gson = new GsonBuilder().setLenient().create();
            JsonReader reader = new JsonReader(new StringReader(response.body()));
            reader.setLenient(true);

            // Read the JSON stream
            JsonObject jsonResponse;
            while (reader.hasNext()) {
                jsonResponse = gson.fromJson(reader, JsonObject.class);

                if (jsonResponse.has("done") && jsonResponse.get("done").getAsBoolean()) {
                    break;
                }

                if (jsonResponse.has("response")) {
                    fullResponse.append(jsonResponse.get("response").getAsString());
                }
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }

        return fullResponse.toString();
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
