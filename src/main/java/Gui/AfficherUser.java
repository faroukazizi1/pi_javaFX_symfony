package Gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import Model.user;
import Service.userService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Gui.UserSession;
import javafx.event.ActionEvent;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.IOException;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class AfficherUser {

    private final userService service = new userService();

    @FXML
    private TableColumn<user, String> Coladdresse;

    @FXML
    private TableColumn<user, Integer> Colcin;

    @FXML
    private TableColumn<user, String> Colemail;

    @FXML
    private TableColumn<user, String> Colnom;

    @FXML
    private TableColumn<user, Integer> Colnumero;

    @FXML
    private TableColumn<user, String> Colpassword;

    @FXML
    private TableColumn<user, String> Colprenom;

    @FXML
    private TableColumn<user, String> Colrole;

    @FXML
    private TableColumn<user, String> Colsexe;

    @FXML
    private TableColumn<user, String> Colusername;

    @FXML
    private TableView<user> tableView;

    @FXML
    private TableColumn<user, Void> Coldelete;

    @FXML
    private TableColumn<user, Void> Colupdate;

    @FXML
    private TableColumn<user, Integer> Colid;

    @FXML
    private Button button_ajouter;

    @FXML
    private TextField searchField;

    private FilteredList<user> filteredData;

    @FXML
    private Button pdf_button;

    @FXML
    private Button stat_button;

    @FXML
    void initialize() {

        UserSession session = UserSession.getInstance();
        if (session != null) {
            String userRole = session.getRole();
            if (!"RHR".equals(userRole)) { // Si ce n'est pas un Responsable RH
                tableView.setVisible(false); // Masquer la table des utilisateurs
                button_ajouter.setVisible(false);

                return;
            }

            // Initialisation normale si c'est un Responsable RH
            try {
                List<user> tab_users = service.getAll();
                ObservableList<user> observableList = FXCollections.observableList(tab_users);

                // Wrap the ObservableList in a FilteredList
                filteredData = new FilteredList<>(observableList, p -> true);

                // Bind the search field text property to the predicate
                searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(user -> {
                        // If filter text is empty, display all users
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Compare user fields with the filter text
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (user.getNom().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (user.getPrenom().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (user.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (user.getUsername().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (user.getRole().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        return false; // Does not match
                    });
                });

                // Wrap the FilteredList in a SortedList
                SortedList<user> sortedData = new SortedList<>(filteredData);

                // Bind the SortedList comparator to the TableView comparator
                sortedData.comparatorProperty().bind(tableView.comparatorProperty());

                // Add sorted (and filtered) data to the table
                tableView.setItems(sortedData);

                Colid.setVisible(false);
                Colid.setCellValueFactory(new PropertyValueFactory<>("id"));
                Colnom.setCellValueFactory(new PropertyValueFactory<>("nom"));
                Colprenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
                Colcin.setCellValueFactory(new PropertyValueFactory<>("cin"));
                Colusername.setCellValueFactory(new PropertyValueFactory<>("username"));
                Colpassword.setCellValueFactory(new PropertyValueFactory<>("password"));
                Colemail.setCellValueFactory(new PropertyValueFactory<>("email"));
                Colrole.setCellValueFactory(new PropertyValueFactory<>("role"));
                Colsexe.setCellValueFactory(new PropertyValueFactory<>("sexe"));
                Coladdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
                Colnumero.setCellValueFactory(new PropertyValueFactory<>("numero"));

                Coladdresse.setCellFactory(column -> new TableCell<user, String>() {
                    private final Hyperlink hyperlink = new Hyperlink();

                    {
                        hyperlink.setOnAction(event -> {
                            String address = getItem();
                            if (address != null && !address.isEmpty()) {
                                openOpenCageMap(address);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            hyperlink.setText(item);
                            setGraphic(hyperlink);
                        }
                    }
                });

                // Ajouter les boutons seulement si l'utilisateur est un Responsable RH
                if ("RHR".equals(userRole)) {
                    addDeleteAndUpdateButtons();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("User session is null!");
        }

        // Bind the PDF button action
        pdf_button.setOnAction(event -> exportToPDF() );
    }

    private void openOpenCageMap(String address) {
        try {
            // Encode the address for the URL
            String encodedAddress = java.net.URLEncoder.encode(address, "UTF-8");

            // Replace YOUR_API_KEY with your actual OpenCage API key
            String apiKey = "dee5490b3f2e46e9a3507c676d139ae5";
            String apiUrl = "https://api.opencagedata.com/geocode/v1/json?q=" + encodedAddress + "&key=" + apiKey;

            // Create a URL object
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject firstResult = jsonResponse.getJSONArray("results").getJSONObject(0);
            JSONObject geometry = firstResult.getJSONObject("geometry");

            // Extract latitude and longitude
            double lat = geometry.getDouble("lat");
            double lng = geometry.getDouble("lng");

            // Open the map in the default browser
            String mapUrl = "https://www.openstreetmap.org/?mlat=" + lat + "&mlon=" + lng + "#map=15/" + lat + "/" + lng;
            java.awt.Desktop.getDesktop().browse(new java.net.URI(mapUrl));

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to open the map. Please check your internet connection or API key.");
            alert.showAndWait();
        }
    }

    public void exportToPDF() {
        String fileName = "Employees_List.pdf";

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);  // ✅ FIXED
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 750);
            contentStream.showText("List of Employees");
            contentStream.endText();

            float yPosition = 730;
            float margin = 50;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float rowHeight = 20;

            // Manually adjusting column widths to allow more space for certain columns like Email, Role, Adresse
            float colWidthCIN = 50;  // CIN
            float colWidthNom = 70;  // Nom
            float colWidthPrenom = 70;  // Prenom
            float colWidthEmail = 120;  // Email (wider)
            float colWidthRole = 60;  // Role
            float colWidthSexe = 60;  // Sexe
            float colWidthNumero = 60;  // Numero
            float colWidthAdresse = 100;  // Adresse (wider)

            // Only the necessary headers
            String[] headers = {"CIN", "Nom", "Prenom", "Email", "Role", "Sexe", "Numero", "Adresse"};

            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);  // ✅ FIXED

            // Print the headers with adjusted widths
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(headers[0]);  // CIN
            contentStream.endText();
            margin += colWidthCIN;

            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(headers[1]);  // Nom
            contentStream.endText();
            margin += colWidthNom;

            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(headers[2]);  // Prenom
            contentStream.endText();
            margin += colWidthPrenom;

            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(headers[3]);  // Email
            contentStream.endText();
            margin += colWidthEmail;

            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(headers[4]);  // Role
            contentStream.endText();
            margin += colWidthRole;

            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(headers[5]);  // Sexe
            contentStream.endText();
            margin += colWidthSexe;

            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(headers[6]);  // Numero
            contentStream.endText();
            margin += colWidthNumero;

            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(headers[7]);  // Adresse
            contentStream.endText();

            contentStream.moveTo(50, yPosition - 5);
            contentStream.lineTo(50 + tableWidth, yPosition - 5);
            contentStream.stroke();

            yPosition -= rowHeight;

            contentStream.setFont(PDType1Font.HELVETICA, 10);  // ✅ FIXED
            // Print the data rows with adjusted column widths
            for (user employee : tableView.getItems()) {
                margin = 50;
                String[] row = {
                        String.valueOf(employee.getCin()), employee.getNom(), employee.getPrenom(),
                        employee.getEmail(), employee.getRole(), employee.getSexe(),
                        String.valueOf(employee.getNumero()), employee.getAdresse()
                };

                // Print each cell in the row with the adjusted widths
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(row[0]);  // CIN
                contentStream.endText();
                margin += colWidthCIN;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(row[1]);  // Nom
                contentStream.endText();
                margin += colWidthNom;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(row[2]);  // Prenom
                contentStream.endText();
                margin += colWidthPrenom;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(row[3]);  // Email
                contentStream.endText();
                margin += colWidthEmail;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(row[4]);  // Role
                contentStream.endText();
                margin += colWidthRole;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(row[5]);  // Sexe
                contentStream.endText();
                margin += colWidthSexe;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(row[6]);  // Numero
                contentStream.endText();
                margin += colWidthNumero;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(row[7]);  // Adresse
                contentStream.endText();

                yPosition -= rowHeight;
                if (yPosition < 50) break;
            }

            contentStream.close();
            document.save(fileName);

            System.out.println("PDF created successfully: " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    // Fonction pour ajouter les boutons Delete et Update
    private void addDeleteAndUpdateButtons() {
        Coldelete.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
                deleteButton.setOnAction(event -> {
                    user selectedUser = getTableView().getItems().get(getIndex());
                    deleteUser(selectedUser);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });

        Colupdate.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");

            {
                updateButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
                updateButton.setOnAction(event -> {
                    user selectedUser = getTableView().getItems().get(getIndex());
                    openUpdateUserForm(selectedUser);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : updateButton);
            }
        });
    }

    private void deleteUser(user selectedUser) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Are you sure you want to delete this user?");
        confirmation.setContentText("This action cannot be undone.");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) { // Use ButtonType.OK for confirmation
                try {
                    service.delete(selectedUser); // Call the delete method from userService
                    tableView.getItems().remove(selectedUser); // Remove user from TableView
                    refreshTable(); // Refresh the table to reflect changes
                } catch (Exception e) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setContentText("Failed to delete user: " + e.getMessage());
                    error.showAndWait();
                }
            }
        });
    }

    private void refreshTable() {
        try {
            List<user> tab_users = service.getAll(); // Fetch updated user list from DB
            ObservableList<user> observableList = FXCollections.observableList(tab_users);
            tableView.setItems(observableList); // Update the TableView
            tableView.refresh(); // Force refresh
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Function to open interface ajouter user
    @FXML
    private void handleAddEmploye(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterUser.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to open the UpdateUser form and pass the selected user
    private void openUpdateUserForm(user selectedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateUser.fxml"));
            Parent root = loader.load();

            UpdateUser controller = loader.getController();
            controller.initData(selectedUser);

            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //function pour ouvrir l'interface d'affichage de promotion
    @FXML
    private void openPromotionForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPromotion.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void DeepSeekAI(ActionEvent event) {
        try {
            // Charger la vue des employés
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DeepSeekAPI.fxml"));  // Assurez-vous que le chemin est correct
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("DeepSeekAI");

        } catch (IOException e) {
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

    @FXML
    public void OpenStatistic(ActionEvent event){
        try {
            // Charger la vue des employés
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stats.fxml"));  // Assurez-vous que le chemin est correct
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Statistics");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}