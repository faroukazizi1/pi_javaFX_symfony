package controllers;

import Util.DBconnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import io.github.palexdev.materialfx.controls.cell.MFXNotificationCell;
import io.github.palexdev.materialfx.notifications.MFXNotificationCenterSystem;
import io.github.palexdev.materialfx.notifications.MFXNotificationSystem;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class home extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        //for notification
        Platform.runLater(() -> {
            MFXNotificationSystem.instance().initOwner(stage);
            MFXNotificationCenterSystem.instance().initOwner(stage);

            MFXNotificationCenter center = MFXNotificationCenterSystem.instance().getCenter();
            center.setCellFactory(notification -> new MFXNotificationCell(center, notification) {
                {
                    setPrefHeight(400);
                }
            });
        });


        //Connection To Database
        DBconnection.getInstance();

        String videoFileName = "intro video.mp4";
        URL videoUrl = home.class.getResource("/assets/videos/" + videoFileName);

        if (videoUrl == null) {
            throw new RuntimeException("Video file not found: " + videoFileName);
        }
        Media media = new Media(videoUrl.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaPlayer.setAutoPlay(true);

        StackPane root = new StackPane();
        root.getChildren().add(mediaView);

        mediaPlayer.setOnEndOfMedia(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(home.class.getResource("/project/projects.fxml"));
                Scene nextScene = new Scene(fxmlLoader.load(), 1200, 700);


                // Fade Effect
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.1), root);
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.setOnFinished(event -> stage.setScene(nextScene));
                fadeTransition.play();

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
        stage.sizeToScene();
        Scene videoScene = new Scene(root, 1200, 700);
        stage.setScene(videoScene);

        stage.show();
        stage.sizeToScene();
    }

    public static void main(String[] args) {
        launch();
    }
}