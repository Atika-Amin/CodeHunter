package com.example.demo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private static final int WALK_SPRITE_SIZE = 32;   // Walking sprite size
    private static final int ATTACK_SPRITE_SIZE = 52; // Attack sprite size
    private static final int FRAME_COUNT = 3;         // 3 frames per movement
    private static final int ATTACK_FRAME_COUNT = 2;  // 2 frames per attack
    private static final double SPEED = 4.5;
    private static final int FRAME_DURATION = 200;

    private ImageView sprite;
    private Map<String, Image[]> animations;
    private String currentAnimation = "down"; // Default animation (down walking)
    private int currentFrame = 0;
    private Timeline animationTimeline;
    private TileMap tileMap;
    private int health = 4000;      // Player starts with 100 health
    private boolean isAlive = true; // Player alive status

    public Player(double x, double y, TileMap tileMap) {
        this.tileMap = tileMap;
        sprite = new ImageView();
        sprite.setX(x);
        sprite.setY(y);
        sprite.setFitWidth(WALK_SPRITE_SIZE);  // Initial size for walking
        sprite.setFitHeight(WALK_SPRITE_SIZE); // Initial size for walking

        loadAnimations();
        updateSprite();

        animationTimeline = new Timeline(new KeyFrame(Duration.millis(FRAME_DURATION), e -> nextFrame()));
        animationTimeline.setCycleCount(Animation.INDEFINITE);
    }
    public double getX() {
        return sprite.getX();
    }

    public double getY() {
        return sprite.getY();
    }

    private void loadAnimations() {
        animations = new HashMap<>();

        // Movement animations (32x32)
        animations.put("down", splitMovementSpritesheet("/assets/player_sprites/down.png"));
        animations.put("up", splitMovementSpritesheet("/assets/player_sprites/top.png"));
        animations.put("left", splitMovementSpritesheet("/assets/player_sprites/left.png"));
        animations.put("right", splitMovementSpritesheet("/assets/player_sprites/right.png"));

        // Attack animations (64x64)
        animations.put("attack_down", splitAttackSpritesheet("/assets/player_sprites/a-down.png"));
        animations.put("attack_up", splitAttackSpritesheet("/assets/player_sprites/a-top.png"));
        animations.put("attack_left", splitAttackSpritesheet("/assets/player_sprites/a-left.png"));
        animations.put("attack_right", splitAttackSpritesheet("/assets/player_sprites/a-right.png"));
    }

    // Split the walking spritesheet (32x32)
    private Image[] splitMovementSpritesheet(String path) {
        Image spritesheet = new Image(getClass().getResourceAsStream(path));
        Image[] frames = new Image[FRAME_COUNT];

        for (int i = 0; i < FRAME_COUNT; i++) {
            frames[i] = new WritableImage(spritesheet.getPixelReader(),
                    i * WALK_SPRITE_SIZE, 0, WALK_SPRITE_SIZE, WALK_SPRITE_SIZE);
        }
        return frames;
    }

    // Split the attacking spritesheet (64x64)
    private Image[] splitAttackSpritesheet(String path) {
        Image spritesheet = new Image(getClass().getResourceAsStream(path));
        Image[] frames = new Image[ATTACK_FRAME_COUNT];

        for (int i = 0; i < ATTACK_FRAME_COUNT; i++) {
            frames[i] = new WritableImage(spritesheet.getPixelReader(),
                    i * ATTACK_SPRITE_SIZE, 0, ATTACK_SPRITE_SIZE, ATTACK_SPRITE_SIZE);
        }
        return frames;
    }

    private void updateSprite() {
        if (animations.containsKey(currentAnimation)) {
            sprite.setImage(animations.get(currentAnimation)[currentFrame]);

            // Adjust sprite size based on current animation (walk or attack)
            if (currentAnimation.startsWith("attack")) {
                sprite.setFitWidth(ATTACK_SPRITE_SIZE);
                sprite.setFitHeight(ATTACK_SPRITE_SIZE);
            } else {
                sprite.setFitWidth(WALK_SPRITE_SIZE);
                sprite.setFitHeight(WALK_SPRITE_SIZE);
            }
        }
    }

    private void nextFrame() {
        if (currentAnimation.startsWith("attack")) {
            currentFrame = (currentFrame + 1) % ATTACK_FRAME_COUNT;
        } else {
            currentFrame = (currentFrame + 1) % FRAME_COUNT;
        }
        updateSprite();
    }

    public void handleKeyPress(KeyCode key) {
        switch (key) {
            case W:
                move(0, -SPEED, "up");
                break;
            case S:
                move(0, SPEED, "down");
                break;
            case A:
                move(-SPEED, 0, "left");
                break;
            case D:
                move(SPEED, 0, "right");
                break;
        }
        animationTimeline.play();
    }

    public void handleKeyRelease(KeyCode key) {
        animationTimeline.stop();
        currentFrame = 0;
        updateSprite();
    }

    private void move(double dx, double dy, String direction) {
        double newX = sprite.getX() + dx;
        double newY = sprite.getY() + dy;

        if (tileMap.isWalkable(newX, newY)) {
            sprite.setX(newX);
            sprite.setY(newY);
            if (!currentAnimation.equals(direction)) {
                currentAnimation = direction;
                currentFrame = 0;
            }
            updateSprite();
        }
    }

    public void handleMouseClick() {
        String attackDirection = "attack_" + currentAnimation;

        if (animations.containsKey(attackDirection)) {
            animationTimeline.stop(); // Stop walking animation
            currentAnimation = attackDirection;
            currentFrame = 0;
            updateSprite();
            SoundManager.playAttack();

            Timeline attackTimeline = new Timeline(new KeyFrame(Duration.millis(FRAME_DURATION), e -> {
                currentFrame = (currentFrame + 1) % ATTACK_FRAME_COUNT;
                updateSprite();
            }));

            attackTimeline.setCycleCount(ATTACK_FRAME_COUNT);
            attackTimeline.setOnFinished(e -> {
                // After attack, go back to idle pose of last direction
                currentAnimation = currentAnimation.replace("attack_", "");
                currentFrame = 0;
                updateSprite();
            });
            attackTimeline.play();
        }
    }

    public Image[] getSpriteFrames(String direction) {
        return animations.getOrDefault(direction, new Image[0]);
    }



    public boolean isAttacking() {
        return currentAnimation.startsWith("attack");
    }

    public ImageView getSprite() {
        return sprite;
    }

    public void takeDamage(int damage) {
        if (!isAlive) return;

        health -= damage;
        System.out.println("Player Health: " + health); // <-- Debugging line
        if (health <= 0) {
            die();
        }
    }

    private void die() {
        isAlive = false;
        sprite.setVisible(false); // Hide player sprite
        animationTimeline.stop(); // Stop any animations
        System.out.println("Player has died!");
    }

    public int getHealth() {
        return health;
    }

    public boolean isAlive() {
        return isAlive;
    }

}
