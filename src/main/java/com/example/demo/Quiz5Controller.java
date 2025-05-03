package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class Quiz5Controller {

    @FXML private ToggleGroup group1;
    @FXML private ToggleGroup group2;
    @FXML private ToggleGroup group4;

    @FXML private RadioButton q1Correct;
    @FXML private RadioButton q2Correct;
    @FXML private RadioButton q4Correct;

    @FXML private TextArea q3Answer;
    @FXML private TextArea q5Answer;

    @FXML private Button submitQuizButton;
    @FXML private ImageView homeIcon;

    private final int TOTAL_SCORE = 5;
    private int score = 0;

    @FXML
    public void initialize() {
        int userLevel = Session.getKnowledgeLevel(); // get the current level of the user
        if (userLevel > 4) { // Assuming level 5 is the final level
            disableQuiz("You have already completed this level. Proceed to the next one.");
        }
        q3Answer.setOnKeyTyped(event -> SoundUtil.playTyping()); // Play typing sound
        q5Answer.setOnKeyTyped(event -> SoundUtil.playTyping());
    }

    private void disableQuiz(String message) {
        submitQuizButton.setDisable(true);
        q3Answer.setDisable(true);
        q5Answer.setDisable(true);

        group1.getToggles().forEach(toggle -> ((RadioButton) toggle).setDisable(true));
        group2.getToggles().forEach(toggle -> ((RadioButton) toggle).setDisable(true));
        group4.getToggles().forEach(toggle -> ((RadioButton) toggle).setDisable(true));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Locked");
        alert.setHeaderText(null);
        alert.setContentText("❌ " + message);
        // Load the wooden theme CSS
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/com/example/demo/wooden-theme.css").toExternalForm());
        dialogPane.getStyleClass().add("wooden-alert");
        alert.showAndWait();

        // Optional: Automatically go back to home after showing the alert
        javafx.application.Platform.runLater(() -> goBackToReadingMode());
    }

    @FXML
    private void submitQuiz(ActionEvent event) {
        SoundUtil.playClick();
        score = 0;

        // Check MCQs
        if (q1Correct.isSelected()) score++;
        if (q2Correct.isSelected()) score++;
        if (q4Correct.isSelected()) score++;

        // Check short-answer questions
        String q3 = q3Answer.getText().trim().toLowerCase();
        if (q3.contains("struct") && q3.contains("{") && q3.contains("}")) {
            score++;
        }

        String q5 = q5Answer.getText().trim().replaceAll("\\s+", "");
        if (q5.equalsIgnoreCase("FILE*fp=fopen(\"filename.txt\",\"r\");") ||
                q5.equalsIgnoreCase("FILE*fp=fopen(\"file.txt\",\"r\");")) {
            score++;
        }

        // Show result
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Quiz Completed");
        alert.setHeaderText("You scored " + score + " out of " + TOTAL_SCORE + ".");
        // Load the wooden theme CSS
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/com/example/demo/wooden-theme.css").toExternalForm());
        dialogPane.getStyleClass().add("wooden-alert");

        if (score == TOTAL_SCORE) {
            alert.setContentText("Great! Your knowledge level will be increased.");
            updateKnowledgeLevel();

            // Check if all levels are completed (i.e., user has completed level 5)
            if (Session.getKnowledgeLevel() >= 5) {
                showFinalCongratulatoryMessage();
            }
        } else {
            alert.setContentText("Try again to pass the quiz.");
        }

        alert.showAndWait();
        goBackToReadingMode();
    }

    private void updateKnowledgeLevel() {
        int currentLevel = Session.getKnowledgeLevel();
        int updatedLevel = currentLevel + 1;
        Session.setKnowledgeLevel(updatedLevel);
        DatabaseConnection.updateKnowledgeLevel(Session.getCurrentUserId(), updatedLevel);
    }

    private void showFinalCongratulatoryMessage() {
        Alert finalAlert = new Alert(AlertType.INFORMATION);
        finalAlert.setTitle("Congratulations!");
        finalAlert.setHeaderText("You've completed all the levels!");
        finalAlert.setContentText("🎉 You've successfully finished the entire game. Well done!");
        // Load the wooden theme CSS
        DialogPane dialogPane = finalAlert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/com/example/demo/wooden-theme.css").toExternalForm());
        dialogPane.getStyleClass().add("wooden-alert");

        // Optionally, you can add a button or more options here to navigate or reset the game.
        finalAlert.showAndWait();
    }

    @FXML
    private void goBackToHome() {
        SoundUtil.playClick();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) submitQuizButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goBackToReadingMode() {
        SoundUtil.playClick();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("reading_mode.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) submitQuizButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
