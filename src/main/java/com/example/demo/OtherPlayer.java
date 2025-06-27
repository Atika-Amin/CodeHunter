package com.example.demo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class OtherPlayer extends Player {

    private final String playerName;

    public OtherPlayer(String playerName) {
        // Start at dummy position (0, 0), you will update it later
        super(0, 0, null); // Assuming you don’t need tileMap for movement
        this.playerName = playerName;
    }

    public void updatePosition(double x, double y) {
        this.getSprite().setX(x);
        this.getSprite().setY(y);
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(getSprite().getImage(), getSprite().getX(), getSprite().getY(), 32, 32);

        gc.setFill(Color.YELLOW);
        gc.setFont(new Font("Arial", 12));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(playerName, getSprite().getX() + 16, getSprite().getY() - 10);
    }
}
