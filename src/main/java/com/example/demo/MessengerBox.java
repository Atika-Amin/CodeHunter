package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class MessengerBox {
    private final Pane pane;
    private final TextField inputField;
    private final VBox messageContainer;
    private final ScrollPane scrollPane;
    private final Timeline textAnimation;
    private final List<String> messages = new LinkedList<>();
    private Consumer<String> sendCallback;

    public MessengerBox() {
        pane = new Pane();
        pane.setPrefSize(300, 200);
        pane.setLayoutX(110);
        pane.setLayoutY(400);
        pane.setStyle("""
            -fx-background-color: linear-gradient(#d2b48c, #8b5a2b);  /* tan to dark wood gradient */
            -fx-border-color: #4E342E;   /* dark brown edge */
            -fx-border-width: 4;
            -fx-background-radius: 20;
            -fx-border-radius: 20;
            -fx-effect: dropshadow(gaussian, rgba(50,30,20,0.4), 10, 0.5, 0, 2);
        """);

        messageContainer = new VBox(5);
        messageContainer.setStyle("""
    -fx-padding: 5;
    -fx-alignment: top-left;
""");
        messageContainer.setPrefWidth(260);
        messageContainer.setFillWidth(true);


        scrollPane = new ScrollPane(messageContainer);
        scrollPane.setLayoutX(10);
        scrollPane.setLayoutY(10);
        scrollPane.setPrefSize(280, 140);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("""
            -fx-background-color: transparent;
            -fx-border-color: transparent;
        """);

        inputField = new TextField();
        inputField.setLayoutX(10);
        inputField.setLayoutY(160);
        inputField.setPrefWidth(280);
        inputField.setPromptText("Type a message...");
        inputField.setStyle("""
            -fx-background-color: #f5deb3;
            -fx-border-color: #A0522D;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
            -fx-font-family: 'Verdana';
            -fx-font-size: 12;
            -fx-prompt-text-fill: #6B4E2E;
        """);

        // Add slight hover glow for interaction feel
        inputField.setOnMouseEntered(e -> inputField.setStyle(inputField.getStyle() +
                "-fx-effect: dropshadow(three-pass-box, rgba(120,80,50,0.5), 8, 0.3, 0, 0);"));
        inputField.setOnMouseExited(e -> inputField.setStyle(inputField.getStyle().replaceAll("-fx-effect:.*?;", "")));

        pane.getChildren().addAll(scrollPane, inputField);
        textAnimation = new Timeline();

        inputField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                String input = inputField.getText().trim();
                if (!input.isEmpty() && sendCallback != null) {
                    sendCallback.accept(input);
                    inputField.clear();
                }
            }
        });
    }

    public void setSendCallback(Consumer<String> callback) {
        this.sendCallback = callback;
    }

    public void addMessage(String message) {
        messages.add(message);
        if (messages.size() > 10) {
            messages.remove(0);
            if (!messageContainer.getChildren().isEmpty()) {
                messageContainer.getChildren().remove(0);
            }
        }

        animateLatestMessage(message);
    }

    private void animateLatestMessage(String message) {
        Text animatedText = new Text();
        animatedText.setFont(Font.font("Verdana", 12));
        animatedText.setWrappingWidth(250); // match your bubble max width
        // Slightly less than bubble width
        animatedText.setStyle("-fx-fill: #3E2723;");

        // Create bubble with fixed width
        StackPane bubble = new StackPane(animatedText);
        bubble.setPrefWidth(280); // Fixed width (like input field)
        bubble.setMaxWidth(260); // or match inputField.getPrefWidth()
        bubble.setMinWidth(260);


        bubble.setStyle("""
    -fx-background-color: #FFF8DC;  /* cornsilk */
    -fx-background-radius: 15;
    -fx-border-radius: 15;
    -fx-padding: 10 15 10 15;
    -fx-border-color: #8B4513;      /* saddle brown */
    -fx-border-width: 2;
""");


        messageContainer.getChildren().add(bubble);

        List<Character> chars = new ArrayList<>();
        for (char c : message.toCharArray()) chars.add(c);

        if (textAnimation.getStatus() == Timeline.Status.RUNNING) {
            textAnimation.stop();
        }

        textAnimation.getKeyFrames().clear();

        for (int i = 0; i < chars.size(); i++) {
            final int index = i;
            textAnimation.getKeyFrames().add(
                    new KeyFrame(Duration.millis(12 * (i + 1)), e -> {
                        animatedText.setText(animatedText.getText() + chars.get(index));
                        Platform.runLater(() -> scrollPane.setVvalue(1.0)); // scroll to bottom
                    })
            );
        }

        textAnimation.play();
    }


    public Pane getPane() {
        return pane;
    }

    public void show() {
        pane.setVisible(true);
        inputField.requestFocus();
    }

    public void hide() {
        pane.setVisible(false);
    }

    public boolean isVisible() {
        return pane.isVisible();
    }
}
