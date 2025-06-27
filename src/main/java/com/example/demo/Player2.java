package com.example.demo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class Player2 implements PlayerInterface{
    private static final int WALK_SPRITE_SIZE = 32;   // Walking sprite size
    private static final int ATTACK_SPRITE_SIZE = 52; // Attack sprite size
    private static final int FRAME_COUNT = 3;         // 3 frames per movement
    private static final int ATTACK_FRAME_COUNT = 2;  // 2 frames per attack
    private static final double SPEED = 4.5;
    private static final int FRAME_DURATION = 200;
    private boolean movingUp = false, movingDown = false, movingLeft = false, movingRight = false;
    private String direction = "down"; // Default direction
    private boolean isAttacking = false;
    private double attackTimer = 0;
    private static final double ATTACK_DURATION = ATTACK_FRAME_COUNT * (FRAME_DURATION / 1000.0); // seconds


    private ImageView sprite;
    private Map<String, Image[]> animations;
    private String currentAnimation = "down"; // Default animation (down walking)
    private int currentFrame = 0;
    private Timeline animationTimeline;
    private TileMap tileMap;
    private int health = 4000;      // Player starts with 100 health
    private boolean isAlive = true; // Player alive status
    private String playerName;


    public Player2(double x, double y, TileMap tileMap) {
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
    public Player2(double x, double y, TileMap tileMap, String playerName) {
        this.tileMap = tileMap;
        sprite = new ImageView();
        sprite.setX(x);
        sprite.setY(y);
        this.playerName = playerName;
        sprite.setFitWidth(WALK_SPRITE_SIZE);  // Initial size for walking
        sprite.setFitHeight(WALK_SPRITE_SIZE); // Initial size for walking

        loadAnimations();
        updateSprite();

        animationTimeline = new Timeline(new KeyFrame(Duration.millis(FRAME_DURATION), e -> nextFrame()));
        animationTimeline.setCycleCount(Animation.INDEFINITE);
    }

    public String getPlayerName() {
        return playerName;
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
        animations.put("down", splitMovementSpritesheet("/assets/player2_sprites/down.png"));
        animations.put("up", splitMovementSpritesheet("/assets/player2_sprites/top.png"));
        animations.put("left", splitMovementSpritesheet("/assets/player2_sprites/left.png"));
        animations.put("right", splitMovementSpritesheet("/assets/player2_sprites/right.png"));

        // Attack animations (64x64)
        animations.put("attack_down", splitAttackSpritesheet("/assets/player2_sprites/a-down.png"));
        animations.put("attack_up", splitAttackSpritesheet("/assets/player2_sprites/a-top.png"));
        animations.put("attack_left", splitAttackSpritesheet("/assets/player2_sprites/a-left.png"));
        animations.put("attack_right", splitAttackSpritesheet("/assets/player2_sprites/a-right.png"));
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

    @Override
    public void handleKeyPress(KeyCode key) {
        switch (key) {
            case W:
                movingUp = true;
                direction = "up";
                break;
            case S:
                movingDown = true;
                direction = "down";
                break;
            case A:
                movingLeft = true;
                direction = "left";
                break;
            case D:
                movingRight = true;
                direction = "right";
                break;
        }
        animationTimeline.play();
    }

    @Override
    public void handleKeyRelease(KeyCode key) {
        switch (key) {
            case W:
                movingUp = false;
                break;
            case S:
                movingDown = false;
                break;
            case A:
                movingLeft = false;
                break;
            case D:
                movingRight = false;
                break;
        }

        if (!movingUp && !movingDown && !movingLeft && !movingRight) {
            animationTimeline.stop();
            currentFrame = 0;
            updateSprite();
        }
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
        if (isAttacking) return; // Already attacking

        String attackDirection = "attack_" + direction;
        if (animations.containsKey(attackDirection)) {
            isAttacking = true;
            attackTimer = 0;
            currentAnimation = attackDirection;
            currentFrame = 0;
            updateSprite();
            SoundManager.playAttack();
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

    @Override
    public void update() {

    }
    @Override
    public void update(double deltaTime) {
        if (!isAlive) return;

        // Handle attack animation
        if (isAttacking) {
            attackTimer += deltaTime;

            // Update frame every FRAME_DURATION
            int frameIndex = (int)((attackTimer * 1000) / FRAME_DURATION) % ATTACK_FRAME_COUNT;
            if (frameIndex != currentFrame) {
                currentFrame = frameIndex;
                updateSprite();
            }

            if (attackTimer >= ATTACK_DURATION) {
                // End attack
                isAttacking = false;
                currentAnimation = direction;
                currentFrame = 0;
                updateSprite();
            }
            return; // Don't move while attacking
        }

        // Movement logic
        double dx = 0, dy = 0;

        if (movingUp) dy -= SPEED * deltaTime;
        if (movingDown) dy += SPEED * deltaTime;
        if (movingLeft) dx -= SPEED * deltaTime;
        if (movingRight) dx += SPEED * deltaTime;

        if (dx != 0 || dy != 0) {
            move(dx, dy, direction);
        }
    }

    @Override
    public void render(GraphicsContext gc) {

    }
}