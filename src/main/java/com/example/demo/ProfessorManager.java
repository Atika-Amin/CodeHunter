package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class ProfessorManager {
    private List<Professor> professors = new ArrayList<>();

    public ProfessorManager(TileMap map) {
        loadProfessorsFromMap(map);
    }

    private void loadProfessorsFromMap(TileMap tileMap) {
        List<JsonNode> objectLayers = tileMap.getObjectLayers();

        for (JsonNode layer : objectLayers) {
            JsonNode nameNode = layer.get("name");
            if (nameNode != null && "professor".equals(nameNode.asText())) {
                for (JsonNode obj : layer.get("objects")) {
                    if (obj.has("name") && obj.get("name").asText().equalsIgnoreCase("professor")) {
                        double x = obj.get("x").asDouble();
                        double y = obj.get("y").asDouble();
                        String professorId = obj.has("id") ? obj.get("id").asText() : String.valueOf((int) x);
                        String objectName = "professor_" + professorId;

                        JsonNode properties = obj.get("properties");
                        String walkLeft = "", walkRight = "", idleLeft = "", idleRight = "";

                        if (properties != null) {
                            for (JsonNode prop : properties) {
                                String propName = prop.get("name").asText();
                                String value = prop.get("value").asText();

                                switch (propName) {
                                    case "walkLeft": walkLeft = value; break;
                                    case "walkRight": walkRight = value; break;
                                    case "idle_left": idleLeft = value; break;
                                    case "idle_right": idleRight = value; break;
                                }
                            }
                        }

                        // Ensure we have all the required properties
                        if (walkLeft.isEmpty() || walkRight.isEmpty() || idleLeft.isEmpty() || idleRight.isEmpty()) {
                            System.err.println("⚠️ Skipping professor at (" + x + ", " + y + "): Missing sprite properties!");
                            continue;
                        }
                        double patrolStartX = x - 20;
                        double patrolEndX = x + 20;

                        // Create the Professor object
                        Professor professor = new Professor(walkLeft, walkRight, idleLeft, idleRight, x, y,patrolStartX,patrolEndX);
                        professors.add(professor);
                    }
                }
            }
        }
    }

    public void update(Player player) {
        for (Professor professor : professors) {
            professor.update(player);
        }
    }

    public void render(GraphicsContext gc) {
        for (Professor professor : professors) {
            professor.render(gc);
        }
    }

    public List<Professor> getProfessors() {
        return professors;
    }
}
