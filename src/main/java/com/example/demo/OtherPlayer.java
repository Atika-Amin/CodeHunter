package com.example.demo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class OtherPlayer extends Player {

    private Timeline animationTimeline;
    private int currentFrame = 0;
    private String currentAnimation = "down"; // default direction

    private double lastX = 0;
    private double lastY = 0;

    public OtherPlayer(String playerName) {
        // Pass dummy initial position and null tileMap (not needed here)
        super(0, 0, null, playerName);

        // Create a new Timeline for frame updates just like Player
        animationTimeline = new Timeline(new KeyFrame(Duration.millis(200), e -> nextFrame()));
        animationTimeline.setCycleCount(Timeline.INDEFINITE);
        animationTimeline.stop();  // start stopped, only play when moving
    }

    /**
     * Update position called on network message.
     * Animates only if position changed.
     */
    public void updatePosition(double x, double y) {
        double dx = x - getX();
        double dy = y - getY();

        boolean isMoving = (dx != 0) || (dy != 0);

        if (isMoving) {
            // Update direction based on movement delta
            if (Math.abs(dx) > Math.abs(dy)) {
                currentAnimation = dx > 0 ? "right" : "left";
            } else {
                currentAnimation = dy > 0 ? "down" : "up";
            }

            // Set sprite position
            getSprite().setX(x);
            getSprite().setY(y);

            // Start animation if not already running
            if (!animationTimeline.getStatus().equals(Animation.Status.RUNNING)) {
                animationTimeline.play();
            }
        } else {
            // No movement: stop animation, reset frame to idle
            animationTimeline.stop();
            currentFrame = 0;
            updateSprite();

            // Still update position in case it was moved programmatically without animation
            getSprite().setX(x);
            getSprite().setY(y);
        }

        lastX = x;
        lastY = y;
    }


    private void nextFrame() {
        currentFrame = (currentFrame + 1) % 3;  // 3 frames for walking animation
        updateSprite();
    }

    /**
     * Use OtherPlayer's currentAnimation and currentFrame to update sprite image,
     * similar to Player's updateSprite.
     */
    protected void updateSprite() {
        if (animations.containsKey(currentAnimation)) {
            getSprite().setImage(animations.get(currentAnimation)[currentFrame]);
            getSprite().setFitWidth(WALK_SPRITE_SIZE);
            getSprite().setFitHeight(WALK_SPRITE_SIZE);
        }
    }

    /**
     * Render method to draw sprite and player name (called from game loop)
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getSprite().getImage(), getX(), getY(), WALK_SPRITE_SIZE, WALK_SPRITE_SIZE);
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.fillText(getPlayerName(), getX() + WALK_SPRITE_SIZE / 2, getY() - 10);
    }
}
