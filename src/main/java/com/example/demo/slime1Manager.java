package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class slime1Manager {
    private List<slime1> slimes = new ArrayList<>();
    private Map<String, double[]> patrolRanges = new HashMap<>();

    public slime1Manager(TileMap map,int health) {
        loadslimesFromMap(map,health);
    }

    private void loadslimesFromMap(TileMap tileMap,int health) {
        List<JsonNode> objectLayers = tileMap.getObjectLayers();

        for (JsonNode layer : objectLayers) {
            JsonNode nameNode = layer.get("name");
            if (nameNode != null && "slime1".equals(nameNode.asText())) {
                for (JsonNode obj : layer.get("objects")) {
                    if (obj.has("name") && obj.get("name").asText().equalsIgnoreCase("slime1")) {
                        double x = obj.get("x").asDouble();
                        double y = obj.get("y").asDouble();
                        String enemyId = obj.has("id") ? obj.get("id").asText() : String.valueOf((int) x);
                        String objectName = "slime1_" + enemyId;

                        JsonNode properties = obj.get("properties");
                        // Now you need more sprite variables
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

                        double patrolStartX = x - 20;
                        double patrolEndX = x + 20;
                        double patrolStartY = y - 20;
                        double patrolEndY = y + 20;

                        if (patrolRanges.containsKey(objectName)) {
                            patrolStartX = patrolRanges.get(objectName)[0];
                            patrolEndX = patrolRanges.get(objectName)[1];
                            patrolStartY = patrolRanges.get(objectName)[2];
                            patrolEndY = patrolRanges.get(objectName)[3];
                        }

                        // Now pass the new parameters
                        slime1 Slime1 = new slime1(
                                front, back, left, right,
                                walkFront, walkBack, walkLeft, walkRight,
                                hurtFront, hurtBack, hurtLeft, hurtRight,
                                deadFront, deadBack, deadLeft, deadRight,
                                x, y, patrolStartX, patrolEndX, patrolStartY, patrolEndY,health
                        );

                        slimes.add(Slime1);
                    }
                }
            }
        }
    }

    public void update(Player player) {
        for (slime1 Slime1 : slimes) {
            Slime1.update(player);
            if (!Slime1.isAlive() && !Slime1.wasCountedForPoints()) {
                Slime1.markCounted();
                ChallengeManager.addPoints(100); // or however many points per kill
            }

        }
    }

    public void render(GraphicsContext gc) {
        for (slime1 Slime1 : slimes) {
            Slime1.render(gc);
        }
    }

    public List<slime1> getslimes() {
        return slimes;
    }
}
