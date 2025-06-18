package com.example.demo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Explosion {
    private double x, y;
    private boolean triggered = false;
    private boolean finished = false;
    private long startTime;
    private final int DAMAGE = 15;

    private Image[] frames;
    private int frameIndex = 0;
    private int frameRate = 100; // ms per frame
    private long lastFrameTime;

    public Explosion(double x, double y, String imagePath) {
        this.x = x;
        this.y = y;
        this.frames = SpriteLoader.loadFrames(imagePath, 32, 32, 4, 4); // assuming 6-frame animation
    }

    public void update(Player player) {
        if (finished) return;

        double dx = x - player.getSprite().getX();
        double dy = y - player.getSprite().getY();
        double distance = Math.hypot(dx, dy);

        if (!triggered && distance < 40) {
            triggered = true;
            startTime = System.currentTimeMillis();
            lastFrameTime = startTime;
            SoundManager.playExplosion();
            player.takeDamage(DAMAGE);
            System.out.println("Explosion triggered! Player takes " + DAMAGE + " damage.");
        }

        if (triggered) {
            long now = System.currentTimeMillis();
            if (now - lastFrameTime > frameRate) {
                frameIndex++;
                lastFrameTime = now;
                if (frameIndex >= frames.length) {
                    finished = true;
                }
            }
        }
    }

    public void render(GraphicsContext gc) {
        if (triggered && !finished) {
            gc.drawImage(frames[frameIndex], x, y);
        }
    }

    public boolean isFinished() {
        return finished;
    }
}

