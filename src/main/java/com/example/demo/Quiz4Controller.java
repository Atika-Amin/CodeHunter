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

public class Quiz4Controller {

    @FXML private ToggleGroup q1Group;
    @FXML private ToggleGroup q2Group;
    @FXML private ToggleGroup q3Group;

    @FXML private TextArea q4Answer;
    @FXML private TextField q5Answer;

    @FXML private Button submitButton;
    @FXML private ImageView homeIcon;

    private final int TOTAL_SCORE = 5;
    private int score = 0;

    @FXML
    public void initialize() {
        int userLevel = Session.getKnowledgeLevel();
        if (userLevel > 3) {
            disableQuiz("You have already completed this level. Proceed to the next one.");
        }
        q4Answer.setOnKeyTyped(event -> SoundUtil.playTyping()); // Play typing sound
        q5Answer.setOnKeyTyped(event -> SoundUtil.playTyping());
    }

    private void disableQuiz(String message) {
        SoundUtil.playClick();
        submitButton.setDisable(true);
        q4Answer.setDisable(true);
        q5Answer.setDisable(true);

        q1Group.getToggles().forEach(t -> ((RadioButton) t).setDisable(true));
        q2Group.getToggles().forEach(t -> ((RadioButton) t).setDisable(true));
        q3Group.getToggles().forEach(t -> ((RadioButton) t).setDisable(true));

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Quiz Locked");
        alert.setHeaderText(null);
        alert.setContentText("❌ " + message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/com/example/demo/wooden-theme.css").toExternalForm());
        dialogPane.getStyleClass().add("wooden-alert");
        alert.showAndWait();

        javafx.application.Platform.runLater(() -> goBackToReadingMode());
    }

    @FXML
    private void submitQuiz (ActionEvent event) {
        SoundUtil.playClick();
        score = 0;

        RadioButton selected1 = (RadioButton) q1Group.getSelectedToggle();
        if (selected1 != null && selected1.getText().equals("A memory address")) score++;

        RadioButton selected2 = (RadioButton) q2Group.getSelectedToggle();
        if (selected2 != null && selected2.getText().equals("10")) score++;

        RadioButton selected3 = (RadioButton) q3Group.getSelectedToggle();
        if (selected3 != null && selected3.getText().equals("free()")) score++;

        String q4 = q4Answer.getText().trim().toLowerCase();
        if (q4.contains("int") && q4.contains("*") && q4.contains("= &")) score++;

        String q5 = q5Answer.getText().trim().toLowerCase();
        if (q5.equals("free")) score++;

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goBackToReadingMode() {
        SoundUtil.playClick();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/reading_mode.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
