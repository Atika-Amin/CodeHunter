package com.example.demo;

import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class slime1 {
    private double x, y;
    private double patrolStartX, patrolEndX,patrolStartY,patrolEndY;
    private boolean goingRight = true;
    private ImageView sprite;

    private Image[] frontFrames, backFrames, leftFrames, rightFrames;
    private Image[] walkFrontFrames, walkBackFrames, walkLeftFrames, walkRightFrames;
    private Image deadSprite;
    private Image currentSprite;
    private int currentDirection; // 0: front, 1: back, 2: left, 3: right

    private Image[] hurtFront, hurtBack, hurtLeft, hurtRight;
    private Image[] deadFront, deadBack, deadLeft, deadRight;
    private int hurtFrameCounter = 0;
    private static final int HURT_DISPLAY_TIME = 20; // show hurt sprite for 20 frames
    private Timeline hurtAnimationTimeline;
    private Timeline deathAnimationTimeline;
    private int health; // or whatever value you want
    private boolean isDead = false;
    private boolean isHurt = false;
    private long hurtTimer = 0;
    private boolean countedForPoints = false;






    private int frameIndex = 0;
    private int frameCounter = 0;
    private static final int FRAME_SWITCH_RATE = 15;

    private static final double MOVE_SPEED = 0.8;
    private static final double DETECTION_RADIUS = 50;
    private boolean finishedDeathAnimation = false;

    private boolean chasing = false;



    public slime1( String front, String back, String left, String right,
                   String walkFront, String walkBack, String walkLeft, String walkRight,
                   String hurtFront, String hurtBack, String hurtLeft, String hurtRight,
                   String deadFront, String deadBack, String deadLeft, String deadRight,
                   double x, double y,
                   double patrolStartX, double patrolEndX,
                   double patrolStartY, double patrolEndY,int health) {
        this.x = x;
        this.y = y;
        double patrolDistanceX = 70;  // more wide horizontally
        double patrolDistanceY = 30;  // also some vertical room
        this.patrolStartX = patrolStartX;
        this.patrolEndX = patrolEndX;
        this.patrolStartY = y;  // no vertical patrol
        this.patrolEndY = y;    // no vertical patrol

        this.frontFrames = SpriteLoader.loadFrames(front, 64, 64, 2, 3);
        this.backFrames = SpriteLoader.loadFrames(back, 64, 64, 2, 3);
        this.leftFrames = SpriteLoader.loadFrames(left, 64, 64, 2, 3);
        this.rightFrames = SpriteLoader.loadFrames(right, 64, 64, 2, 3);

        this.walkFrontFrames = SpriteLoader.loadFrames(walkFront, 64, 64, 2, 3);
        this.walkBackFrames = SpriteLoader.loadFrames(walkBack, 64, 64, 2, 3);
        this.walkLeftFrames = SpriteLoader.loadFrames(walkLeft, 64, 64, 2, 3);
        this.walkRightFrames = SpriteLoader.loadFrames(walkRight, 64, 64, 2, 3);

        this.hurtFront = SpriteLoader.loadFrames(hurtFront, 64, 64, 2, 2);
        this.hurtBack = SpriteLoader.loadFrames(hurtBack, 64, 64, 2, 2);
        this.hurtLeft = SpriteLoader.loadFrames(hurtLeft, 64, 64, 2, 2);
        this.hurtRight = SpriteLoader.loadFrames(hurtRight, 64, 64, 2, 2);

        this.deadFront = SpriteLoader.loadFrames(deadFront, 64, 64, 3, 3);
        this.deadBack = SpriteLoader.loadFrames(deadBack, 64, 64, 3, 3);
        this.deadLeft = SpriteLoader.loadFrames(deadLeft, 64, 64, 3, 3);
        this.deadRight = SpriteLoader.loadFrames(deadRight, 64, 64, 3, 3);
        this.health=health;

    }

    public void setHealth(int health1){
        health=health1;
    }

    public void update(Player player) {
        // First check if dead
        if (isDead) {
            Image[] deadFrames;
            switch (currentDirection) {
                case 0: deadFrames = deadFront; break;
                case 1: deadFrames = deadBack; break;
                case 2: deadFrames = deadLeft; break;
                case 3: deadFrames = deadRight; break;
                default: deadFrames = deadFront; break;
            }

            frameCounter++;
            if (frameCounter >= FRAME_SWITCH_RATE) {
                frameCounter = 0;
                frameIndex++;

                if (frameIndex >= deadFrames.length) {
                    // Death animation finished
                    finishedDeathAnimation = true;
                    return;
                }
            }

            if (frameIndex < deadFrames.length) {
                currentSprite = deadFrames[frameIndex];
            }

            return;
        }


        // Check if player is attacking and hits enemy
        if (player.isAttacking() && isCollidingWithPlayer(player)) {
            if (!isHurt) { // Only trigger once per attack
                health--;
                if (health <= 0) {
                    isDead = true;
                    currentSprite = deadSprite;
                    return;
                } else {
                    isHurt = true;
                    hurtTimer = System.currentTimeMillis();
                }
            }
        }




        // Handle HURT duration
        if (isHurt) {
            long elapsed = System.currentTimeMillis() - hurtTimer;
            if (elapsed < 500) { // Show hurt sprite for 1 second
                // Animate hurt frames
                frameCounter++;
                if (frameCounter >= FRAME_SWITCH_RATE) {
                    frameCounter = 0;
                    frameIndex = (frameIndex + 1) % hurtFront.length; // assuming all hurt arrays have same length
                }

                switch (currentDirection) {
                    case 0: currentSprite = hurtFront[frameIndex]; break;
                    case 1: currentSprite = hurtBack[frameIndex]; break;
                    case 2: currentSprite = hurtLeft[frameIndex]; break;
                    case 3: currentSprite = hurtRight[frameIndex]; break;
                    default: currentSprite = hurtFront[frameIndex]; break;
                }
                return;
            } else {
                isHurt = false; // Done showing hurt animation
            }
        }

        // Now Normal Movement (Patrol or Chase)

        double playerX = player.getSprite().getX();
        double playerY = player.getSprite().getY();

        double dx = playerX - x;
        double dy = playerY - y;
        double distance = Math.hypot(dx, dy);

        chasing = distance < DETECTION_RADIUS;

        if (chasing) {
            dx /= distance;
            dy /= distance;

            x += dx * MOVE_SPEED;
            y += dy * MOVE_SPEED;

            if (Math.abs(dx) > Math.abs(dy)) {
                currentDirection = (dx > 0) ? 3 : 2; // Right : Left
            } else {
                currentDirection = (dy > 0) ? 0 : 1; // Front : Back
            }
            if (isCollidingWithPlayer(player)) {
                //SoundManager.playMonsterAttack();
                player.takeDamage(3); // Deal 20 damage to the player
            }
        } else {
            if (goingRight) {
                x += MOVE_SPEED;
                currentDirection = 3; // Right
                if (x >= patrolEndX) goingRight = false;
            } else {
                x -= MOVE_SPEED;
                currentDirection = 2; // Left
                if (x <= patrolStartX) goingRight = true;
            }
        }

        if (x < patrolStartX) {
            x = patrolStartX;
            goingRight = true;
        } else if (x > patrolEndX) {
            x = patrolEndX;
            goingRight = false;
        }

        if (y < patrolStartY) {
            y = patrolStartY;
        } else if (y > patrolEndY) {
            y = patrolEndY;
        }

        // Frame animation
        frameCounter++;
        if (frameCounter >= FRAME_SWITCH_RATE) {
            frameCounter = 0;
            frameIndex = (frameIndex + 1) % 2;
        }

        // Choose animation
        Image[] activeFrames;
        if (chasing) {
            switch (currentDirection) {
                case 0: activeFrames = frontFrames; break;
                case 1: activeFrames = backFrames; break;
                case 2: activeFrames = leftFrames; break;
                case 3: activeFrames = rightFrames; break;
                default: activeFrames = frontFrames; break;
            }
        } else {
            switch (currentDirection) {
                case 0: activeFrames = walkFrontFrames; break;
                case 1: activeFrames = walkBackFrames; break;
                case 2: activeFrames = walkLeftFrames; break;
                case 3: activeFrames = walkRightFrames; break;
                default: activeFrames = walkFrontFrames; break;
            }
        }

        currentSprite = activeFrames[frameIndex];
    }

    public ImageView getSprite() {
        return sprite;
    }

    private boolean isCollidingWithPlayer(Player player) {
        double dx = x - player.getSprite().getX();
        double dy = y - player.getSprite().getY();
        double distance = Math.hypot(dx, dy);
        return distance < 50; // or your collision radius
    }




    public void render(GraphicsContext gc) {
        gc.drawImage(currentSprite, x, y);
    }
    public boolean wasCountedForPoints() {
        return countedForPoints;
    }

    public void markCounted() {
        countedForPoints = true;
    }


    public boolean isAlive() {
        return !isDead;
    }



    public double getX() { return x; }
    public double getY() { return y; }

}
