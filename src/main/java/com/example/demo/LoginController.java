package com.example.demo;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private ImageView woodenFrame;

    @FXML
    private VBox loginBox; // The VBox containing the login form elements (buttons, fields)

    @FXML
    private Group rootGroup; // A Group containing the wooden frame, title, and loginBox

    @FXML
    private Group loginGroup; // The group containing the wooden frame, title, and login form elements
    @FXML private ImageView musicIcon;
    @FXML private Slider volumeSlider;
    @FXML
    private void initialize() {
        // Apply zoom-in effect on the group containing the elements
        Platform.runLater(() -> applyZoomInAnimation(loginGroup)); // Defer animation

        MusicManager.startMusic();
        // Update icon initially based on music state
        updateMusicIcon();
        usernameField.setOnKeyTyped(event -> SoundUtil.playTyping()); // Play typing sound
        passwordField.setOnKeyTyped(event -> SoundUtil.playTyping()); // Play typing sound
    }

    private void applyZoomInAnimation(Group loginGroup) {
        // Create a ScaleTransition for the group (wooden frame, title, and login form)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1.2), loginGroup);
        scaleTransition.setFromX(0.8); // Start smaller (80% of the original size)
        scaleTransition.setFromY(0.8); // Same for Y-axis
        scaleTransition.setToX(1.0);   // End at 100% (original size)
        scaleTransition.setToY(1.0);   // End at 100% (original size)

        // Play the zoom-in transition
        scaleTransition.play();
    }
    @FXML
    private void handleLogin() {
        SoundUtil.playClick();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("❌ Error", "All fields are required!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                showAlert("❌ Error", "Database connection failed. Please check your database settings.");
                return;
            }

            String sql = "SELECT id, username, password FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
                int userId = rs.getInt("id"); // ✅ Extract user_id
                String name= rs.getString("username");
                if (isValidHashedPassword(storedHashedPassword) && BCrypt.checkpw(password, storedHashedPassword)) {
                    //showAlert("✅ Success", "Login successful!");
                    // Redirect to home page
                    // ✅ Store user ID in session
                    Session.setCurrentUserId(userId);
                    Session.setUsername(name);
                    switchToHome();
                } else {
                    showAlert("❌ Error", "Invalid username or password!");
                }
            } else {
                showAlert("❌ Error", "Invalid username or password!");
            }
        } catch (SQLException e) {
            showAlert("❌ Error", "Login failed: " + e.getMessage());
        }
    }

    /**
     * Ensures the stored password is a valid bcrypt hash.
     */
    private boolean isValidHashedPassword(String hashedPassword) {
        return hashedPassword != null && hashedPassword.startsWith("$2a$");
    }

    @FXML
    private void switchToSignup() {
        SoundUtil.playClick();
        switchScene("signup.fxml");
    }

    @FXML
    private void switchToHome() {
        switchScene("home.fxml");
    }

    private void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
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
