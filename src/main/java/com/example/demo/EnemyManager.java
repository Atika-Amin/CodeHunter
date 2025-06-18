package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnemyManager {

    private List<Enemy> enemies = new ArrayList<>();
    private Map<String, double[]> patrolRanges = new HashMap<>();

    public EnemyManager(TileMap map,int health) {
        loadEnemiesFromMap(map,health);
    }

    private void loadEnemiesFromMap(TileMap tileMap ,int health) {
        List<JsonNode> objectLayers = tileMap.getObjectLayers();

        for (JsonNode layer : objectLayers) {
            JsonNode nameNode = layer.get("name");
            if (nameNode != null && "enemies".equals(nameNode.asText())) {
                for (JsonNode obj : layer.get("objects")) {
                    if (obj.has("name") && obj.get("name").asText().equalsIgnoreCase("enemy")) {
                        double x = obj.get("x").asDouble();
                        double y = obj.get("y").asDouble();
                        String enemyId = obj.has("id") ? obj.get("id").asText() : String.valueOf((int) x);
                        String objectName = "enemy_" + enemyId;

                        JsonNode properties = obj.get("properties");
                        String front = "", back = "", left = "", right = "";
                        String walkFront = "", walkBack = "", walkLeft = "", walkRight = "";
                        String hurtFront = "", hurtBack = "", hurtLeft = "", hurtRight = "";
                        String deadFront = "", deadBack = "", deadLeft = "", deadRight = "";


                        if (properties != null) {
                            for (JsonNode prop : properties) {
                                String propName = prop.get("name").asText();
                                String value = prop.get("value").asText();

                                switch (propName) {
                                    case "spriteFront":
                                        front = value;
                                        break;
                                    case "spriteBack":
                                        back = value;
                                        break;
                                    case "spriteLeft":
                                        left = value;
                                        break;
                                    case "spriteRight":
                                        right = value;
                                        break;
                                    case "walkFront":
                                        walkFront = value;
                                        break;
                                    case "walkBack":
                                        walkBack = value;
                                        break;
                                    case "walkLeft":
                                        walkLeft = value;
                                        break;
                                    case "walkRight":
                                        walkRight = value;
                                        break;
                                    case "hurtFront":
                                        hurtFront = value;
                                        break;
                                    case "hurtBack":
                                        hurtBack = value;
                                        break;
                                    case "hurtLeft":
                                        hurtLeft = value;
                                        break;
                                    case "hurtRight":
                                        hurtRight = value;
                                        break;
                                    case "deadFront":
                                        deadFront = value;
                                        break;
                                    case "deadBack":
                                        deadBack = value;
                                        break;
                                    case "deadLeft":
                                        deadLeft = value;
                                        break;
                                    case "deadRight":
                                        deadRight = value;
                                        break;
                                }
                            }
                        }



                        if (front.isEmpty() || back.isEmpty() || left.isEmpty() || right.isEmpty()
                                || walkFront.isEmpty() || walkBack.isEmpty() || walkLeft.isEmpty() || walkRight.isEmpty()
                                || hurtFront.isEmpty() || hurtBack.isEmpty() || hurtLeft.isEmpty() || hurtRight.isEmpty()
                                || deadFront.isEmpty() || deadBack.isEmpty() || deadLeft.isEmpty() || deadRight.isEmpty()) {
                            System.err.println("⚠️ Skipping enemy at (" + x + ", " + y + "): Missing sprite properties!");
                            continue;
                        }

                        // For the x axis, patrolStartX and patrolEndX are already set as per your logic.
// You can apply the same concept for the y axis.

                        double patrolStartX = x - 20;
                        double patrolEndX = x + 20;
                        double patrolStartY = y - 20;  // Set a range for the y-axis (e.g., 20 units above the current position)
                        double patrolEndY = y + 20;    // Set a range for the y-axis (e.g., 20 units below the current position)

// If patrol ranges are provided for specific objects, use them
                        if (patrolRanges.containsKey(objectName)) {
                            patrolStartX = patrolRanges.get(objectName)[0];
                            patrolEndX = patrolRanges.get(objectName)[1];
                            patrolStartY = patrolRanges.get(objectName)[2];  // Assuming you store vertical patrol bounds as well
                            patrolEndY = patrolRanges.get(objectName)[3];    // Same assumption
                        }


// Create the Enemy object with the x, y, patrolStartX, patrolEndX, patrolStartY, and patrolEndY values
                        Enemy enemy = new Enemy(
                                front, back, left, right,
                                walkFront, walkBack, walkLeft, walkRight,
                                hurtFront, hurtBack, hurtLeft, hurtRight,
                                deadFront, deadBack, deadLeft, deadRight,
                                x, y, patrolStartX, patrolEndX, patrolStartY, patrolEndY,health
                        );




                        enemies.add(enemy);
                    }
                }
            }
        }
    }


    public void update(Player player) {
        for (Enemy enemy : enemies) {
            enemy.update(player);
        }
    }

    public void render(GraphicsContext gc) {
        for (Enemy enemy : enemies) {
            enemy.render(gc);
        }
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
}



