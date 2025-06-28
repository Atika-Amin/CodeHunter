package com.example.demo;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiplayerGame implements GameInterface {

    private static final int TILE_SIZE = 32;
    private static final int MAP_WIDTH = 40;
    private static final int MAP_HEIGHT = 30;
    private static final int VIEWPORT_WIDTH = 15;
    private static final int VIEWPORT_HEIGHT = 10;
    private double cameraX = 0;
    private double cameraY = 0;

    private Canvas canvas;
    private PlayerInterface player;
    private TileMap tileMap;
    private Affine cameraTransform;
    private EnemyManager enemyManager;

    private ProfessorManager professorManager;
    private slime1Manager slime1Manager;

    private TreasureManager4 treasureManager;
    private ExplosionManager explosionManager;

    private Label timerLabel;
    private int secondsElapsed = 0;
    private MessengerBox messengerBox;

    private HealthBarManager healthBarManager;
    private WinManager winManager;
    private boolean hasWon = false;
    private YouWinOverlay winOverlay = null;
    private MultiplayerClient client;
    private Map<String, OtherPlayer> otherPlayers = new HashMap<>();

    private Stage stage;
    private boolean isPaused = false;
    private Pane root;
    private AnimationTimer gameLoop;

    private DialogueBox dialogueBox;
    private DialogueManager dialogueManager;
    private final String mapName = "map5";
    private boolean farewellTriggered = false;

    public MultiplayerGame(Stage stage, MultiplayerClient client) {
        this.stage = stage;
        this.client = client;
    }

    public void start() throws Exception {
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }
        MusicManager.pauseMusic();
        SoundManager.playMusic();
        root = new Pane();
        canvas = new Canvas(VIEWPORT_WIDTH * TILE_SIZE * 2, VIEWPORT_HEIGHT * TILE_SIZE * 2);
        root.getChildren().add(canvas);

        HomeButton homeButton = new HomeButton(stage);
        homeButton.getNode().setOnAction(e -> cleanupAndGoHome());
        root.getChildren().add(homeButton.getNode());

        RestartButton restartButton = new RestartButton(stage, this);
        root.getChildren().add(restartButton.getNode());
        messengerBox = new MessengerBox();
        messengerBox.hide();
        root.getChildren().add(messengerBox.getPane());

        MessengerButton messengerButton = new MessengerButton(messengerBox);
        root.getChildren().add(messengerButton.getNode());
        messengerBox.setSendCallback(message -> {
            client.sendMessage("CHAT:" + message); // Also send to server
        });



        tileMap = new TileMap("src/main/resources/assets/maps/map3.tmj",
                "src/main/resources/assets/maps/tileset.png");

        String playerName = Session.getUsername();
        int selectedAvatar = Session.getSelectedAvatar(); // or get from DB

        if (selectedAvatar == 0) {
            player = new Player(1 * TILE_SIZE, 10 * TILE_SIZE, tileMap,playerName);
        }
        else if (selectedAvatar == 1) {
            player = new Player2(1 * TILE_SIZE, 10 * TILE_SIZE, tileMap,playerName);
        }
        else if(selectedAvatar==2){
            player=new Player3(1 * TILE_SIZE, 10 * TILE_SIZE, tileMap,playerName);
        }
        cameraTransform = new Affine();

        enemyManager = new EnemyManager(tileMap, 5);
        professorManager = new ProfessorManager(tileMap);
        slime1Manager = new slime1Manager(tileMap, 3);

        treasureManager = new TreasureManager4(tileMap);
        explosionManager = new ExplosionManager(tileMap);
        winManager = new WinManager();

        dialogueBox = new DialogueBox();
        dialogueBox.setMapName(mapName);
        dialogueManager = new DialogueManager();
        root.getChildren().add(dialogueBox.getPane());

        ChallengeManager.init(this, root);
        ChallengeManager.setupPointsDisplay(root);
        healthBarManager = new HealthBarManager(player, root);


        Scene scene = new Scene(root);
        Image messengerImg = new Image(getClass().getResourceAsStream("/assets/other/music_on.png"));
        ImageView messengerIcon = new ImageView(messengerImg);
        messengerIcon.setFitWidth(40);
        messengerIcon.setFitHeight(40);

        scene.setOnKeyPressed(event -> {
            if (player.isAlive() && !isPaused) {
                player.handleKeyPress(event.getCode());
                client.sendMessage("POS:" + player.getX() + "," + player.getY());
                drawGame();
            }
        });


        scene.setOnKeyReleased(event -> {
            if (player.isAlive() && !isPaused) {
                player.handleKeyRelease(event.getCode());
                drawGame();
            }
        });

        scene.setOnMouseClicked(event -> {
            if (dialogueBox.isVisible()) {
                dialogueBox.hide();
                return;
            }

            if (player.isAlive() && !isPaused) {
                player.handleMouseClick();
                drawGame();
            }
        });
        timerLabel = new Label("Time: 0s");
        timerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        timerLabel.setTextFill(Color.WHITE);
        timerLabel.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #deb887, #a0522d);" +
                        "-fx-border-color: #654321;" +
                        "-fx-border-width: 6;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;" +
                        "-fx-padding: 8 8 8 8;" // 🔽 reduced horizontal padding from 20 to 8
        );
        double canvasWidth = VIEWPORT_WIDTH * TILE_SIZE * 2;
        timerLabel.setLayoutX(canvasWidth - 150); // 150 is label width + margin (adjust as needed)
        timerLabel.setLayoutY(10);   // Y for top padding

        root.getChildren().add(timerLabel);

        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsElapsed++;
            timerLabel.setText("Time: " + secondsElapsed + "s");
        }));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();

        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }

                double deltaTime = (now - lastUpdate) / 1_000_000_000.0; // convert to seconds
                lastUpdate = now;

                if (player.isAlive() && !isPaused) {
                    player.update(deltaTime); // ✅ Frame-independent player movement
                    enemyManager.update(player);
                    professorManager.update(player);
                    slime1Manager.update(player);
                    treasureManager.update(player);
                    explosionManager.update(player);
                    healthBarManager.update();
                    winManager.checkAndShowWin(treasureManager, root, canvas, MultiplayerGame.this, 5, mapName);

                    drawGame();
                }
            }
        };

        gameLoop.start();

        System.out.println("Setting scene to stage...");
        stage.setScene(scene);
        stage.setTitle("Multiplayer Game");
        stage.show();
        System.out.println("Scene set and stage shown.");

        client.setMessageListener(this::onMessageReceive);

    }

    private void onMessageReceive(String message) {
        if (message.startsWith("POS_UPDATE:")) {
            // Expected format: POS_UPDATE:playerName:x,y
            String[] parts = message.split(":");
            if (parts.length == 3) {
                String playerId = parts[1];
                String[] coords = parts[2].split(",");
                if (coords.length == 2) {
                    try {
                        double x = Double.parseDouble(coords[0]);
                        double y = Double.parseDouble(coords[1]);

                        if (!playerId.equals(player.getPlayerName())) {
                            otherPlayers.putIfAbsent(playerId, new OtherPlayer(playerId));
                            otherPlayers.get(playerId).updatePosition(x, y);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️ Invalid position format: " + message);
                    }
                }
            }
        }else if (message.startsWith("CHAT:")) {
            String chatMessage = message.substring(5); // Remove "CHAT:"
            Platform.runLater(() -> {
                messengerBox.addMessage(chatMessage);
            });
        } else if (message.startsWith("JOINED:")) {
            String name = message.substring(7);
            Platform.runLater(() -> messengerBox.addMessage("🟢 " + name + " joined the game."));
        } else if (message.startsWith("LEFT:")) {
            String name = message.substring(5);
            Platform.runLater(() -> messengerBox.addMessage("🔴 " + name + " left the game."));
        }

        // ... handle other messages like chat, join, leave, etc.
    }


    private void drawGameInternal() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setTransform(cameraTransform);
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (!player.isAlive()) {
            SoundManager.stopMusic();
            GameOverOverlay gameOverOverlay = new GameOverOverlay(root, canvas.getWidth(), canvas.getHeight());
            gameOverOverlay.applyBlur(canvas);
            for (Node node : new ArrayList<>(root.getChildren())) {
                if (node instanceof Button) {
                    node.toFront();
                }
            }
            return;
        }

        if (hasWon) {
            if (winOverlay == null) {
                winOverlay = new YouWinOverlay(root, canvas.getWidth(), canvas.getHeight(), canvas);
            }
            for (Node node : new ArrayList<>(root.getChildren())) {
                if (node instanceof Button) {
                    node.toFront();
                }
            }
            return;
        }

        double playerCenterX = player.getSprite().getX() + TILE_SIZE / 2;
        double playerCenterY = player.getSprite().getY() + TILE_SIZE / 2;

        double targetX = Math.max(0, Math.min(playerCenterX - (VIEWPORT_WIDTH * TILE_SIZE) / 2,
                MAP_WIDTH * TILE_SIZE - VIEWPORT_WIDTH * TILE_SIZE));
        double targetY = Math.max(0, Math.min(playerCenterY - (VIEWPORT_HEIGHT * TILE_SIZE) / 2,
                MAP_HEIGHT * TILE_SIZE - VIEWPORT_HEIGHT * TILE_SIZE));

// Smooth camera easing
        double lerpFactor = 0.07; // Try 0.07 or 0.05 instead of 0.1
        // The smaller, the smoother (0.1 = 10% closer per frame)
        cameraX += (targetX - cameraX) * lerpFactor;
        cameraY += (targetY - cameraY) * lerpFactor;


        cameraTransform.setToIdentity();
        cameraTransform.appendScale(2, 2);
        cameraTransform.appendTranslation(-cameraX, -cameraY);

        tileMap.drawMap(gc);
        enemyManager.render(gc);
        professorManager.render(gc);
        slime1Manager.render(gc);
        treasureManager.render(gc);
        explosionManager.render(gc);

        double drawWidth = player.isAttacking() ? 52 : TILE_SIZE;
        double drawHeight = player.isAttacking() ? 52 : TILE_SIZE;
        double offsetSpriteX = player.isAttacking() ? -(drawWidth - TILE_SIZE) / 2 : 0;
        double offsetSpriteY = player.isAttacking() ? -(drawHeight - TILE_SIZE) / 2 : 0;

        gc.drawImage(
                player.getSprite().getImage(),
                player.getSprite().getX() + offsetSpriteX,
                player.getSprite().getY() + offsetSpriteY,
                drawWidth,
                drawHeight
        );

        // Draw local player's name above head
        // Draw subtle shadow behind text for depth
        gc.setFont(Font.font("Verdana", 10));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.rgb(60, 30, 10, 0.7));  // Dark brown shadow with some transparency
        gc.fillText(player.getPlayerName(), player.getSprite().getX() + drawWidth / 2 + 1, player.getSprite().getY() - 10 + 2);

// Draw text with a simple gradient fill for wooden feel
        Stop[] stops = new Stop[] { new Stop(0, Color.web("#D2B48C")), new Stop(1, Color.web("#8B5A2B")) }; // light tan to rich brown
        LinearGradient lg = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        gc.setFill(lg);
        gc.fillText(player.getPlayerName(), player.getSprite().getX() + drawWidth / 2, player.getSprite().getY() - 10);

// Optional: draw a thin dark outline to enhance readability
        gc.setStroke(Color.web("#4B3621"));
        gc.setLineWidth(0.8);
        gc.strokeText(player.getPlayerName(), player.getSprite().getX() + drawWidth / 2, player.getSprite().getY() - 10);




        for (OtherPlayer op : otherPlayers.values()) {
            op.render(gc);
        }
        // Show professor dialogue when near
        boolean playerNearProfessor = false;
        // Add this flag at the top of your class:

        for (Professor professor : professorManager.getProfessors()) {
            if (professor.isNearPlayer(player)) {
                playerNearProfessor = true;

                // Show intro dialogue if not already visible
                if (!dialogueBox.isVisible() && !farewellTriggered) {
                    List<String> introDialogue = dialogueManager.getIntroDialogue(mapName);
                    dialogueBox.show(introDialogue);
                    System.out.println("Player is near professor. Attempting to show dialogue.");

                }

                // Only enable input if farewell hasn't been triggered
                if (!farewellTriggered) {
                    dialogueBox.getInputField().setDisable(false);
                    dialogueBox.getInputField().setVisible(true);
                }

                String inputText = dialogueBox.getInputField().getText().trim().toLowerCase();
                if ((inputText.equals("thank you") || inputText.equals("exit")) && !farewellTriggered) {
                    farewellTriggered = true; // Prevent re-trigger

                    List<String> farewellDialogue = DialogueManager.getResponseBasedOnInput(inputText,mapName);
                    dialogueBox.show(farewellDialogue);

                    PauseTransition disableInput = new PauseTransition(Duration.seconds(2));
                    disableInput.setOnFinished(e -> {
                        dialogueBox.getInputField().setDisable(true);
                        dialogueBox.getInputField().setVisible(false);
                        dialogueBox.getInputField().clear();
                    });
                    disableInput.play();

                    PauseTransition hideDialogue = new PauseTransition(Duration.seconds(6));
                    hideDialogue.setOnFinished(e -> {
                        dialogueBox.hide();
                        dialogueBox.getInputField().setVisible(false);
                    });
                    hideDialogue.play();
                }

                break; // Only one professor interaction at a time
            }
        }
        // Re-enable interaction when player moves away and comes back
        if (!playerNearProfessor) {
            farewellTriggered = false;
        }
// Hide the dialogue box if player moved away from all professors
        if (!playerNearProfessor && dialogueBox.isVisible()) {
            dialogueBox.hide();
            dialogueBox.getInputField().setVisible(false);
        }
        DialogueBox.render(gc, player.getX(), player.getY(), canvas.getWidth(), canvas.getHeight());
    }

    private void cleanupAndGoHome() {
        isPaused = true;
        SoundManager.stopMusic();
        MusicManager.startMusic();
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }
        root.getChildren().clear();
        enemyManager = null;
        professorManager = null;
        slime1Manager = null;
        treasureManager = null;
        explosionManager = null;
        healthBarManager = null;
        player = null;
        tileMap = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/singlePlayer_mode.fxml"));
            Parent gamingRoot = loader.load();
            Scene gamingScene = new Scene(gamingRoot);
            stage.setScene(gamingScene);
            stage.setTitle("Gaming Mode");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawGame() {
        drawGameInternal();
    }

    public void pauseGame() {
        isPaused = true;
    }

    public void resumeGame() {
        isPaused = false;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public Pane getRoot() {
        return root;
    }

    public void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }

    public boolean hasWon() {
        return hasWon;
    }
}
