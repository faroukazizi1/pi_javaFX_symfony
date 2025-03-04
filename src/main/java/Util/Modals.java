package Util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Modals {

    public static void displayError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void displaySuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean displayConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(title);
        alert.setContentText(message);

        // Return true if the user clicks OK, false if they click Cancel
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }
    public static void displayInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
