package GroupSuccess.esprit;

import controllers.home;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class TestFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        String videoFileName = "intro video.mp4";
        URL videoUrl = home.class.getResource("/assets/videos/" + videoFileName);
        if (videoUrl == null) {
            System.err.println("Video file not found: " + videoFileName);
            throw new RuntimeException("Video file not found: " + videoFileName);
        }

        Media media = new Media(videoUrl.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaPlayer.setAutoPlay(true);

        // StackPane pour la vidéo
        StackPane videoRoot = new StackPane();
        videoRoot.getChildren().add(mediaView);

        mediaPlayer.setOnEndOfMedia(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
                Scene nextScene = new Scene(loader.load(), 1200, 700);

                // Transition en fondu
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.1), videoRoot);
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.setOnFinished(event -> primaryStage.setScene(nextScene));
                fadeTransition.play();

                // Charger la scène du login après la vidéo
                Parent loginRoot = loader.load();  // Changer 'root' en 'loginRoot'
                Scene scene = new Scene(loginRoot);
                primaryStage.setTitle("Gestion User");
                primaryStage.setScene(scene);
                primaryStage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        });

        // Redimensionner la scène et afficher la vidéo
        primaryStage.setScene(new Scene(videoRoot, 1200, 700));
        primaryStage.show();
    }
}
