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


public class MessengerButton {
    private final Button messengerButton;

    public MessengerButton(MessengerBox messengerBox) {
        messengerButton = new Button();

        double buttonRadius = 10; // same as HomeButton
        messengerButton.setPrefWidth(buttonRadius * 2);
        messengerButton.setPrefHeight(buttonRadius * 2);

        Circle circleShape = new Circle(buttonRadius);
        messengerButton.setShape(circleShape);

        messengerButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-padding: 0;" +
                        "-fx-background-radius: " + buttonRadius + "px;"
        );

        // Base icon (same size and style as HomeButton background)
        Image backgroundImage = new Image(getClass().getResourceAsStream("/assets/other/home.png"));
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(50);
        backgroundView.setFitHeight(50);
        backgroundView.setPreserveRatio(true);

        // Messenger icon (smaller overlay)
        Image messengerImage = new Image(getClass().getResourceAsStream("/assets/other/Messenger.png"));
        ImageView messengerView = new ImageView(messengerImage);
        messengerView.setFitWidth(40); // smaller, sits on top
        messengerView.setFitHeight(40);
        messengerView.setPreserveRatio(true);

        // Stack the two icons (like a badge or overlay)
        StackPane layeredIcons = new StackPane(backgroundView, messengerView);
        layeredIcons.setTranslateY(4); // same as HomeButton for alignment

        messengerButton.setGraphic(layeredIcons);

        // Position (e.g., right side of HomeButton)
        messengerButton.setLayoutX(150); // adjust as needed
        messengerButton.setLayoutY(580);

        messengerButton.setOnAction(e -> {
            if (messengerBox.isVisible()) {
                messengerBox.hide();
            } else {
                messengerBox.show();
            }
        });
    }
    public Button getNode() {
        return messengerButton;
    }

}
