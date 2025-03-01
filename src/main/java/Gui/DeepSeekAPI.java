package Gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
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
}
