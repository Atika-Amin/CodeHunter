package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class HealthBarManager {
    private final ImageView healthImageView;
    private final Player player;

    public HealthBarManager(Player player, Pane root) {
        this.player = player;
        this.healthImageView = new ImageView();

        healthImageView.setFitWidth(150); // wider image
        healthImageView.setFitHeight(50); // height stays same
        healthImageView.setLayoutX(140);  // moved more to the right (adjust as needed)
        healthImageView.setLayoutY(10);   // top aligned


        root.getChildren().add(healthImageView);
        update(); // Initial image
    }

    public void update() {
        int health = player.getHealth();
        int index = Math.max(1, 16 - (health / 250)); // 4000 -> health1, 3750 -> health2, ..., 0 -> health15
        index = Math.min(index, 15); // clamp between 1-15

        String path = "/assets/other/health" + index + ".png";
        Image healthImage = new Image(getClass().getResourceAsStream(path));
        healthImageView.setImage(healthImage);
    }
}
