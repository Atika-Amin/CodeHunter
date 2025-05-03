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

public class Quiz3Controller {

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
        int userLevel = Session.getKnowledgeLevel();
        if (userLevel > 2) {
            disableQuiz("You have already completed this level. Proceed to the next one.");
        }
        answer4.setOnKeyTyped(event -> SoundUtil.playTyping()); // Play typing sound
        answer5.setOnKeyTyped(event -> SoundUtil.playTyping());
    }

    private void disableQuiz(String message) {
        SoundUtil.playClick();
        submitQuizButton.setDisable(true);
        answer4.setDisable(true);
        answer5.setDisable(true);

        group1.getToggles().forEach(t -> ((RadioButton) t).setDisable(true));
        group2.getToggles().forEach(t -> ((RadioButton) t).setDisable(true));
        group3.getToggles().forEach(t -> ((RadioButton) t).setDisable(true));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Locked");
        alert.setHeaderText(null);
        alert.setContentText("❌ " + message);
        // Load the wooden theme CSS
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/com/example/demo/wooden-theme.css").toExternalForm());
        dialogPane.getStyleClass().add("wooden-alert");
        alert.showAndWait();

        javafx.application.Platform.runLater(() -> goBackToReadingMode());
    }

    @FXML
    private void submitQuiz(ActionEvent event) {
        SoundUtil.playClick();
        score = 0;

        RadioButton selected1 = (RadioButton) group1.getSelectedToggle();
        if (selected1 != null && selected1.getText().equals("int arr[5];")) score++;

        RadioButton selected2 = (RadioButton) group2.getSelectedToggle();
        if (selected2 != null && selected2.getText().contains("arrays of characters")) score++;

        RadioButton selected3 = (RadioButton) group3.getSelectedToggle();
        if (selected3 != null && selected3.getText().equals("int add(int a, int b);")) score++;

        String q4 = answer4.getText().trim().toLowerCase();
        if (q4.contains("int") && q4.contains("return") && q4.contains("(") && q4.contains(")")) score++;

        String q5 = answer5.getText().trim().toLowerCase();
        if (q5.contains("printf") && (q5.contains("name[0]") || q5.contains("*name"))) score++;

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
