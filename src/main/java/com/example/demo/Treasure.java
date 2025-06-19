package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Treasure {
    private double x, y;
    private boolean isOpened = false;
    private boolean isAnimating = false; // New flag to control animation
    private ImageView sprite;
    private Image[] openFrames;  // Array to hold frames for opening animation
    private int currentFrame = 0;
    private static final double WIDTH = 21; // width from your Tiled properties
    private static final double HEIGHT = 21; // height from your Tiled properties
    private Image[] frontFrames; // Front frames for closed state
    private Image[] openFrontFrames; // Front frames for opening animation
    private static final int OPEN_ANIMATION_RATE = 5; // Speed of animation
    private double scaleFactor = 1.0; // Adjust according to your scaling logic
    private int frameCounter = 0;
    private int frameIndex = 0;
    private static final int FRAME_SWITCH_RATE = 3;
    private boolean solved = false;

    private Challenge challenge;


    public Treasure(String frontImagePath, String openImagePath, double x, double y, Challenge challenge) {
        this.x = x * scaleFactor;  // Apply scaling if necessary
        this.y = y * scaleFactor;  // Apply scaling if necessary

        this.challenge = challenge;

        // Load the frames for the closed (front) state and opening animation
        this.frontFrames = SpriteLoader.loadFrames(frontImagePath, 32, 32, 1, 1);  // Closed treasure front frames
        this.openFrontFrames = SpriteLoader.loadFrames(openImagePath, 32, 32, 2, 3);  // Opening frames

        // Initial sprite setup (closed treasure)
        this.sprite = new ImageView(frontFrames[0]);  // Default to the first frame (closed)
        sprite.setX(x);
        sprite.setY(y);
        sprite.setFitWidth(WIDTH);
        sprite.setFitHeight(HEIGHT);
    }



    public void update(Player player) {
        // Only trigger the opening animation if the treasure hasn't been opened
        if (!isOpened && isCollidingWithPlayer(player)) {
            System.out.println("🎯 Player collided with treasure! Opening...");
            SoundManager.playTreasureOpen();
            open();
        }
    }

    private boolean isCollidingWithPlayer(Player player) {
        double playerCenterX = player.getSprite().getX() + player.getSprite().getFitWidth() / 2;
        double playerCenterY = player.getSprite().getY() + player.getSprite().getFitHeight() / 2;

        double treasureCenterX = x + WIDTH / 2;
        double treasureCenterY = y + HEIGHT / 2;

        double dx = treasureCenterX - playerCenterX;
        double dy = treasureCenterY - playerCenterY;
        double distance = Math.hypot(dx, dy);
        return distance < 30; // Adjust if needed
    }

    private void open() {
        isOpened = true; // Mark the treasure as opened
        if (!isAnimating) {
            sprite.setImage(openFrontFrames[0]);  // Immediately swap to the first frame of opening animation
            isAnimating = true;
            System.out.println("Instantly swapped to first open frame.");
            animateOpening();  // Start the opening animation
        }
    }
    private boolean challengeShown = false;
    private void animateOpening() {

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(OPEN_ANIMATION_RATE / 60.0), e -> {
                    frameCounter++;
                    if (frameCounter >= FRAME_SWITCH_RATE) {
                        frameCounter = 0;
                        frameIndex++;

                        if (frameIndex < openFrontFrames.length) {
                            sprite.setImage(openFrontFrames[frameIndex]);
                        } else {
                            sprite.setImage(openFrontFrames[openFrontFrames.length - 1]);
                            isAnimating = false; // Animation finished

                        }
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        if (!challengeShown && challenge != null) {
            challengeShown = true;

            ChallengeManager.showChallenge(
                    challenge.getQuestion(),
                    challenge.getExpectedKeywords(),
                    () -> {
                        markSolved();  // ✅ Mark this treasure as solved
                        System.out.println("✅ Challenge solved!");
                    }
            );
        }



    }

    public void render(GraphicsContext gc) {
        // Draw the sprite (treasure) at its x and y position
        gc.drawImage(sprite.getImage(), x, y, WIDTH, HEIGHT);
    }
    public boolean isSolved() {
        return solved;
    }
    public void markSolved() {
        this.solved = true;
    }


    public ImageView getSprite() {
        return sprite;
    }

    public boolean isOpened() {
        return isOpened;
    }
}
