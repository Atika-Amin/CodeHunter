package com.example.demo;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundManager {

    // Sound effects (short sounds)
    private static final AudioClip attackSound;
    private static final AudioClip explosionSound;
    private static final AudioClip treasureSound;
    //private static final AudioClip enemySound;

    // Background music using MediaPlayer (we'll reuse game_start as background)
    private static MediaPlayer backgroundMusic;
    private static boolean musicPlaying = false;

    static {
        attackSound = loadClip("/assets/sound/player_attack.wav");
        explosionSound = loadClip("/assets/sound/explosion.wav");
        treasureSound = loadClip("/assets/sound/treasure_open.wav");
       // enemySound=loadClip("/assets/sound/monster attack.wav");

    }

    private static AudioClip loadClip(String path) {
        try {
            URL url = SoundManager.class.getResource(path);
            if (url == null) {
                System.err.println("❌ Sound file not found: " + path);
                return null;
            }
            return new AudioClip(url.toExternalForm());
        } catch (Exception e) {
            System.err.println("💥 Error loading sound: " + path);
            e.printStackTrace();
            return null;
        }
    }

    // === Sound Effects ===
    public static void playAttack() {
        if (attackSound != null) attackSound.play();
    }

    public static void playExplosion() {
        if (explosionSound != null) explosionSound.play();
    }

    public static void playTreasureOpen() {
        if (treasureSound != null) treasureSound.play();
    }
//    public static void playMonsterAttack(){
//        if(enemySound!=null) enemySound.play();
//    }


    // === Background Music ===
    public static void playMusic() {
        if (backgroundMusic == null) {
            try {
                // Load the background music (looped)
                String musicPath = SoundManager.class.getResource("/assets/sound/game_start.wav").toExternalForm();
                Media media = new Media(musicPath);
                backgroundMusic = new MediaPlayer(media);
                backgroundMusic.setVolume(0.3);  // Adjust volume as needed
                backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);  // Loop indefinitely
                backgroundMusic.play();
                musicPlaying = true;
            } catch (Exception e) {
                System.err.println("❌ Error playing music: " + e.getMessage());
            }
        } else if (!musicPlaying) {
            // If music is paused, resume it
            backgroundMusic.play();
            musicPlaying = true;
        }
    }

    public static void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();  // Stop the music
            musicPlaying = false;
        }
    }

    public static void pauseMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.pause();  // Pause the music
            musicPlaying = false;
        }
    }

    public static void toggleMusic() {
        if (musicPlaying) {
            pauseMusic();
        } else {
            playMusic();
        }
    }

    public static void setMusicVolume(double volume) {
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(volume);
        }
    }

    public static void setSoundVolume(double volume) {
        if (attackSound != null) attackSound.setVolume(volume);
        if (explosionSound != null) explosionSound.setVolume(volume);
        if (treasureSound != null) treasureSound.setVolume(volume);
        //if (gameStartSound != null) gameStartSound.setVolume(volume);
    }

    // Call this when the game is over or when transitioning to another page
    public static void stopBackgroundMusicOnGameOver() {
        if (musicPlaying) {
            stopMusic();  // Stop background music when the game is over
        }
    }
}

