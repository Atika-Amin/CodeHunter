package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class TreasureManager implements TreasureManagerBase {
    private static List<Treasure> treasures;

    public TreasureManager(TileMap map){
        treasures = new ArrayList<>();
        loadTreasuresFromMap(map);
    }

    public TreasureManager() {
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

                        System.out.println("load treasure from map is called!!");

                        String treasureName = obj.has("name") ? obj.get("name").asText() : "unknown";
                        System.out.println("✅ Treasure created: " + treasureName);  // Debug line

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
                            System.err.println("⚠️ Skipping treasure at (" + x + ", " + y + "): Missing image properties!");
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
                        "📦 Product Bill Generator\n\n" +
                                "🧩 Scenario: A shop wants a C program that asks the user for a product name, unit price, and quantity. Then, it should calculate and display the total price.\n\n" +
                                "🛠️ Task: Take product name (string), unit price (float), and quantity (int) as input. Multiply price by quantity and show total cost.\n\n" +
                                "📥 Sample Input:\n" +
                                "Product: Pen\n" +
                                "Price: 10.5\n" +
                                "Quantity: 3\n\n" +
                                "📤 Sample Output:\n" +
                                "Product: Pen\n" +
                                "Total Cost: $31.50\n",
                        new String[] { "char", "float", "int", "scanf", "printf", "*", "%s", "%f", "%d" }
                );

            case "treasure3":
                return new Challenge(
                        "📚 GPA Percentage Calculator\n\n" +
                                "🧩 Scenario: A university wants to convert a student's GPA to a percentage. Each GPA point equals 20%. The program should print the name, ID, GPA, and percentage.\n\n" +
                                "🛠️ Task: Declare and initialize the student's name (char), ID (int), and GPA (float). Then calculate percentage = GPA * 20 and display everything.\n\n" +
                                "📤 Sample Output:\n" +
                                "Name: Sarah\n" +
                                "ID: 1024\n" +
                                "GPA: 3.5\n" +
                                "Percentage: 70.00%\n",
                        new String[] { "char", "int", "float", "printf", "*", "=", "%.2f", "%s", "%d" }
                );

            case "treasure4":
                return new Challenge(
                        "📐 Circle Area Calculator\n\n" +
                                "🧩 Scenario: An engineer wants a program that takes the radius of a circle from the user and calculates the area.\n\n" +
                                "🛠️ Task: Take a float input for radius, calculate area using the formula `area = 3.14 * radius * radius`, and print the result.\n\n" +
                                "📥 Sample Input:\n" +
                                "Enter radius: 5\n\n" +
                                "📤 Sample Output:\n" +
                                "Radius: 5.00\n" +
                                "Area: 78.50\n",
                        new String[] { "float", "scanf", "printf", "*", "=", "%.2f" }
                );
        }

        return null; // Return null if no case matches

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

            System.out.println("Treasure " + index + " solved? " + treasure.isSolved());
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
