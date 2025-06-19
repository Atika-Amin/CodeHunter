package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class DialogueBox {
    private static boolean visible = false;
    private static List<String> lines = new ArrayList<>();
    private static int currentLineIndex = 0;
    private final Pane pane;
    private static TextField inputField;
    private String playerName = "Player";

    private String fullLine = "";
    private static String displayedText = "";
    private Timeline textAnimation;

    private boolean waitingForInput = false;
    private boolean isAnimating = false;
    private String mapName;

    public DialogueBox() {
        pane = new Pane();
        pane.setPickOnBounds(false);
        inputField = new TextField();
        inputField.setStyle("-fx-background-color: white; -fx-border-color: brown;");
        inputField.setVisible(false); // Always visible now
        inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                SoundUtil.playClick();
                String inputText = inputField.getText().trim().toLowerCase();

                    List<String> responseLines = DialogueManager.getResponseBasedOnInput(inputText,mapName);
                    show(responseLines);
                    inputField.clear();
                    inputField.setVisible(false);

            }
        });

        pane.getChildren().add(inputField);
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public void show(List<String> dialogueLines) {
        this.lines = dialogueLines;
        this.currentLineIndex = 0;
        this.visible = true;
        this.pane.setVisible(true);
        showNextAnimatedLine();
    }

    public void nextLine() {
        SoundUtil.playClick();
        if (!visible) return;

        if (waitingForInput) {
            String response = inputField.getText().toLowerCase();
            lines = DialogueManager.getResponseBasedOnInput(response,mapName);
            currentLineIndex = 0;
            waitingForInput = false;
            showNextAnimatedLine();
        } else if (!isAnimating) {
            currentLineIndex++;
            if (currentLineIndex >= lines.size()) {
                hide();
            } else {
                showNextAnimatedLine();
            }
        } else {
            skipAnimation();
        }
    }

    private void showNextAnimatedLine() {
        if (currentLineIndex >= lines.size()) {
            hide();
            return;
        }

        String rawLine = lines.get(currentLineIndex).trim().toUpperCase();

        // Check if input is required
        if (rawLine.equals("INPUT") || rawLine.equals("[INPUT]")) {
            waitingForInput = true;
            displayedText = "";
            fullLine = "";
            isAnimating = false;

            if (textAnimation != null) {
                textAnimation.stop();
            }

            inputField.setText(""); // clear previous
            inputField.requestFocus();
            inputField.toFront();
            return;
        }

        // Otherwise, show normal dialogue with animation
        fullLine = lines.get(currentLineIndex).replace("{username}", playerName);
        displayedText = "";
        isAnimating = true;
        waitingForInput = false;

        if (textAnimation != null) {
            textAnimation.stop();
        }

        // Text wrapping logic
        String[] words = fullLine.split(" ");
        List<String> wrappedLines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        Font font = Font.font("Verdana", 10);
        double maxWidth = 190;

        for (String word : words) {
            Text text = new Text(currentLine + word + " ");
            text.setFont(font);
            if (text.getLayoutBounds().getWidth() > maxWidth) {
                wrappedLines.add(currentLine.toString());
                currentLine = new StringBuilder(word + " ");
            } else {
                currentLine.append(word).append(" ");
            }
        }
        wrappedLines.add(currentLine.toString());

        List<String> allChars = new ArrayList<>();
        for (String line : wrappedLines) {
            for (char c : line.toCharArray()) {
                allChars.add(String.valueOf(c));
            }
            allChars.add("\n");
        }

        textAnimation = new Timeline();
        for (int i = 0; i < allChars.size(); i++) {
            final int index = i;
            textAnimation.getKeyFrames().add(
                    new KeyFrame(Duration.millis(20 * (i + 1)), e -> {
                        displayedText += allChars.get(index);
                    })
            );
        }

        textAnimation.setOnFinished(e -> {
            isAnimating = false;
            if (currentLineIndex + 1 < lines.size()) {
                String nextRaw = lines.get(currentLineIndex + 1).trim().toUpperCase();
                if (!nextRaw.equals("INPUT") && !nextRaw.equals("[INPUT]")) {
                    new Timeline(new KeyFrame(Duration.seconds(1.5), ev -> nextLine())).play();
                }
            }
        });

        textAnimation.play();

        if (waitingForInput) {
            inputField.setVisible(true);
            inputField.setText("");
            inputField.requestFocus();  // Only request focus when input is needed
            inputField.toFront();
        } else {
            inputField.setVisible(false);  // Hide when not needed
        }

    }

    private void skipAnimation() {
        if (textAnimation != null) {
            textAnimation.stop();
        }
        displayedText = fullLine;
        isAnimating = false;
    }

    public static void render(GraphicsContext gc, double playerX, double playerY, double canvasWidth, double canvasHeight) {
        if (!visible || currentLineIndex >= lines.size()) return;

        double boxWidth = 180;
        double boxHeight = 130;
        double padding = 10;
        double lineHeight = 14;

        double offsetX = -boxWidth / 2 + 20; // Slightly left of player
        double offsetY = -boxHeight - 10;                 // Lower than the player

        double x = playerX + offsetX;
        double y = playerY + offsetY;

        // ✅ Clamp the box so it doesn’t go off-canvas
        if (x < 10) x = 10;
        if (x + boxWidth > canvasWidth - 10) x = canvasWidth - boxWidth - 10;
        if (y + boxHeight > canvasHeight - 10) y = canvasHeight - boxHeight - 10;

        // Draw dialogue box background
        gc.setFill(Color.BURLYWOOD);
        gc.fillRoundRect(x, y, boxWidth, boxHeight, 20, 20);

        // Draw border
        gc.setStroke(Color.SADDLEBROWN);
        gc.setLineWidth(2);
        gc.strokeRoundRect(x, y, boxWidth, boxHeight, 20, 20);

        // Set text font and alignment
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Verdana", 8));
        gc.setTextAlign(TextAlignment.LEFT);

        List<String> wrappedLines = wrapText(displayedText, boxWidth - 2 * padding, gc.getFont());
        int maxLines = (int) ((boxHeight - 2 * padding - 30) / lineHeight);

        for (int i = 0; i < Math.min(wrappedLines.size(), maxLines); i++) {
            gc.fillText(wrappedLines.get(i), x + padding, y + padding + (i + 1) * lineHeight);
        }

        // Input field setup
        double inputFieldHeight = 24;
        double inputFieldWidth = boxWidth - padding + 120;
        double inputFieldCanvasX = x + padding;
        double inputFieldCanvasY = y + boxHeight - inputFieldHeight - padding + 10;

        Affine transform = gc.getTransform();
        Point2D screenCoords = transform.transform(inputFieldCanvasX, inputFieldCanvasY);

        inputField.setLayoutX(screenCoords.getX());
        inputField.setLayoutY(screenCoords.getY());
        inputField.setPrefWidth(inputFieldWidth);
        inputField.setPrefHeight(inputFieldHeight);
        inputField.setVisible(true);
        inputField.toFront();

        inputField.setOnKeyTyped(event -> {
            String character = event.getCharacter();
            if (!character.trim().isEmpty()) {
                SoundUtil.playTyping();
            }
        });
    }





    private static List<String> wrapText(String text, double maxWidth, Font font) {
        Text helper = new Text();
        helper.setFont(font);

        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            String testLine = line.length() == 0 ? word : line + " " + word;
            helper.setText(testLine);
            if (helper.getLayoutBounds().getWidth() > maxWidth) {
                lines.add(line.toString());
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(testLine);
            }
        }
        if (!line.isEmpty()) {
            lines.add(line.toString());
        }

        return lines;
    }


    public void hide() {
        visible = false;
        inputField.setVisible(true); // still always visible
        if (textAnimation != null) textAnimation.stop();
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isAwaitingInput() {
        return waitingForInput;
    }

    public Pane getPane() {
        return pane;
    }

    public TextField getInputField() {
        return inputField;
    }

}
