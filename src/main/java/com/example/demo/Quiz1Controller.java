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


public class Quiz1Controller {

    @FXML private ToggleGroup group1;
    @FXML private ToggleGroup group2;
    @FXML private ToggleGroup group3;

    @FXML private TextArea answer4;
    @FXML private TextArea answer5;

    @FXML private Button submitQuizButton;
    @FXML private ImageView homeIcon;

    private final int TOTAL_SCORE = 5;
    private int score = 0;
    @FXML
    public void initialize() {
        int userLevel = Session.getKnowledgeLevel(); // get the current level of the user
        if (userLevel > 0) {
            disableQuiz("You have already completed this level. Proceed to the next one.");
        }
        answer4.setOnKeyTyped(event -> SoundUtil.playTyping()); // Play typing sound
        answer5.setOnKeyTyped(event -> SoundUtil.playTyping());
    }
    private void disableQuiz(String message) {
        submitQuizButton.setDisable(true);
        answer4.setDisable(true);
        answer5.setDisable(true);

        group1.getToggles().forEach(toggle -> ((RadioButton) toggle).setDisable(true));
        group2.getToggles().forEach(toggle -> ((RadioButton) toggle).setDisable(true));
        group3.getToggles().forEach(toggle -> ((RadioButton) toggle).setDisable(true));

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
        // Use Platform.runLater to delay scene switch until after alert closes
        javafx.application.Platform.runLater(() -> goBackToReadingMode());
    }

    @FXML
    private void submitQuiz(ActionEvent event) {
        SoundUtil.playClick();
        score = 0;

        // Question 1
        RadioButton selected1 = (RadioButton) group1.getSelectedToggle();
        if (selected1 != null && selected1.getText().equals("Welcome to C Programming!")) {
            score++;
        }

        // Question 2
        RadioButton selected2 = (RadioButton) group2.getSelectedToggle();
        if (selected2 != null && selected2.getText().equals("int age = 18;")) {
            score++;
        }

        // Question 3
        RadioButton selected3 = (RadioButton) group3.getSelectedToggle();
        if (selected3 != null && selected3.getText().equals("Roll: 101, Weight: 72.5")) {
            score++;
        }

        // Question 4
        String q4 = answer4.getText().trim().toLowerCase();
        if (q4.contains("printf") && q4.contains("sum") && q4.contains("a+b")) {
            score++;
        }

        // Question 5
        String q5 = answer5.getText().trim().toLowerCase();
        if (q5.contains("scanf") && q5.contains("%d") && q5.contains("&")) {
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
