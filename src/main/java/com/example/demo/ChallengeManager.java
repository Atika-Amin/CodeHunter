package com.example.demo;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class ChallengeManager {
    private static GameInterface game;  // generic game

    private static Pane root;

    private static int currentPoints = 0;
    private static int totalPoints = 0;
    private static Label pointsLabel;
    private static TreasureManagerBase currentManager;

    // You’ll call this method in game UI setup
    public static void setupPointsDisplay(Pane root) {
        currentPoints = 0;
        totalPoints = Session.getPoints();
        Image pointsIcon = new Image(ChallengeManager.class.getResource("/assets/other/pointimage.png").toExternalForm());
        ImageView pointsImageView = new ImageView(pointsIcon);
        pointsImageView.setFitWidth(120);
        pointsImageView.setFitHeight(30);

        // Create label to show the points
        pointsLabel = new Label("0");
        pointsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        pointsLabel.setTextFill(Color.web("#FFFFFF")); // Wooden color
        StackPane.setAlignment(pointsLabel, Pos.CENTER_LEFT); // Align to left center
        StackPane.setMargin(pointsLabel, new Insets(0, 0, 0, 20)); // More right


        // Stack the label on top of the image
        StackPane pointsPane = new StackPane();
        pointsPane.getChildren().addAll(pointsImageView, pointsLabel);
        pointsPane.setLayoutX(10); // top-left corner
        pointsPane.setLayoutY(10);
        pointsPane.setPrefSize(120, 50); // same as image

        // Add to root pane
        root.getChildren().add(pointsPane);

    }
    public static void addPoints(int amount) {
        currentPoints += amount;
        if (pointsLabel != null) {
            pointsLabel.setText(String.valueOf(currentPoints));
        }
        // Store to DB
        int userId = Session.getCurrentUserId();
        totalPoints+=currentPoints;
        Session.setPoints(totalPoints);
        DatabaseConnection.updatePoints(userId, totalPoints);
    }



    public static void init(GameInterface mapGame, Pane rootPane) {
        game = mapGame;
        root = rootPane;
    }

    public static void showChallenge(String question, String[] expectedKeywords, Runnable onSuccess)
    {
        Platform.runLater(() -> {
            game.pauseGame();


            if (root.getChildren().get(0) instanceof Canvas) {
                root.getChildren().get(0).setEffect(new GaussianBlur(15));
            }

            StackPane popup = new StackPane();

            Image frame = new Image(ChallengeManager.class.getResource("/assets/other/setting_BG.png").toExternalForm());
            ImageView frameView = new ImageView(frame);
            frameView.setFitWidth(495);    // You can bind this too if you want fully responsive popup
            frameView.setFitHeight(600);

// Spacer to push content down
            Region topSpacer = new Region();
            topSpacer.setPrefHeight(20);

            Text questionText = new Text(question);
            questionText.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            questionText.setFill(Color.web("#8B5E3C"));
            questionText.setWrappingWidth(300);

            VBox questionBox = new VBox(questionText);
            questionBox.setPadding(new Insets(10));
            questionBox.setMaxWidth(300);

            ScrollPane scrollPane = new ScrollPane(questionBox);
            scrollPane.setFitToWidth(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setPrefViewportHeight(280);
            scrollPane.setMaxHeight(300);
            scrollPane.setPrefViewportWidth(340);
            scrollPane.setMaxWidth(360);
            scrollPane.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-padding: 10;" +
                            "-fx-background-insets: 0;" +
                            "-fx-border-color: #5E3A1C;" +
                            "-fx-border-radius: 10;" +
                            "-fx-background-radius: 10;" +
                            "-fx-border-width: 2;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0.2, 2, 2);"
            );



            // Smaller and constrained TextArea
            TextArea answerArea = new TextArea();
            answerArea.setWrapText(true);
            answerArea.setFont(Font.font("Consolas", 18));
            answerArea.setPrefWidth(370);
            answerArea.setMinWidth(370);
            answerArea.setMaxWidth(370);
            answerArea.setPrefHeight(80);
            answerArea.setMinHeight(80);
            answerArea.setMaxHeight(80);

            // On first mouse click inside TextArea, enable editing
            answerArea.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                private boolean enabled = false;
                @Override
                public void handle(MouseEvent event) {
                    if (!enabled) {
                        answerArea.setEditable(true);
                        answerArea.requestFocus();
                        enabled = true;
                    }
                }
            });

            Label feedback = new Label();
            feedback.setFont(Font.font(14));
            feedback.setTextFill(Color.GREEN);
            feedback.setAlignment(Pos.CENTER);

            Button submit = new Button("Submit");
            submit.setPrefWidth(120);  // Slightly wider
            submit.setPrefHeight(35);  // Taller for better appearance
            submit.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Bigger, bold font

            submit.setStyle("-fx-background-color: linear-gradient(#C99C66, #8B5E3C);" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-color: #5E3A1C;" +
                    "-fx-border-width: 2;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 4, 0.2, 2, 2);");

            submit.setOnAction(e -> {
                String answer = answerArea.getText().toLowerCase();
                boolean isCorrect = true;

                for (String keyword : expectedKeywords) {
                    if (!answer.contains(keyword.toLowerCase())) {
                        isCorrect = false;
                        break;
                    }
                }

                if (isCorrect) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Treasure Solved!");
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(ChallengeManager.class.getResource("/com/example/demo/wooden-theme.css").toExternalForm());
                    dialogPane.getStyleClass().add("wooden-alert");
                        alert.setHeaderText("✅ Correct!");
                        alert.setContentText("You solved the treasure challenge!\n");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Incorrect Answer");
                    alert.setHeaderText("❌ Some keywords are missing.");
                    alert.setContentText("Please try again. Make sure you've included all required keywords.");
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(ChallengeManager.class.getResource("/com/example/demo/wooden-theme.css").toExternalForm());
                    dialogPane.getStyleClass().add("wooden-alert");
                    alert.showAndWait();
                }


                if (isCorrect) {
                    addPoints(1000);
                    if (onSuccess != null) {
                        onSuccess.run(); // ✅ Notify Treasure it's solved
                    }
                }


                new Thread(() -> {
                    try { Thread.sleep(2000); } catch (Exception ignored) {}
                    Platform.runLater(() -> {
                        root.getChildren().remove(popup);
                        if (root.getChildren().get(0) instanceof Canvas) {
                            root.getChildren().get(0).setEffect(null);
                        }
                        game.resumeGame();
                    });
                }).start();
            });

            VBox content = new VBox(10, topSpacer, scrollPane, answerArea, submit, feedback);
            content.setAlignment(Pos.CENTER);
            content.setPadding(new Insets(10));

            popup.setPrefSize(frameView.getFitWidth(), frameView.getFitHeight());
            popup.getChildren().addAll(frameView, content);
            popup.setScaleX(0.0);
            popup.setScaleY(0.0);

            root.getChildren().add(popup);
            // Center popup function
            Runnable centerPopup = () -> {
                double x = (root.getWidth() - popup.getWidth()) / 2;
                double y = (root.getHeight() - popup.getHeight()) / 2;
                popup.setLayoutX(x);
                popup.setLayoutY(y);
            };

// Add listeners to keep popup centered on window resize
            root.widthProperty().addListener((obs, oldVal, newVal) -> centerPopup.run());
            root.heightProperty().addListener((obs, oldVal, newVal) -> centerPopup.run());

// Center popup after layout completes
            Platform.runLater(centerPopup);

            ScaleTransition popIn = new ScaleTransition(Duration.millis(700), popup);
            popIn.setFromX(0.0);
            popIn.setFromY(0.0);
            popIn.setToX(1.0);
            popIn.setToY(1.0);
            popIn.setInterpolator(Interpolator.EASE_OUT);
            popIn.play();



            popup.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
                popup.setLayoutX((root.getWidth() - popup.getPrefWidth()) / 2);
                popup.setLayoutY((root.getHeight() - popup.getPrefHeight()) / 2);
            });
        });
    }
    public void applyBlur(Canvas canvas) {
        canvas.setEffect(new GaussianBlur(15));
    }

}
