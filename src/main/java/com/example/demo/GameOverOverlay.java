package com.example.demo;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class GameOverOverlay {

    private VBox overlayBox;

    public GameOverOverlay(Pane root, double width, double height) {
        overlayBox = new VBox(10); // spacing 10px
        overlayBox.setAlignment(Pos.CENTER);
        overlayBox.setPrefWidth(width);
        overlayBox.setPrefHeight(height);
        overlayBox.setLayoutX(0);
        overlayBox.setLayoutY(0);
        overlayBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);");

        // Load and set up the Game Over icon
        ImageView gameOverSymbol = new ImageView(new Image(
                getClass().getResource("/assets/other/game_over.png").toExternalForm()));
        gameOverSymbol.setFitWidth(300);
        gameOverSymbol.setFitHeight(300);

        // Create a label for "Game Over" text



        // Add the icon and label to the VBox
        overlayBox.getChildren().addAll(gameOverSymbol);

        // Add overlay VBox to the root pane
        root.getChildren().add(overlayBox);
    }

    public void applyBlur(Canvas canvas) {
        canvas.setEffect(new GaussianBlur(15));
    }

    public VBox getOverlayBox() {
        return overlayBox;
    }
}
