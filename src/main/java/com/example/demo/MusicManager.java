package com.example.demo;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
public class MusicManager {

    private static MediaPlayer mediaPlayer;
    private static boolean isPlaying = false;

    public static void startMusic() {
        if (mediaPlayer == null) {
            try {
                String path = MusicManager.class.getResource("/com/example/demo/Audio_Music/background_Music.wav").toExternalForm();
                Media media = new Media(path);
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setVolume(0.3); // Initial volume (adjust as needed)
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                mediaPlayer.play();
                isPlaying = true;
            } catch (Exception e) {
                System.out.println("Music Error: " + e.getMessage());
            }
        } else if (!isPlaying) {
            mediaPlayer.play();
            isPlaying = true;
        }
    }

    public static void pauseMusic() {
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    public static void toggleMusic() {
        if (isPlaying) {
            pauseMusic();
        } else {
            startMusic();
        }
    }

    public static boolean isMusicPlaying() {
        return isPlaying;
    }

    public static void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public static double getVolume() {
        return mediaPlayer != null ? mediaPlayer.getVolume() : 0.3;
    }

}
