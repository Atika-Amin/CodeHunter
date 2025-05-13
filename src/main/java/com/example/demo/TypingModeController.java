package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Random;

public class TypingModeController {

    @FXML
    private TextFlow codeFlow;

    @FXML
    private TextArea typingArea;

    @FXML
    private Label timerLabel;

    @FXML
    private Button tryAgainButton;

    @FXML
    private ImageView musicIcon, homeIcon;

    private Timeline timer;
    private int timeLeft = 180;
    private String currentCode = "";


    private final String[] codeSamples = {
            "int factorial(int n) {\n" +
                    "    if (n == 0) return 1;\n" +
                    "    else return n * factorial(n - 1);\n" +
                    "}\n\n" +
                    "int main() {\n" +
                    "    int num = 5;\n" +
                    "    printf(\"Factorial of %d is %d\\n\", num, factorial(num));\n" +
                    "    return 0;\n" +
                    "}",

            "#include <stdio.h>\n\n" +
                    "int main() {\n" +
                    "    int arr[5] = {1, 2, 3, 4, 5};\n" +
                    "    int sum = 0;\n" +
                    "    for (int i = 0; i < 5; i++) {\n" +
                    "        sum += arr[i];\n" +
                    "    }\n" +
                    "    printf(\"Sum = %d\\n\", sum);\n" +
                    "    return 0;\n" +
                    "}",

            "#include <stdio.h>\n\n" +
                    "void reverseString(char str[]) {\n" +
                    "    int len = 0;\n" +
                    "    while (str[len] != '\\0') len++;\n" +
                    "    for (int i = 0; i < len / 2; i++) {\n" +
                    "        char temp = str[i];\n" +
                    "        str[i] = str[len - 1 - i];\n" +
                    "        str[len - 1 - i] = temp;\n" +
                    "    }\n" +
                    "}\n\n" +
                    "int main() {\n" +
                    "    char str[100];\n" +
                    "    scanf(\"%s\", str);\n" +
                    "    reverseString(str);\n" +
                    "    printf(\"Reversed: %s\\n\", str);\n" +
                    "    return 0;\n" +
                    "}",

            "#include <stdio.h>\n\n" +
                    "int main() {\n" +
                    "    int marks;\n" +
                    "    printf(\"Enter marks: \");\n" +
                    "    scanf(\"%d\", &marks);\n" +
                    "    \n" +
                    "    if (marks >= 90) {\n" +
                    "        printf(\"Grade: A\\n\");\n" +
                    "    } else if (marks >= 80) {\n" +
                    "        printf(\"Grade: B\\n\");\n" +
                    "    } else if (marks >= 70) {\n" +
                    "        printf(\"Grade: C\\n\");\n" +
                    "    } else {\n" +
                    "        printf(\"Grade: F\\n\");\n" +
                    "    }\n" +
                    "    return 0;\n" +
                    "}"
    };

    @FXML
    public void initialize() {
        loadRandomCode();
        startTimer();
        updateMusicIcon();

        typingArea.addEventFilter(KeyEvent.KEY_TYPED, e -> updateTypingFeedback());

        typingArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().toString().equals("ENTER")) {
                int caretPos = typingArea.getCaretPosition();
                String text = typingArea.getText();

                // Find the start of the current line
                int lineStart = text.lastIndexOf('\n', caretPos - 1) + 1;
                int lineEnd = caretPos;

                // Extract indentation from the previous line
                StringBuilder indent = new StringBuilder();
                while (lineStart < lineEnd &&
                        (text.charAt(lineStart) == ' ' || text.charAt(lineStart) == '\t')) {
                    indent.append(text.charAt(lineStart));
                    lineStart++;
                }

                // Insert new line with same indentation
                typingArea.insertText(caretPos, "\n" + indent);
                typingArea.positionCaret(caretPos + 1 + indent.length());

                // Consume the event
                event.consume();
            }
        });
    }


    private void loadRandomCode() {
        currentCode = codeSamples[new Random().nextInt(codeSamples.length)];
        codeFlow.getChildren().clear();

        for (char c : currentCode.toCharArray()) {
            Text t = new Text(String.valueOf(c));
            t.setStyle("-fx-fill: black; -fx-font-size: 16px; -fx-font-family: 'Consolas';");
            codeFlow.getChildren().add(t);
        }

        typingArea.clear();
        typingArea.setDisable(false);
        timeLeft = 180;
        timerLabel.setText("Time: " + timeLeft + "s");
    }

    private void startTimer() {
        if (timer != null) timer.stop();
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft + "s");

            if (timeLeft <= 0) {
                timer.stop();
                typingArea.setDisable(true);

                // Ensure UI updates happen on JavaFX thread
                javafx.application.Platform.runLater(this::showFinalResult);
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }


    private void updateTypingFeedback() {
        String typed = typingArea.getText();
        for (int i = 0; i < codeFlow.getChildren().size(); i++) {
            Text t = (Text) codeFlow.getChildren().get(i);
            if (i < typed.length()) {
                char expected = currentCode.charAt(i);
                char actual = typed.charAt(i);
                if (expected == actual) {
                    t.setStyle("-fx-fill: green; -fx-font-size: 16px; -fx-font-family: 'Consolas';");
                } else {
                    t.setStyle("-fx-fill: red; -fx-font-size: 16px; -fx-font-family: 'Consolas';");
                }
            } else {
                t.setStyle("-fx-fill: black; -fx-font-size: 16px; -fx-font-family: 'Consolas';");
            }
        }

        // Typing Complete
        if (typed.equals(currentCode)) {
            timer.stop();
            typingArea.setDisable(true);
            showAlert("✅ Perfect!", "You typed the code perfectly in time!");
            goBackToHome();
        }
    }

    private void showFinalResult() {
        String typed = typingArea.getText().replaceAll("\\s+", "");
        String original = currentCode.replaceAll("\\s+", "");

        if (typed.equals(original)) {
            showAlert("✅ Well Done!", "You completed the code with perfect accuracy!");
        } else {
            showAlert("⏰ Time's Up!", "You ran out of time. Try again.");
        }
    }

    @FXML
    private void handleTryAgain() {
        loadRandomCode();
        startTimer();
    }

    @FXML
    private void toggleMusic(MouseEvent event) {
        SoundUtil.playClick();
        MusicManager.toggleMusic();
        updateMusicIcon();
    }

    private void updateMusicIcon() {
        String iconPath = MusicManager.isMusicPlaying()
                ? "/com/example/demo/Frames/music_on.png"
                : "/com/example/demo/Frames/music_off.png";
        musicIcon.setImage(new Image(getClass().getResourceAsStream(iconPath)));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Typing Result");
        alert.setHeaderText(title);
        alert.setContentText(message);
        // Load the wooden theme CSS
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/com/example/demo/wooden-theme.css").toExternalForm());
        dialogPane.getStyleClass().add("wooden-alert");

        alert.showAndWait();
    }
    @FXML
    private void goBackToHome() {
        SoundUtil.playClick();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
            Stage stage = (Stage) homeIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
