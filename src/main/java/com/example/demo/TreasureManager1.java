package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class TreasureManager1 implements TreasureManagerBase {

    private static List<Treasure> treasures;
    private final int treasureId = 2;
    public int getTreasureId() {
        return treasureId;
    }

    public TreasureManager1(TileMap map){
        treasures = new ArrayList<>();
        loadTreasuresFromMap(map);
    }

    public TreasureManager1() {
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
                        "🏕️ Village Festival Access Checker\n\n" +
                                "🧩 Scenario: A village festival has 4 zones: Kids, Teen, Adult, and Senior. Based on the visitor’s age and which zones are open, the program should decide whether a person can enter and to which zone.\n\n" +
                                "🛠️ Task: Input the number of people `N`, and 4 binary flags (K, T, A, S) for zone availability. Then take `N` ages and print the zone they can access or 'Not Allowed'.\n\n" +
                                "📥 Sample Input:\n" +
                                "4\n" +
                                "1 0 1 0\n" +
                                "10\n" +
                                "15\n" +
                                "35\n" +
                                "70\n\n" +
                                "📤 Sample Output:\n" +
                                "Kids Zone\n" +
                                "Not Allowed\n" +
                                "Adult Zone\n" +
                                "Not Allowed\n",
                        new String[] { "int", "if", "else", "scanf", "printf", "for", "nested if" }
                );


            case "treasure3":
                return new Challenge(
                        "📅 Weekly Schedule Optimizer\n\n" +
                                "🧩 Scenario: A student follows a weekly routine based on the day of the week. Your task is to print the corresponding activity.\n\n" +
                                "🛠️ Task: Input `T`, then take `T` day numbers (1 for Monday to 7 for Sunday). Print the task for each day using switch-case.\n\n" +
                                "📥 Sample Input:\n" +
                                "4\n" +
                                "1\n" +
                                "3\n" +
                                "6\n" +
                                "7\n\n" +
                                "📤 Sample Output:\n" +
                                "Study\n" +
                                "Reading\n" +
                                "Relax\n" +
                                "Relax\n",
                        new String[] { "int", "switch", "case", "scanf", "printf", "break", "for", "%d" }
                );


            case "treasure4":
                return new Challenge(
                        "➕ Sum of Multiples\n\n" +
                                "🧩 Scenario: A company wants a program that helps analyze numbers. It should calculate the sum of all numbers from 1 to N that are divisible by 3 or 5.\n\n" +
                                "🛠️ Task: Take a positive integer `N` as input. Use a loop to find all numbers from 1 to N that are divisible by 3 or 5, and print their sum.\n\n" +
                                "📥 Sample Input:\n" +
                                "10\n\n" +
                                "📤 Sample Output:\n" +
                                "Sum: 33\n\n" +
                                "🧠 Explanation:\n" +
                                "Numbers divisible by 3 or 5: 3, 5, 6, 9, 10 → sum = 33",
                        new String[] { "int", "for", "scanf", "printf", "%d", "%", "if", "+=" }
                );

        }

        return null;

    }
    public static int getRemainingTreasureCount() {
        int count = 2;
        for (Treasure treasure : treasures) {
            if (treasure.isSolved()) {
                count--;
            }
        }
        return count;
    }
    public boolean allTreasuresSolved() {
        int index = 1;
        for (Treasure treasure : treasures) {


            if (!treasure.isSolved()) {

                return false;
            }
            index++;
        }
        index--;
        return true;
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
