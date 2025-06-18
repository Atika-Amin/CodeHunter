package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class TreasureManager3 implements TreasureManagerBase{

    private List<Treasure> treasures;

    public TreasureManager3(TileMap map){
        treasures = new ArrayList<>();
        loadTreasuresFromMap(map);
    }

    public TreasureManager3() {
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
                        "🧪 Pointer Magic in Potion Lab\n\n" +
                                "🧩 Scenario: A wizard's apprentice is testing potions stored in memory containers. Each container's address is stored using pointers. The apprentice must modify potion potency using only pointers.\n\n" +
                                "🛠️ Task: Input the potency of `N` potions into an array. Then use a pointer to increase each potion’s potency by 10 and print the updated values using only pointer arithmetic.\n\n" +
                                "📥 Sample Input:\n" +
                                "3\n" +
                                "50 60 70\n\n" +
                                "📤 Sample Output:\n" +
                                "60 70 80\n",
                        new String[] { "int", "scanf", "printf", "for", "pointer", "*", "array", "pointer arithmetic" }
                );


            case "treasure3":
                return new Challenge(
                        "🏠 Dynamic Shelter Allocation\n\n" +
                                "🧩 Scenario: A village is hit by a storm and people need to be temporarily housed. The number of people varies, so memory must be allocated dynamically to store their ages.\n\n" +
                                "🛠️ Task: Input the number of people `N`, dynamically allocate memory for storing their ages, then print the average age using the allocated memory. Free the memory afterward.\n\n" +
                                "📥 Sample Input:\n" +
                                "4\n" +
                                "12 18 25 30\n\n" +
                                "📤 Sample Output:\n" +
                                "Average Age: 21.25\n",
                        new String[] { "int", "float", "malloc", "free", "scanf", "printf", "for" }
                );


            case "treasure4":
                return new Challenge(
                        "🧗 Recursive Stair Climber\n\n" +
                                "🧩 Scenario: A robot climbs a staircase where it can take either 1 or 2 steps at a time. You must calculate how many distinct ways it can reach the `N`-th step using recursion.\n\n" +
                                "🛠️ Task: Input a step number `N`. Using recursion, calculate and print the number of unique ways the robot can reach the top.\n\n" +
                                "📥 Sample Input:\n" +
                                "4\n\n" +
                                "📤 Sample Output:\n" +
                                "5\n",
                        new String[] { "int", "scanf", "printf", "recursion", "if", "return" }
                );


            case "treasure5":
                return new Challenge(
                        "🔄 Potion Swap with Pointers\n\n" +
                                "🧩 Scenario: Two magical potions are mixed up. You must write a function to swap their potencies using pointers.\n\n" +
                                "🛠️ Task: Input two integers, swap their values using a function with pointer arguments, and print the values after swapping.\n\n" +
                                "📥 Sample Input:\n" +
                                "30 50\n\n" +
                                "📤 Sample Output:\n" +
                                "After Swap: 50 30\n",
                        new String[] { "int", "function", "pointer", "scanf", "printf", "swap", "address-of" }
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
