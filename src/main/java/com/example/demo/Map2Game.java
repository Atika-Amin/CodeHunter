package com.example.demo;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map2Game implements GameInterface{

    private static final int TILE_SIZE = 32;
    private static final int MAP_WIDTH = 40;
    private static final int MAP_HEIGHT = 30;
    private static final int VIEWPORT_WIDTH = 15;
    private static final int VIEWPORT_HEIGHT = 10;

    private Canvas canvas;
    private Player player;
    private TileMap tileMap;
    private Affine cameraTransform;
    private EnemyManager enemyManager;

    private ProfessorManager professorManager;
    private slime1Manager slime1Manager;

    private TreasureManager1 treasureManager;
    private ExplosionManager explosionManager;

    private HealthBarManager healthBarManager;
    private WinManager winManager;
    private boolean hasWon = false;
    private YouWinOverlay winOverlay = null;



    private Stage stage;

    private boolean isPaused = false;

    private Pane root;
    private AnimationTimer gameLoop;
    // Dialogue-related
    private DialogueBox dialogueBox;
    private DialogueManager dialogueManager;
    private final String mapName = "map2"; // Add this near other class fields
    // Declare this flag at the top of your class
    private boolean farewellTriggered = false;




    public Map2Game(Stage stage) {
        this.stage = stage;
    }

    public void start() throws Exception {
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }

        SoundManager.playMusic();

        root = new Pane();

        canvas = new Canvas(VIEWPORT_WIDTH * TILE_SIZE * 2, VIEWPORT_HEIGHT * TILE_SIZE * 2);
        root.getChildren().add(canvas);
        HomeButton homeButton = new HomeButton(stage);
        homeButton.getNode().setOnAction(e -> cleanupAndGoHome());
        root.getChildren().add(homeButton.getNode());
        RestartButton restartButton = new RestartButton(stage, this);
        root.getChildren().add(restartButton.getNode());


        tileMap = new TileMap("src/main/resources/assets/maps/map2.tmj",
                "src/main/resources/assets/maps/tileset2.png");

        player = new Player(1 * TILE_SIZE, 13 * TILE_SIZE, tileMap);
        cameraTransform = new Affine();

        enemyManager = new EnemyManager(tileMap,5);

        professorManager = new ProfessorManager(tileMap);
        slime1Manager = new slime1Manager(tileMap,3);

        treasureManager = new TreasureManager1(tileMap);
        explosionManager = new ExplosionManager(tileMap);
        winManager = new WinManager();
        // Initialize dialogue
        dialogueBox = new DialogueBox();
        dialogueBox.setMapName(mapName);
        dialogueManager = new DialogueManager();
        root.getChildren().add(dialogueBox.getPane());
        dialogueBox.getPane().toFront();

        ChallengeManager.init(this, root);
        ChallengeManager.setupPointsDisplay(root);
        healthBarManager = new HealthBarManager(player, root);

        Scene scene = new Scene(root);

        // Input handlers
        scene.setOnKeyPressed(event -> {
            if (player.isAlive()&& !isPaused ) {
                player.handleKeyPress(event.getCode());
                drawGame();
            }
        });

        scene.setOnKeyReleased(event -> {
            if (player.isAlive()&& !isPaused ) {
                player.handleKeyRelease(event.getCode());
                drawGame();
            }
        });

        scene.setOnMouseClicked(event -> {
            if (dialogueBox.isVisible()) {
                dialogueBox.hide();
                return; // Don’t do anything else if closing dialogue
            }

            if (player.isAlive() && !isPaused ) {
                player.handleMouseClick();
                drawGame();
            }
        });

        // ✅ Properly managed game loop
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (player.isAlive() && !isPaused) {
                    enemyManager.update(player);
                    professorManager.update(player);
                    slime1Manager.update(player);
                    treasureManager.update(player);
                    explosionManager.update(player);
                    healthBarManager.update();
                    System.out.println("Calling checkAndShowWin...");
                    winManager.checkAndShowWin(treasureManager, root, canvas, Map2Game.this,3,mapName);



                    drawGame();
                }
            }
        };
        gameLoop.start();


        stage.setScene(scene);
        stage.setTitle("Map 2 - Tile Game");
        stage.show();
    }
    private void cleanupAndGoHome() {
        isPaused = true;

        // Stop music and game loop
        SoundManager.stopMusic();

        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }

        // Clear root to release memory/resources
        root.getChildren().clear();

        // Optionally null out heavy managers
        enemyManager = null;
        professorManager = null;
        slime1Manager = null;
        treasureManager = null;
        explosionManager = null;
        healthBarManager = null;
        player = null;
        tileMap = null;
        // 🔄 Navigate to gaming_mode.fxml
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

    private void drawGameInternal() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setTransform(cameraTransform);
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());


        if (!player.isAlive()) {
            // Game over logic
            SoundManager.stopMusic();
            GameOverOverlay gameOverOverlay = new GameOverOverlay(root, canvas.getWidth(), canvas.getHeight());
            gameOverOverlay.applyBlur(canvas);

            // Bring home button to front
            for (Node node : new ArrayList<>(root.getChildren())) {
                if (node instanceof Button) {
                    node.toFront();
                }
            }
            return; // Skip drawing rest
        }

        if (hasWon) {
            // Show win overlay only once
            if (winOverlay == null) {
                winOverlay = new YouWinOverlay(root, canvas.getWidth(), canvas.getHeight(), canvas);
            }

            // Bring home button to front
            for (Node node : new ArrayList<>(root.getChildren())) {
                if (node instanceof Button) {
                    node.toFront();
                }
            }
            return; // Skip drawing rest
        }


        double playerCenterX = player.getSprite().getX() + TILE_SIZE / 2;
        double playerCenterY = player.getSprite().getY() + TILE_SIZE / 2;

        double offsetX = Math.max(0, Math.min(playerCenterX - (VIEWPORT_WIDTH * TILE_SIZE) / 2,
                MAP_WIDTH * TILE_SIZE - VIEWPORT_WIDTH * TILE_SIZE));
        double offsetY = Math.max(0, Math.min(playerCenterY - (VIEWPORT_HEIGHT * TILE_SIZE) / 2,
                MAP_HEIGHT * TILE_SIZE - VIEWPORT_HEIGHT * TILE_SIZE));

        cameraTransform.setToIdentity();
        cameraTransform.appendScale(2, 2);
        cameraTransform.appendTranslation(-offsetX, -offsetY);

        tileMap.drawMap(gc);
        enemyManager.render(gc);
        professorManager.render(gc);
        slime1Manager.render(gc);
        treasureManager.render(gc);
        explosionManager.render(gc);

        // Adjust player sprite size when attacking
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

        // === Dialogue Triggering Logic ===
        boolean playerNearProfessor = false;

        for (Professor professor : professorManager.getProfessors()) {
            if (professor.isNearPlayer(player)) {
                playerNearProfessor = true;

                // Show intro dialogue if not already shown
                if (!dialogueBox.isVisible() && !farewellTriggered) {
                    List<String> introDialogue = dialogueManager.getIntroDialogue(mapName);
                    if (introDialogue != null && !introDialogue.isEmpty()) {
                        dialogueBox.show(introDialogue);
                        System.out.println("Player is near professor. Showing intro dialogue.");
                    }
                }

                // Enable input field
                if (!farewellTriggered) {
                    dialogueBox.getInputField().setDisable(false);
                    dialogueBox.getInputField().setVisible(true);
                }

                // Handle farewell input
                String inputText = dialogueBox.getInputField().getText().trim().toLowerCase();
                if ((inputText.equals("thank you") || inputText.equals("exit")) && !farewellTriggered) {
                    farewellTriggered = true;

                    List<String> farewellDialogue = DialogueManager.getResponseBasedOnInput(inputText, mapName);
                    if (farewellDialogue != null && !farewellDialogue.isEmpty()) {
                        dialogueBox.show(farewellDialogue);
                    }

                    // Disable input field after 2 seconds
                    PauseTransition disableInput = new PauseTransition(Duration.seconds(2));
                    disableInput.setOnFinished(e -> {
                        dialogueBox.getInputField().setDisable(true);
                        dialogueBox.getInputField().setVisible(false);
                        dialogueBox.getInputField().clear();
                    });
                    disableInput.play();

                    // Hide dialogue after 6 seconds
                    PauseTransition hideDialogue = new PauseTransition(Duration.seconds(6));
                    hideDialogue.setOnFinished(e -> {
                        dialogueBox.hide();
                        dialogueBox.getInputField().setVisible(false);
                    });
                    hideDialogue.play();
                }

                break; // Only interact with one professor at a time
            }
        }

// Reset farewell if moved away from all professors
        if (!playerNearProfessor) {
            farewellTriggered = false;

            if (dialogueBox.isVisible()) {
                dialogueBox.hide();
                dialogueBox.getInputField().setVisible(false);
            }
        }

// Render the dialogue box (ensure always rendered)
        DialogueBox.render(gc, player.getX(), player.getY(), canvas.getWidth(), canvas.getHeight());


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