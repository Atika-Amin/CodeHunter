package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class TreasureManager4 implements TreasureManagerBase{

    private List<Treasure> treasures;

    public TreasureManager4(TileMap map){
        treasures = new ArrayList<>();
        loadTreasuresFromMap(map);
    }

    public TreasureManager4() {
        treasures = new ArrayList<>();
    }

    // Add a treasure to the manager
    public void addTreasure(Treasure treasure) {
        treasures.add(treasure);
    }

    // Update all treasures (check for collisions, interactions, etc.)
    public void update(Player player) {
        for (Treasure treasure : treasures) {
            treasure.update(player);  // Update each treasure's state

        }
    }

    private void loadTreasuresFromMap(TileMap tileMap) {
        List<JsonNode> objectLayers = tileMap.getObjectLayers();

        for (JsonNode layer : objectLayers) {
            JsonNode nameNode = layer.get("name");
            if (nameNode != null && "treasure".equals(nameNode.asText())) {
                for (JsonNode obj : layer.get("objects")) {
                    if (obj.has("name") && (obj.get("name").asText().equalsIgnoreCase("treasure1")||obj.get("name").asText().equalsIgnoreCase("treasure2")||
                            obj.get("name").asText().equalsIgnoreCase("treasure3")||obj.get("name").asText().equalsIgnoreCase("treasure4")||
                            obj.get("name").asText().equalsIgnoreCase("treasure5")||obj.get("name").asText().equalsIgnoreCase("treasure6"))) {



                        String treasureName = obj.has("name") ? obj.get("name").asText() : "unknown";


                        double x = obj.get("x").asDouble();
                        double y = obj.get("y").asDouble();
                        String treasureId = obj.has("id") ? obj.get("id").asText() : String.valueOf((int) x);
                        String objectName = "treasure_" + treasureId;

                        JsonNode properties = obj.get("properties");
                        String frontImagePath = "";  // This will hold the front image path
                        String openImagePath = "";   // This will hold the open image path

                        if (properties != null) {
                            for (JsonNode prop : properties) {
                                String propName = prop.get("name").asText();
                                String value = prop.get("value").asText();

                                // Assuming you're using properties for the front and open image paths
                                if ("front".equals(propName)) {
                                    frontImagePath = value;
                                }
                                if ("open".equals(propName)) {
                                    openImagePath = value;
                                }
                            }
                        }

                        if (frontImagePath.isEmpty() || openImagePath.isEmpty()) {
                            continue;
                        }



                        // Now create the Treasure object using the updated constructor
                        Treasure treasure = new Treasure(frontImagePath, openImagePath, x, y, getChallengeFor(treasureName));

                        // Add the treasure to your collection (assuming treasures is a list)
                        treasures.add(treasure);
                    }
                }
            }
        }
    }

    private Challenge getChallengeFor(String name) {
        switch (name.toLowerCase()) {
            case "treasure1":
                return new Challenge(
                        "Write a C program to define a structure for a student (name, roll, marks) and display the data.",
                        new String[] { "struct", "char", "int", "printf" }
                );

            case "treasure3":
                return new Challenge(
                        "Write a C program to dynamically allocate memory for an array of integers and find the average.",
                        new String[] { "malloc", "int", "for", "free" }
                );

            case "treasure4":
                return new Challenge(
                        "Write a C program to read and write to a file using fopen, fprintf, and fclose.",
                        new String[] { "FILE", "fopen", "fprintf", "fclose" }
                );

            case "treasure5":
                return new Challenge(
                        "Write a C program to access structure members using a pointer to a structure.",
                        new String[] { "struct", "->", "pointer", "printf" }
                );

            case "treasure6":
                return new Challenge(
                        "Write a C program to store and display information of 3 students using an array of structures.",
                        new String[] { "struct", "array", "for", "printf" }
                );
        }

        return null;

    }


    public List<Treasure> getTreasures() {
        return treasures;
    }



    // Render all treasures on the screen
    public void render(GraphicsContext gc) {
        for (Treasure treasure : treasures) {
            treasure.render(gc);  // Render each treasure
        }
    }
}
