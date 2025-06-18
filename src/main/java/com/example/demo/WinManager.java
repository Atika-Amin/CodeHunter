package com.example.demo;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

public class WinManager {
    private boolean hasShownWin = false;

    public void checkAndShowWin(TreasureManagerBase treasureManager, Pane root, Canvas canvas,
                                GameInterface game, int requiredSolvedCount, String mapName) {
        System.out.println("checkAndShowWin called");

        if (hasShownWin) return;

        int solvedCount = 0;
        for (Treasure t : treasureManager.getTreasures()) {
            System.out.println("Treasure solved? " + t.isSolved());
            if (t.isSolved()) {
                solvedCount++;
            }
        }

        System.out.println("Total solved treasures: " + solvedCount);

        if (solvedCount >= requiredSolvedCount) {
            hasShownWin = true;
            System.out.println("✅ Enough treasures solved. Showing Win Overlay...");

            if (game != null) {
                game.drawGame(); // Draw final state before blurring
                game.pauseGame();
                game.setHasWon(true);
            }
            // 🎯 Update user's max_level based on map name
            int completedLevel = getLevelFromMap(mapName);
            int userId = Session.getCurrentUserId();
            int currentMaxLevel = DatabaseConnection.getUserMaxLevel(userId);

            if (completedLevel > currentMaxLevel) {
                DatabaseConnection.updateUserMaxLevel(userId, completedLevel);
            }

            new YouWinOverlay(root, canvas.getWidth(), canvas.getHeight(), canvas);
        }
    }
    private int getLevelFromMap(String mapName) {
        switch (mapName) {
            case "map1": return 1;
            case "map2": return 2;
            case "map3": return 3;
            case "map4": return 4;
            case "map5": return 5;
            // Add more maps here as needed
            default: return 0; // Unknown map
        }
    }
}


