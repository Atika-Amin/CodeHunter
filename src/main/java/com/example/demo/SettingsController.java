package com.example.demo;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.prefs.Preferences;

public class SettingsController {

    @FXML private Slider musicVolumeSlider;
    @FXML private Slider soundVolumeSlider;
    @FXML private ImageView logoutIcon;
    @FXML private ImageView musicIcon;
    @FXML
    private Group SettingGroup;
    private Preferences preferences;

    private boolean isMusicMuted = false;

    @FXML
    private void initialize() {
        preferences = Preferences.userNodeForPackage(SettingsController.class);

        // Load saved values
        double musicVol = preferences.getDouble("musicVolume", 0.3);
        double soundVol = preferences.getDouble("soundVolume", 0.7);

        musicVolumeSlider.setValue(musicVol * 100);
        soundVolumeSlider.setValue(soundVol * 100);


        // Slider Listeners
        musicVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double volume = newVal.doubleValue() / 100.0;
            if (!isMusicMuted) {
                MusicManager.setVolume(volume);
            }
        });

        soundVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double volume = newVal.doubleValue() / 100.0;
            SoundUtil.setVolume(volume);
        });

        // Optional: Click feedback for logout icon
        logoutIcon.setOnMouseEntered(e -> logoutIcon.setOpacity(0.8));
        logoutIcon.setOnMouseExited(e -> logoutIcon.setOpacity(1.0));


        // Update icon initially based on music state
        updateMusicIcon();
        // Apply zoom-in effect on the group containing the elements
        Platform.runLater(() -> applyZoomInAnimation(SettingGroup)); // Defer animation
    }
    private void applyZoomInAnimation(Group loginGroup) {
        // Create a ScaleTransition for the group (wooden frame, title, and login form)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1.2), loginGroup);
        scaleTransition.setFromX(0.8); // Start smaller (80% of the original size)
        scaleTransition.setFromY(0.8); // Same for Y-axis
        scaleTransition.setToX(1.0);   // End at 100% (original size)
        scaleTransition.setToY(1.0);   // End at 100% (original size)

        // Play the zoom-in transition
        scaleTransition.play();
    }
    @FXML
    private void saveSettings() {
        SoundUtil.playClick();
        double musicVolume = musicVolumeSlider.getValue() / 100.0;
        double soundVolume = soundVolumeSlider.getValue() / 100.0;

        try {
            preferences.putDouble("musicVolume", musicVolume);
            preferences.putDouble("soundVolume", soundVolume);

            MusicManager.setVolume(musicVolume);
            SoundUtil.setVolume(soundVolume);

            showConfirmationAlert();
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    @FXML
    private void handleLogout(MouseEvent event) {
        SoundUtil.playClick();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/Login.fxml"));
            Parent loginRoot = loader.load();
            Stage stage = (Stage) logoutIcon.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backToHomePage(MouseEvent event) throws IOException {
        SoundUtil.playClick();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/home.fxml"));
        Parent homePage = loader.load();
        Scene homeScene = new Scene(homePage);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(homeScene);
        stage.show();
    }


    // Optional feature: Toggle music mute via icon
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

    private void showConfirmationAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Settings Saved");
        alert.setHeaderText("Your settings have been saved successfully.");
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Could not save settings.");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
