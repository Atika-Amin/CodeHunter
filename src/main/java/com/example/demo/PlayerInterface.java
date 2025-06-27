package com.example.demo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public interface PlayerInterface {
    void update();
    void update(double deltaTime);

    void render(GraphicsContext gc);

    boolean isAlive();

    void handleKeyPress(KeyCode code);

    void handleKeyRelease(KeyCode code);

    void handleMouseClick();

    ImageView getSprite();

    boolean isAttacking();

    double getX();

    double getY();

    int getHealth();

    void takeDamage(int i);

    String getPlayerName();
    // other common player methods
}
