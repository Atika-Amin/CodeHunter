package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class ExplosionManager {
    private List<Explosion> explosions;

    public ExplosionManager(TileMap tileMap) {
        explosions = new ArrayList<>();
        loadExplosionsFromMap(tileMap);
    }

    public ExplosionManager() {
        explosions = new ArrayList<>();
    }

//    public void addExplosion(Explosion explosion) {
//        explosions.add(explosion);
//    }

    public void update(Player player) {
        for (Explosion explosion : explosions) {
            explosion.update(player);
        }
    }

    public void render(GraphicsContext gc) {
        for (Explosion explosion : explosions) {
            explosion.render(gc);
        }
    }

    private void loadExplosionsFromMap(TileMap tileMap) {
        List<JsonNode> objectLayers = tileMap.getObjectLayers();

        for (JsonNode layer : objectLayers) {
            JsonNode nameNode = layer.get("name");
            if (nameNode != null && "explosion".equals(nameNode.asText())) {
                for (JsonNode obj : layer.get("objects")) {
                    if (obj.has("name") && obj.get("name").asText().equalsIgnoreCase("explosion")) {
                        double x = obj.get("x").asDouble();
                        double y = obj.get("y").asDouble();
                        String explosionId = obj.has("id") ? obj.get("id").asText() : String.valueOf((int) x);

                        JsonNode properties = obj.get("properties");
                        String explosionImagePath = "";

                        if (properties != null) {
                            for (JsonNode prop : properties) {
                                String propName = prop.get("name").asText();
                                String value = prop.get("value").asText();

                                if ("explosion".equals(propName)) {
                                    explosionImagePath = value;
                                }
                            }
                        }

                        if (explosionImagePath.isEmpty()) {
                            System.err.println("⚠️ Skipping explosion at (" + x + ", " + y + "): Missing image path!");
                            continue;
                        }

                        Explosion explosion = new Explosion(x, y, explosionImagePath);
                        explosions.add(explosion);
                    }
                }
            }
        }
    }
}
