package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class TreasureManager2 implements TreasureManagerBase{
    private List<Treasure> treasures;

    public TreasureManager2(TileMap map){
        treasures = new ArrayList<>();
        loadTreasuresFromMap(map);
    }

    public TreasureManager2() {
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
                        "🏪 Grocery Price Calculator\n\n" +
                                "🧩 Scenario: A shopkeeper stores the prices of 5 grocery items. You need to calculate the total and average price using a function.\n\n" +
                                "🛠️ Task:\n" +
                                "- Declare an array of float to store 5 prices.\n" +
                                "- Write a function `float calculateTotal(float arr[], int size)` that returns the total.\n" +
                                "- In `main()`, call the function, calculate the average, and print both.\n\n" +
                                "📥 Sample Input:\n" +
                                "Prices: 20.5 15.0 40.25 10.0 30.0\n\n" +
                                "📤 Sample Output:\n" +
                                "Prices: 20.5 15.0 40.25 10.0 30.0\n" +
                                "Total: 115.75\n" +
                                "Average: 23.15\n",
                        new String[] { "float", "array", "function", "return", "for", "printf", "%.2f" }
                );


            case "treasure3":
                return new Challenge(
                        "🧑‍🎓 Name Length Analyzer\n\n" +
                                "🧩 Scenario: You are storing names of 3 students. Write a program that displays each name and its length using a separate function.\n\n" +
                                "🛠️ Task:\n" +
                                "- Use a 2D char array to store 3 names.\n" +
                                "- Write a function `int getLength(char name[])` that calculates string length (without using `strlen`).\n" +
                                "- Display: `Name: Ali, Length: 3`\n\n" +
                                "📥 Sample Input:\n" +
                                "Names: Ali Sarah Zayn\n\n" +
                                "📤 Sample Output:\n" +
                                "Name: Ali, Length: 3\n" +
                                "Name: Sarah, Length: 5\n" +
                                "Name: Zayn, Length: 4\n",
                        new String[] { "char", "array", "function", "for", "return", "printf", "%s", "%d" }
                );


            case "treasure4":
                return new Challenge(
                        "🎶 Favorite Songs Reverser\n\n" +
                                "🧩 Scenario: You store the names of 4 favorite songs. Create a program that displays the song list in reverse order using a function.\n\n" +
                                "🛠️ Task:\n" +
                                "- Declare a 2D char array for 4 song names.\n" +
                                "- Write a function `void displayReverse(char songs[][30], int size)` that prints them in reverse.\n" +
                                "- Call the function from `main()`.\n\n" +
                                "📥 Sample Input:\n" +
                                "Songs: Perfect Levitating Shape of You Blinding Lights\n\n" +
                                "📤 Sample Output:\n" +
                                "Song 4: Blinding Lights\n" +
                                "Song 3: Shape of You\n" +
                                "Song 2: Levitating\n" +
                                "Song 1: Perfect\n",
                        new String[] { "char", "array", "function", "for", "printf", "%s" }
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
