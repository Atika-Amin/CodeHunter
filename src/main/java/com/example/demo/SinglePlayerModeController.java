package com.example.demo;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SinglePlayerModeController {

    @FXML private ImageView musicIcon, homeIcon;
    @FXML private ImageView level1Icon, level2Icon, level3Icon, level4Icon, level5Icon;
    @FXML private ImageView lock1, lock2, lock3, lock4, lock5;
    @FXML
    private Group readingGroup;

    private int userMaxLevel = 0;
    private int gameLevel = 0;// Default to Level 1 if not found

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
            String sql = "SELECT knowledge_level, max_level FROM user_progress WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Session.getCurrentUserId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userMaxLevel = rs.getInt("knowledge_level");
                gameLevel = rs.getInt("max_level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void lockLevelsBasedOnProgress() {
        if (userMaxLevel >= 1 && gameLevel >= 0) {
            level1Icon.setOpacity(1.0);
            lock1.setVisible(false);
        } else {
            level1Icon.setOpacity(0.5);
            lock1.setVisible(true);
        }

        if (userMaxLevel >= 2 && gameLevel >= 1) {
            level2Icon.setOpacity(1.0);
            lock2.setVisible(false);
        } else {
            level2Icon.setOpacity(0.5);
            lock2.setVisible(true);
        }

        if (userMaxLevel >= 3 && gameLevel >= 2) {
            level3Icon.setOpacity(1.0);
            lock3.setVisible(false);
        } else {
            level3Icon.setOpacity(0.5);
            lock3.setVisible(true);
        }

        if (userMaxLevel >= 4 && gameLevel >= 3) {
            level4Icon.setOpacity(1.0);
            lock4.setVisible(false);
        } else {
            level4Icon.setOpacity(0.5);
            lock4.setVisible(true);
        }

        if (userMaxLevel >= 5 && gameLevel >= 4) {
            level5Icon.setOpacity(1.0);
            lock5.setVisible(false);
        } else {
            level5Icon.setOpacity(0.5);
            lock5.setVisible(true);
        }
    }



    @FXML
    private void openLevel1(MouseEvent event) {
        level1Icon.setOpacity(1.0);  // Fully visible
        lock1.setVisible(false);
        if (userMaxLevel >= 1 && gameLevel >= 0) openMapLevel(1);
    }

    @FXML
    private void openLevel2(MouseEvent event) {
        level2Icon.setOpacity(1.0);  // Fully visible
        lock2.setVisible(false);
        if (userMaxLevel >= 2 && gameLevel >= 1) openMapLevel(2);
    }

    @FXML
    private void openLevel3(MouseEvent event) {

        level3Icon.setOpacity(1.0);  // Fully visible
        lock3.setVisible(false);
        if (userMaxLevel >= 3 && gameLevel >= 2) openMapLevel(3);
    }

    @FXML
    private void openLevel4(MouseEvent event) {
        level4Icon.setOpacity(1.0);  // Fully visible
        lock4.setVisible(false);
        if (userMaxLevel >= 4 && gameLevel >= 3) openMapLevel(4);
    }

    @FXML
    private void openLevel5(MouseEvent event) {
        level5Icon.setOpacity(1.0);  // Fully visible
        lock5.setVisible(false);
        if (userMaxLevel >= 5 && gameLevel >= 4) openMapLevel(5);
    }

    private void openMapLevel(int level) {
        SoundUtil.playClick();
        Stage stage = (Stage) homeIcon.getScene().getWindow();

        try {
            MusicManager.pauseMusic();
            switch (level) {
                case 1:
                    new Map1Game(stage).start(); break;
                case 2:
                    new Map2Game(stage).start(); break;
                case 3:
                    new Map3Game(stage).start(); break;
                case 4:
                    new Map4Game(stage).start(); break;
                case 5:
                    new Map5Game(stage).start(); break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    private void goBack(MouseEvent event) {
        SoundUtil.playClick();
        try {
            MusicManager.startMusic();
            Parent root = FXMLLoader.load(getClass().getResource("gaming_mode.fxml"));
            Stage stage = (Stage) homeIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
