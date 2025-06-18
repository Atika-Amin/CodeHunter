package com.example.demo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeButton {
    private Button homeButton;

    public HomeButton(Stage stage) {
        homeButton = new Button();

        double buttonRadius = 10; // smaller button (diameter 32px)
        homeButton.setPrefWidth(buttonRadius * 2);
        homeButton.setPrefHeight(buttonRadius * 2);

        Circle circleShape = new Circle(buttonRadius);
        homeButton.setShape(circleShape);

        homeButton.setStyle(
                "-fx-background-color: transparent;" + // semi-transparent blue
                        "-fx-padding: 0;" +
                        "-fx-background-radius: " + buttonRadius + "px;"
        );

        // Load icon image
        Image image = new Image(getClass().getResourceAsStream("/assets/other/home.png"));
        ImageView imageView = new ImageView(image);

        // Bigger icon than button size (40x40)
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);

        // Wrap icon so it can overflow button bounds
        StackPane imageWrapper = new StackPane(imageView);
        // Center the icon but can add offsets if you want:
        imageWrapper.setTranslateY(4); // small downward shift if needed

        homeButton.setGraphic(imageWrapper);

        // Position button bottom-left
        homeButton.setLayoutX(15);
        homeButton.setLayoutY(580);

        homeButton.setOnAction(e -> {
            SoundManager.stopMusic();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/_mode.fxml"));
                Parent gamingRoot = loader.load();

                Scene gamingScene = new Scene(gamingRoot);
                stage.setScene(gamingScene);
                stage.setTitle("Gaming Mode");
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public Button getNode() {
        return homeButton;
    }
}

