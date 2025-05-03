package com.example.demo;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReadingModeController {

    @FXML private ImageView musicIcon, homeIcon;
    @FXML private ImageView level1Icon, level2Icon, level3Icon, level4Icon, level5Icon;
    @FXML private ImageView lock1, lock2, lock3, lock4, lock5;
    @FXML
    private Group readingGroup;

    private int userMaxLevel = 1; // Default to Level 1 if not found

    public void initialize() {
        // Apply zoom-in effect on the group containing the elements
        applyZoomInAnimation(readingGroup);
        updateMusicIcon();
        fetchUserMaxLevel();
        lockLevelsBasedOnProgress();
    }
    private void applyZoomInAnimation(Group readingGroup) {
        // Create a ScaleTransition for the group (wooden frame, title, and signup form)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1.2), readingGroup);
        scaleTransition.setFromX(0.8); // Start smaller (80% of the original size)
        scaleTransition.setFromY(0.8); // Same for Y-axis
        scaleTransition.setToX(1.0);   // End at 100% (original size)
        scaleTransition.setToY(1.0);   // End at 100% (original size)

        // Play the zoom-in transition
        scaleTransition.play();
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

    private void fetchUserMaxLevel() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT knowledge_level FROM user_progress WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Session.getCurrentUserId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userMaxLevel = rs.getInt("knowledge_level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void lockLevelsBasedOnProgress() {
        // Unlock Level 1 - Always available, no need for lock
        level1Icon.setOpacity(1.0);  // Make it fully visible
        lock1.setVisible(false);     // Hide the lock icon

        // Unlock levels based on user's progress
        if (userMaxLevel >= 1) {
            level2Icon.setOpacity(1.0); // Make Level 2 fully visible
            lock2.setVisible(false);    // Hide the lock icon
        } else {
            level2Icon.setOpacity(0.5); // Dim Level 2 if not unlocked
            lock2.setVisible(true);     // Show the lock icon
        }

        if (userMaxLevel >= 2) {
            level3Icon.setOpacity(1.0); // Make Level 3 fully visible
            lock3.setVisible(false);    // Hide the lock icon
        } else {
            level3Icon.setOpacity(0.5); // Dim Level 3 if not unlocked
            lock3.setVisible(true);     // Show the lock icon
        }

        if (userMaxLevel >= 3) {
            level4Icon.setOpacity(1.0); // Make Level 4 fully visible
            lock4.setVisible(false);    // Hide the lock icon
        } else {
            level4Icon.setOpacity(0.5); // Dim Level 4 if not unlocked
            lock4.setVisible(true);     // Show the lock icon
        }

        if (userMaxLevel >= 4) {
            level5Icon.setOpacity(1.0); // Make Level 5 fully visible
            lock5.setVisible(false);    // Hide the lock icon
        } else {
            level5Icon.setOpacity(0.5); // Dim Level 5 if not unlocked
            lock5.setVisible(true);     // Show the lock icon
        }
    }


    @FXML
    private void openLevel1(MouseEvent event) {

        openLevel("Level1.fxml");
    }

    @FXML
    private void openLevel2(MouseEvent event) {
        level2Icon.setOpacity(1.0);  // Fully visible
        lock2.setVisible(false);
        if (userMaxLevel >= 1) openLevel("Level2.fxml");
    }

    @FXML
    private void openLevel3(MouseEvent event) {

        level3Icon.setOpacity(1.0);  // Fully visible
        lock3.setVisible(false);
        if (userMaxLevel >= 2) openLevel("Level3.fxml");
    }

    @FXML
    private void openLevel4(MouseEvent event) {
        level4Icon.setOpacity(1.0);  // Fully visible
        lock4.setVisible(false);
        if (userMaxLevel >= 3) openLevel("Level4.fxml");
    }

    @FXML
    private void openLevel5(MouseEvent event) {
        level5Icon.setOpacity(1.0);  // Fully visible
        lock5.setVisible(false);
        if (userMaxLevel >= 4) openLevel("Level5.fxml");
    }

    private void openLevel(String fxmlFile) {
        SoundUtil.playClick();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) homeIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack(MouseEvent event) {
        SoundUtil.playClick();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
            Stage stage = (Stage) homeIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
