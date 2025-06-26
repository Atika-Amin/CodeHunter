package com.example.demo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MultiplayerLobbyController {

    @FXML
    private TextField passwordField, nameField;

    @FXML
    private TextArea statusArea;

    private MultiplayerClient client;

    private static boolean serverStarted = false;
    private int messageCount = 0;
    private String localPlayerName;
    private boolean gameStarted = false;

    @FXML
    private TextField ipField; // add this

    @FXML
    private void handleJoin() {
        String ip = ipField.getText().trim(); // get IP
        String password = passwordField.getText().trim();
        String playerName = nameField.getText().trim();

        if (ip.isEmpty() || password.isEmpty() || playerName.isEmpty()) {
            statusArea.appendText("❌ Fill all fields (IP, Password, Name).\n");
            return;
        }

        localPlayerName = playerName;

        try {
            client = new MultiplayerClient(ip, 5050, password, playerName, this::onMessageReceived);
        } catch (Exception e) {
            statusArea.appendText("🚫 Failed to connect.\n");
            e.printStackTrace(); // helpful for debugging
            return;
        }

        statusArea.appendText("🔄 Connecting to server at " + ip + "...\n");
    }

    private void onMessageReceived(String message) {
        Platform.runLater(() -> {
            if (message.equals("SERVER_FULL")) {
                statusArea.appendText("⚠️ Server is full. Try later.\n");
                client.close();
            } else if (message.equals("INVALID_PASSWORD")) {
                statusArea.appendText("🔒 Wrong password.\n");
                client.close();
            } else if (message.equals("PASSWORD_ACCEPTED")) {
                statusArea.appendText("✅ Password accepted. Waiting to join...\n");
            } else if (message.startsWith("JOINED:")) {
                String joinedPlayer = message.substring(7);
                statusArea.appendText("🎉 " + joinedPlayer + " joined the game.\n");
            } else if (message.startsWith("LEFT:")) {
                statusArea.appendText("👋 " + message.substring(5) + " left.\n");
            } else if (message.equals("DISCONNECTED")) {
                statusArea.appendText("❌ Connection lost.\n");
            } else if (message.startsWith("PLAYER_COUNT:")) {
                int count = Integer.parseInt(message.substring(13));
                statusArea.appendText("👥 Players connected: " + count + "\n");

                if (count >= 2 && !gameStarted && localPlayerName != null) {
                    gameStarted = true;
                    try {
                        statusArea.appendText("🚀 Starting game...\n");
                        MultiplayerGame game = new MultiplayerGame((Stage) statusArea.getScene().getWindow(), client);
                        game.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                statusArea.appendText("📨 " + message + "\n");
            }
        });
    }
}
