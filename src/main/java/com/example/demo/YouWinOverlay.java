package com.example.demo;

import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;



public class YouWinOverlay {
    public YouWinOverlay(Pane root, double width, double height, Canvas canvas) {
        SoundManager.stopMusic();
        applyBlur(canvas);

        // Create overlay background
        VBox overlayBox = new VBox(10);
        overlayBox.setAlignment(Pos.CENTER);
        overlayBox.setPrefWidth(width);
        overlayBox.setPrefHeight(height);
        overlayBox.setLayoutX(0);
        overlayBox.setLayoutY(0);
        overlayBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);");

        // Add the WIN image
        ImageView winImage = new ImageView(new Image(
                getClass().getResource("/assets/other/win.png").toExternalForm()));
        winImage.setFitWidth(300);
        winImage.setFitHeight(300);

        // 🎉 Apply pop animation
        winImage.setScaleX(0);
        winImage.setScaleY(0);

        ScaleTransition pop = new ScaleTransition(Duration.millis(500), winImage);
        pop.setFromX(0);
        pop.setFromY(0);
        pop.setToX(1);
        pop.setToY(1);
        pop.setCycleCount(1);
        pop.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        pop.play();

        overlayBox.getChildren().add(winImage);
        root.getChildren().add(overlayBox);

        // Bring home button to front
        for (Node node : root.getChildren()) {
            if (node instanceof Button) {
                node.toFront();
            }
        }
    }

    void applyBlur(Canvas canvas) {
        canvas.setEffect(new BoxBlur(5, 5, 3));
    }
}
