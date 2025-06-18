package com.example.demo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Professor {
    private double x, y;
    private boolean isMoving = true;

    private Image[] walkLeftFrames, walkRightFrames;
    private Image[] idleLeftFrames, idleRightFrames;
    private Image currentSprite;

    // Patrol boundaries
    private double patrolStartX;
    private double patrolEndX;

    private boolean movingRight = true;

    private int frameIndex = 0;
    private int frameCounter = 0;
    private static final int FRAME_SWITCH_RATE = 15;

    private static final double MOVE_SPEED = 0.8;
    private static final double DETECTION_RADIUS = 25;  // Distance at which the professor will stop moving

    public Professor(String walkLeft, String walkRight, String idleLeft, String idleRight, double x, double y, double patrolStartX, double patrolEndX) {
        this.x = x;
        this.y = y;
        this.patrolStartX = patrolStartX;
        this.patrolEndX = patrolEndX;

        this.walkLeftFrames = SpriteLoader.loadFrames(walkLeft, 32, 32, 2, 3);
        this.walkRightFrames = SpriteLoader.loadFrames(walkRight, 32, 32, 2, 3);
        this.idleLeftFrames = SpriteLoader.loadFrames(idleLeft, 32, 32, 1, 1);
        this.idleRightFrames = SpriteLoader.loadFrames(idleRight, 32, 32, 1, 1);

        this.currentSprite = walkLeftFrames[0];
    }

    private boolean goingRight = true; // Patrol direction flag

    public void update(Player player) {
        double playerX = player.getSprite().getX();
        double playerY = player.getSprite().getY();

        double dx = playerX - x;
        double dy = playerY - y;
        double distance = Math.hypot(dx, dy);

        // Check if player is within detection radius
        if (distance < DETECTION_RADIUS) {
            // Stop moving and switch to idle state
            isMoving = false;
        } else {
            // Continue patrolling
            isMoving = true;
        }

        // Patrol logic for horizontal movement (left-right movement)
        if (isMoving) {
            if (goingRight) {
                x += MOVE_SPEED;
                if (x >= patrolEndX) {
                    goingRight = false; // Reverse direction when reaching right boundary
                }
            } else {
                x -= MOVE_SPEED;
                if (x <= patrolStartX) {
                    goingRight = true; // Reverse direction when reaching left boundary
                }
            }

            // Set the walking animation based on patrol direction
            if (goingRight) {
                currentSprite = walkRightFrames[frameIndex]; // Walking right
            } else {
                currentSprite = walkLeftFrames[frameIndex]; // Walking left
            }

            // Frame switching logic for walking animation
            frameCounter++;
            if (frameCounter >= FRAME_SWITCH_RATE) {
                frameCounter = 0;
                frameIndex = (frameIndex + 1) % walkRightFrames.length; // Update frame for animation
            }
        } else {
            // If the professor is idle (e.g., detecting the player), use idle frames
            if (goingRight) {
                currentSprite = idleRightFrames[0]; // Idle right (single frame)
            } else {
                currentSprite = idleLeftFrames[0]; // Idle left (single frame)
            }
        }

        // Ensure professor stays within patrol bounds (horizontal boundaries only)
        if (x < patrolStartX) {
            x = patrolStartX;
            goingRight = true; // Reverse direction if out of bounds
        } else if (x > patrolEndX) {
            x = patrolEndX;
            goingRight = false; // Reverse direction if out of bounds
        }
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(currentSprite, x, y);
    }
    public boolean isNearPlayer(Player player) {
        double dx = this.x - player.getX();
        double dy = this.y - player.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance < 25; // You can adjust this range (50 pixels is a good start)
    }


}
