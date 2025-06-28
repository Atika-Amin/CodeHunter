package com.example.demo;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class GamingModeController {

    @FXML private HBox titleBox;
    @FXML private VBox buttonBox;
    @FXML private Button multiplayerModeButton;
    @FXML private Button singlePlayerModeButton;
    @FXML private Button TypingModeButton;

    @FXML private ImageView knowledgeLevelImage;
    @FXML private ImageView pointsImage;
    @FXML private ImageView logoutIcon;
    @FXML private ImageView settingsIcon;

    @FXML private Label knowledgeLabel;
    @FXML private Label pointsLabel;
    @FXML private StackPane pointsPane;
    @FXML private Label pointsValueLabel;
    @FXML private ImageView musicIcon, homeIcon;
    @FXML private Slider volumeSlider;
    private static boolean serverStarted = false;

    @FXML
    private void initialize() {
        // Animate title and buttons
        applyFadeInAnimation(titleBox);
        applyFadeAndSlideAnimation(buttonBox);
        applyFadeInAnimation(knowledgeLevelImage);
        applyFadeInAnimation(pointsImage);
        applyFadeInAnimation(pointsImage);
        applyFadeInAnimation(pointsPane);
        // Update icon initially based on music state
        updateMusicIcon();

        Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 14);
        // Set initial images (you can fetch values dynamically later)

        // Fetch user data dynamically
        int userId = Session.getCurrentUserId();
        int level = DatabaseConnection.getKnowledgeLevel(userId);
        int points = DatabaseConnection.getPoints(userId);

        // Update session
        Session.setKnowledgeLevel(level);
        Session.setPoints(points);

        // Display on homepage
        updateStatusImages(level, points);
    }


    private void applyFadeInAnimation(javafx.scene.Node node) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.5), node);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    private void applyFadeAndSlideAnimation(javafx.scene.Node node) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.5), node);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1.5), node);
        translateTransition.setFromY(30);
        translateTransition.setToY(0);

        fadeTransition.play();
        translateTransition.play();
    }

    private void updateStatusImages(int level, int points) {
        try {
            String levelImagePath = "/com/example/demo/asset/KnG_Level/level_" + level + ".png";
            String pointsImagePath = "/com/example/demo/asset/points/pointImage.png";

            knowledgeLevelImage.setImage(new Image(getClass().getResourceAsStream(levelImagePath)));
            pointsImage.setImage(new Image(getClass().getResourceAsStream(pointsImagePath)));

            pointsValueLabel.setText(String.valueOf(points));
        } catch (Exception e) {
            System.out.println("Error loading status images: " + e.getMessage());
        }
    }

    @FXML
    private void toggleMusic(MouseEvent event) {
        SoundUtil.playClick();
        MusicManager.toggleMusic();
        updateMusicIcon();
    }

    private void updateMusicIcon() {
        String iconPath = MusicManager.isMusicPlaying()
                ? "/com/example/demo/Frames/music_on.png"
                : "/com/example/demo/Frames/music_off.png";
        musicIcon.setImage(new Image(getClass().getResourceAsStream(iconPath)));
    }
    @FXML
    private void openSettings(MouseEvent event) {
        SoundUtil.playClick(); // Play sound (if you want)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/settings.fxml"));
            Parent settingsRoot = loader.load();
            Stage stage = (Stage) settingsIcon.getScene().getWindow(); // Get current window
            Scene settingsScene = new Scene(settingsRoot);
            stage.setScene(settingsScene);
            stage.setTitle("Settings");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openSinglePlayerMode() {
        SoundUtil.playClick();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/singlePlayer_mode.fxml")); // Correct FXML path
            Parent readingRoot = loader.load();
            Stage stage = (Stage) singlePlayerModeButton.getScene().getWindow();
            Scene readingScene = new Scene(readingRoot);
            stage.setScene(readingScene);
            stage.setTitle("Reading Mode");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void openMultiPlayerMode() {
        SoundUtil.playClick();
        System.out.println("MultiPlayer opened.");
        // Start server here
        startServerInBackground();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/multiplayer_lobby.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) multiplayerModeButton.getScene().getWindow(); // ensure multiplayerModeButton is defined with @FXML
            stage.setScene(new Scene(root));
            stage.setTitle("Multiplayer Lobby");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startServerInBackground() {
        if (!serverStarted) {
            serverStarted = true;
            new Thread(() -> {
                try {
                    MultiplayerServer.main(new String[]{});
                } catch (Exception e) {
                    System.out.println("⚠️ Server might already be running: " + e.getMessage());
                }
            }).start();
        }
    }
    @FXML
    private void openAvatarMode(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/demo/avatar.fxml"));
            Parent avatarRoot = fxmlLoader.load();
            Scene avatarScene = new Scene(avatarRoot);
            Stage avatarStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            avatarStage.setTitle("Avatar Selection");
            avatarStage.setScene(avatarScene);
            avatarStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goBack(MouseEvent event) {
        SoundUtil.playClick();
        try {
            MusicManager.startMusic();
            Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
            Stage stage = (Stage) homeIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
