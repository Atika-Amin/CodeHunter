package com.example.demo;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;

import javafx.util.Duration;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignupController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    private ImageView woodenFrame;

    @FXML
    private Group signupGroup; // The group containing the wooden frame, title, and signup form elements
    @FXML private ImageView musicIcon;
    @FXML private Slider volumeSlider;
    @FXML
    private void initialize() {
        // Apply zoom-in effect on the group containing the elements
        applyZoomInAnimation(signupGroup);
        // Update icon initially based on music state
        updateMusicIcon();
        usernameField.setOnKeyTyped(event -> SoundUtil.playTyping()); // Play typing sound
        emailField.setOnKeyTyped(event -> SoundUtil.playTyping());    // Play typing sound
        passwordField.setOnKeyTyped(event -> SoundUtil.playTyping()); // Play typing sound
    }

    private void applyZoomInAnimation(Group signupGroup) {
        // Create a ScaleTransition for the group (wooden frame, title, and signup form)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1.2), signupGroup);
        scaleTransition.setFromX(0.8); // Start smaller (80% of the original size)
        scaleTransition.setFromY(0.8); // Same for Y-axis
        scaleTransition.setToX(1.0);   // End at 100% (original size)
        scaleTransition.setToY(1.0);   // End at 100% (original size)

        // Play the zoom-in transition
        scaleTransition.play();
    }

    @FXML
    private void handleSignup() {
        SoundUtil.playClick();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("❌ Error", "All fields are required!");
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12)); // Securely hash password

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                // ✅ Get generated user_id
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    // ✅ Create user_progress for this user
                    DatabaseConnection.createUserProgress(userId);
                }

                showAlert("✅ Success", "User registered successfully!");
                switchToLogin();
            }
        } catch (SQLException e) {
            showAlert("❌ Error", "User registration failed: " + e.getMessage());
        }
    }


    @FXML
    private void switchToLogin() {
        SoundUtil.playClick();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Load the wooden theme CSS
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/com/example/demo/wooden-theme.css").toExternalForm());
        dialogPane.getStyleClass().add("wooden-alert");

        alert.showAndWait();
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
}
