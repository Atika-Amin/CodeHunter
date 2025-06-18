package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.GameConfig.VIEWPORT_HEIGHT;
import static com.example.demo.GameConfig.VIEWPORT_WIDTH;

public class TileMap {
    private static final int TILE_SIZE = 32;
    private static final int MAP_WIDTH = 40;
    private static final int MAP_HEIGHT = 30;
    private static final int SCALE = 3; // Increase from 2 to 3 for a bigger screen

    private int[][][] tileLayers;
    private List<JsonNode> objectLayers = new ArrayList<>();
    private Map<Integer, Image> tileImages = new HashMap<>();
    private Canvas canvas;

    public TileMap(InputStream mapStream, InputStream tilesetStream) throws IOException {
        canvas = new Canvas(VIEWPORT_WIDTH * TILE_SIZE * SCALE, VIEWPORT_HEIGHT * TILE_SIZE * SCALE);
        loadMapData(mapStream);
        loadTileset(tilesetStream);
    }


    private void loadMapData(InputStream mapStream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(mapStream);


        JsonNode layers = rootNode.get("layers");
        int tileLayerCount = 0;
        for (JsonNode layer : layers) {
            if (layer.get("type").asText().equals("tilelayer")) {
                tileLayerCount++;
            }
        }

        tileLayers = new int[tileLayerCount][MAP_HEIGHT][MAP_WIDTH];
        int tileIndex = 0;
        for (JsonNode layer : layers) {
            String type = layer.get("type").asText();
            if (type.equals("tilelayer")) {
                JsonNode layerData = layer.get("data");
                for (int i = 0; i < layerData.size(); i++) {
                    int row = i / MAP_WIDTH;
                    int col = i % MAP_WIDTH;
                    tileLayers[tileIndex][row][col] = layerData.get(i).asInt();
                }
                tileIndex++;
            } else if (type.equals("objectgroup")) {
                objectLayers.add(layer); // Store the whole layer, not just the "objects"
            }
        }
    }


    private void loadTileset(InputStream tilesetStream) {
        Image tileset = new Image(tilesetStream);

        int tilesetColumns = (int) tileset.getWidth() / TILE_SIZE;
        int tilesetRows = (int) tileset.getHeight() / TILE_SIZE;
        PixelReader pixelReader = tileset.getPixelReader();

        int tileID = 1;
        for (int row = 0; row < tilesetRows; row++) {
            for (int col = 0; col < tilesetColumns; col++) {
                WritableImage tile = new WritableImage(pixelReader, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                tileImages.put(tileID++, tile);
            }
        }
    }


    public void drawMap(GraphicsContext gc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int layer = 0; layer < tileLayers.length; layer++) {
            for (int row = 0; row < MAP_HEIGHT; row++) {
                for (int col = 0; col < MAP_WIDTH; col++) {
                    int tileID = tileLayers[layer][row][col];
                    if (tileID != 0 && tileImages.containsKey(tileID)) {
                        gc.drawImage(tileImages.get(tileID), col * TILE_SIZE, row * TILE_SIZE);
                    }
                }
            }
        }
    }

    public boolean isWalkable(double x, double y) {
        double hitboxPadding = 3; // Reduce hitbox size slightly for better movement
        double playerWidth = TILE_SIZE - hitboxPadding * 2;
        double playerHeight = TILE_SIZE - hitboxPadding * 2;

        if (x < 0 || x + playerWidth >= MAP_WIDTH * TILE_SIZE ||
                y < 0 || y + playerHeight >= MAP_HEIGHT * TILE_SIZE) {
            return false; // Out of bounds
        }

        // Check for tile-based collisions (wall tiles)
        int tileX = (int) (x / TILE_SIZE);
        int tileY = (int) (y / TILE_SIZE);

        // For all tile layers, check if there's a wall tile at the player's position
        for (int layer = 0; layer < tileLayers.length; layer++) {
            int tileID = tileLayers[layer][tileY][tileX];

            // List of wall tile IDs (update this based on your specific wall tile IDs)
            List<Integer> wallTileIDs = List.of(1, 2, 3); // Replace with actual wall tile IDs

            if (wallTileIDs.contains(tileID)) { // If the tile is a wall
                return false; // Collision with wall
            }
        }

        JsonNode targetLayer = null;
        // Check for object layer collisions (objects placed on top of tiles)

        for (JsonNode objectLayer : objectLayers) {
            JsonNode nameNode = objectLayer.get("name");
            if (nameNode != null && "Object Layer 1".equals(nameNode.asText())) {
                targetLayer = objectLayer;
                break;
            }
        }

        if (targetLayer != null) {
            JsonNode objectsArray = targetLayer.get("objects");
            if (objectsArray != null) {
                for (JsonNode object : objectsArray) {
                    double objectX = object.get("x").asDouble();
                    double objectY = object.get("y").asDouble();
                    double objectWidth = object.get("width").asDouble();
                    double objectHeight = object.get("height").asDouble();

                    if (x + hitboxPadding < objectX + objectWidth &&
                            x + playerWidth + hitboxPadding > objectX &&
                            y + hitboxPadding < objectY + objectHeight &&
                            y + playerHeight + hitboxPadding > objectY) {
                        return false; // Collision with an object
                    }
                }
            }
        }
        return true; // No collision
    }


    public List<JsonNode> getObjectLayers() {
        return objectLayers;
    }

    public Canvas getCanvas() {
        return canvas;
    }
    public String getTreasureImagePath() {
        for (JsonNode objectLayer : objectLayers) {
            JsonNode objectsArray = objectLayer.get("objects");
            if (objectsArray != null) {
                for (JsonNode object : objectsArray) {
                    String objectName = object.get("name").asText();
                    if ("treasure".equals(objectName)) {
                        JsonNode properties = object.get("properties");
                        if (properties != null) {
                            for (JsonNode property : properties) {
                                if ("open".equals(property.get("name").asText())) {
                                    return property.get("value").asText(); // Return the image path
                                }
                            }
                        }
                    }
                }
            }
        }
        return null; // Return null if no treasure object is found
    }


}
//
//    // ✅ NEW: Extract patrol range from "EnemyWalkRange" layer
//    public Map<Integer, double[]> getEnemyPatrolRanges() {
//        System.out.println("[DEBUG] getEnemyPatrolRanges() called");
//
//        Map<Integer, double[]> patrolRanges = new HashMap<>();
//
//        if (objectLayers == null) {
//            System.out.println("[ERROR] objectLayers is null!");
//            return patrolRanges;
//        }
//
//        for (JsonNode objectLayer : objectLayers) {
//            if (objectLayer == null) {
//                System.out.println("[WARNING] Null objectLayer found, skipping...");
//                continue;
//            }
//
//            JsonNode nameNode = objectLayer.get("name");
//            String layerName = (nameNode != null) ? nameNode.asText() : "(no name)";
//            System.out.println("[DEBUG] Checking layer: '" + layerName + "'");
//
//            if (nameNode != null && "EnemyWalkRange".equals(nameNode.asText())) {
//                System.out.println("[DEBUG] Found EnemyWalkRange layer!");
//
//                JsonNode objectsArray = objectLayer.get("objects");
//                if (objectsArray == null) {
//                    System.out.println("[ERROR] EnemyWalkRange layer has no 'objects' array!");
//                    continue;
//                }
//
//                for (JsonNode object : objectsArray) {
//                    if (object == null) {
//                        System.out.println("[WARNING] Null object found in objects array, skipping...");
//                        continue;
//                    }
//
//                    double x = object.has("x") ? object.get("x").asDouble() : 0.0;
//                    double width = object.has("width") ? object.get("width").asDouble() : 0.0;
//                    double startX = x;
//                    double endX = x + width;
//
//                    System.out.println("[DEBUG] Object found: startX=" + startX + ", endX=" + endX);
//
//                    JsonNode properties = object.get("properties");
//                    if (properties == null) {
//                        System.out.println("[WARNING] Object has no properties! Skipping...");
//                        continue;
//                    }
//
//                    boolean enemyIdFound = false;
//                    for (JsonNode property : properties) {
//                        if (property == null) continue;
//
//                        String propName = property.has("name") ? property.get("name").asText() : "(no name)";
//                        System.out.println("[DEBUG] Found property: " + propName);
//
//                        if ("enemyid".equals(propName)) {
//                            int enemyId = property.has("value") ? property.get("value").asInt() : -1;
//                            patrolRanges.put(enemyId, new double[]{startX, endX});
//                            System.out.println("[DEBUG] Added patrol range for enemyId=" + enemyId);
//                            enemyIdFound = true;
//                            break;
//                        }
//                    }
//
//                    if (!enemyIdFound) {
//                        System.out.println("[WARNING] No 'enemyid' property found for object starting at x=" + startX);
//                    }
//                }
//            }
//        }
//
//        System.out.println("[DEBUG] Finished getEnemyPatrolRanges()");
//        return patrolRanges;
//    }
//

