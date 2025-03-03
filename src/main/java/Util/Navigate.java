package Util;

import controllers.home;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;

public class Navigate {
    private static final int SCREEN_WIDTH = 1200;
    private static final int SCREEN_HEIGHT = 700;

    public static void Setup_Page(String path, Button Btn) throws IOException {
        FXMLLoader loadingFXMLLoader = new FXMLLoader(home.class.getResource("views/loading.fxml"));
        Parent loadingRoot = loadingFXMLLoader.load();
        Stage loadingStage = new Stage();
        loadingStage.initModality(Modality.APPLICATION_MODAL);
        loadingStage.initStyle(StageStyle.TRANSPARENT);
        loadingStage.setScene(new Scene(loadingRoot));
        loadingStage.setX(1100);
        loadingStage.setY(445);
        loadingStage.show();

        Task<Void> loadContentTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                FXMLLoader fxmlLoader = new FXMLLoader(home.class.getResource(path));
                Parent root = fxmlLoader.load();
                Platform.runLater(() -> {
                    // Switch to the loaded content
                    Stage window = (Stage) Btn.getScene().getWindow();
                    window.setScene(new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT));
                    loadingStage.close();
                });
                return null;
            }
        };
        Thread thread = new Thread(loadContentTask);
        thread.setDaemon(true);
        thread.start();
    }

    public static void toHome(Button btn) throws IOException {
        Setup_Page("views/home.fxml", btn);
    }

    public static void toProject(Button btn, String userRole) throws IOException {
        if (userRole.equals("employe"))
            Setup_Page("views/project/project_as_admin.fxml", btn);
        else
            Setup_Page("views/project/project_as_employee.fxml", btn);

    }

    public static void toEvent(Button btn, String userRole) throws IOException {
        if (userRole.equals("employe"))
            Setup_Page("views/events/events.fxml", btn);
        else
            Setup_Page("views/events/eventsfront.fxml", btn);
    }

    public static void toAssociation(Button btn, String userRole) throws IOException {
        if (userRole.equals("employe"))
            Setup_Page("views/association/association.fxml", btn);
        else
            Setup_Page("views/association/DemandeAssociation.fxml", btn);
    }

    public static void toReclamation(Button btn, String userRole) throws IOException {
        if (userRole.equals("employe"))
            Setup_Page("views/reclamation2.fxml", btn);
        else
            Setup_Page("views/reclamations.fxml", btn);
    }

    public static void toUser(Button btn, String userRole) throws IOException {
        if (userRole.equals("employe"))
            Setup_Page("views/users.fxml", btn);
        else
            Setup_Page("views/useraccount.fxml", btn);
    }

    public static void toDocument(Button btn, String userRole) throws IOException {
        if (userRole.equals("employe"))
            Setup_Page("views/documents.fxml", btn);
        else
            Setup_Page("views/documents_requests_space.fxml", btn);
    }
}
