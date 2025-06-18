package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;
import java.util.List;

public class SpriteLoader {

    // Load frames from sprite sheet (with columns and rows)
    public static Image[] loadFrames(String imagePath, int frameWidth, int frameHeight, int columns, int rows) {
        List<Image> frames = new ArrayList<>();
        try {
            System.out.println("imagepath: "+imagePath);
            var stream = SpriteLoader.class.getResourceAsStream("/" + imagePath);
            if (stream == null) {
                System.err.println("❌ Sprite sheet not found at path: /" + imagePath);
                return new Image[0];
            }
            Image image = new Image(stream);

            int sheetWidth = (int) image.getWidth();
            int sheetHeight = (int) image.getHeight();

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    int x = col * frameWidth;
                    int y = row * frameHeight;

                    if (x + frameWidth > sheetWidth || y + frameHeight > sheetHeight) {
                        System.err.println("⚠️ Frame out of bounds at (" + x + "," + y + ")");
                        continue;
                    }

                    WritableImage frame = new WritableImage(image.getPixelReader(), x, y, frameWidth, frameHeight);
                    frames.add(frame);
                    System.out.println("✅ Loaded frame at col " + col + ", row " + row);
                }
            }

        } catch (Exception e) {
            System.err.println("💥 Exception while loading frames from image: " + imagePath);
            e.printStackTrace();
        }

        return frames.toArray(new Image[0]);
    }

}
