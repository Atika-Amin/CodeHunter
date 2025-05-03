package com.example.demo;

import javafx.scene.media.AudioClip;

import java.net.URL;

public class SoundUtil {

    private static final AudioClip clickSound;
    private static final AudioClip typingSound;

    private static double volume = 0.7; // Default volume
    private static long lastTypingTime = 0;
    private static final long TYPING_COOLDOWN_MS = 150;

    static {
        AudioClip tempClick = null;
        AudioClip tempTyping = null;

        try {
            URL clickURL = SoundUtil.class.getResource("/com/example/demo/Audio_Music/mouse_click.wav");
            URL typingURL = SoundUtil.class.getResource("/com/example/demo/Audio_Music/keyboard.wav");

            if (clickURL == null) {
                System.err.println("❌ mouse_click.wav not found at /com/example/demo/Audio/");
            } else {
                tempClick = new AudioClip(clickURL.toExternalForm());
            }

            if (typingURL == null) {
                System.err.println("❌ keyboard.wav not found at /com/example/demo/Audio/");
            } else {
                tempTyping = new AudioClip(typingURL.toExternalForm());
            }

        } catch (Exception e) {
            System.err.println("💥 Error initializing sounds: " + e.getMessage());
            e.printStackTrace();
        }

        clickSound = tempClick;
        typingSound = tempTyping;

        setVolume(volume); // Set initial volume
    }

    public static void setVolume(double v) {
        volume = v;
        if (clickSound != null) clickSound.setVolume(volume);
        if (typingSound != null) typingSound.setVolume(volume * 0.5); // half volume for typing
    }

    public static double getVolume() {
        return volume;
    }

    public static void playClick() {
        if (clickSound != null) {
            clickSound.play();
        }
    }

    public static void playTyping() {
        long now = System.currentTimeMillis();
        if (now - lastTypingTime >= TYPING_COOLDOWN_MS) {
            if (typingSound != null) typingSound.play();
            lastTypingTime = now;
        }
    }
}
