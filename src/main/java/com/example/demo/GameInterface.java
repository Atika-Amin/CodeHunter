package com.example.demo;

import javafx.scene.layout.Pane;

public interface GameInterface {
    void pauseGame();
    void resumeGame();
    boolean isPaused();
    Pane getRoot();
    void setHasWon(boolean hasWon);
    void drawGame();
    void start() throws Exception;
}