package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class TreasureManager4 implements TreasureManagerBase{

    private List<Treasure> treasures;
    private final int id = 5;

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
                        "🧑‍🚀 Mission Log Recorder\n\n" +
                                "🧩 Scenario: An astronaut logs daily data during a space mission, including the date and status report. These entries must be saved in a log file.\n\n" +
                                "🛠️ Task:\n" +
                                "- Define a `struct Log` with `char date[20]` and `char status[100]`.\n" +
                                "- Ask the user for 3 log entries.\n" +
                                "- Write the logs to a file named `mission.txt` using `fprintf`.\n\n" +
                                "📥 Sample Input:\n" +
                                "2025-06-01 Safe\n" +
                                "2025-06-02 Storm Detected\n" +
                                "2025-06-03 Engine Stable\n\n" +
                                "📤 Sample Output (in mission.txt):\n" +
                                "Date: 2025-06-01, Status: Safe\n" +
                                "Date: 2025-06-02, Status: Storm Detected\n" +
                                "Date: 2025-06-03, Status: Engine Stable\n",
                        new String[] { "struct", "char", "fopen", "fprintf", "fclose" }
                );

            case "treasure3":
                return new Challenge(
                        "📖 Student File Reader\n\n" +
                                "🧩 Scenario: A teacher has saved student grades in a file called `grades.txt`. You need to read and display the data for review.\n\n" +
                                "🛠️ Task:\n" +
                                "- Define a structure `Student` with `char name[50]` and `int grade`.\n" +
                                "- Read all lines from `grades.txt` using `fscanf` or `fgets`.\n" +
                                "- Display each student's data.\n\n" +
                                "📤 Sample Output:\n" +
                                "Name: Alice, Grade: 85\n" +
                                "Name: Bob, Grade: 90\n",
                        new String[] { "struct", "char", "int", "fopen", "fscanf", "fclose", "printf" }
                );

            case "treasure4":
                return new Challenge(
                        "🧬 DNA Memory Expansion\n\n" +
                                "🧩 Scenario: A bio-lab stores DNA sequence fragments. Initially, 3 samples are stored, but more arrive later requiring memory expansion.\n\n" +
                                "🛠️ Task:\n" +
                                "- Use `malloc` to allocate memory for 3 integers.\n" +
                                "- Store values: 101, 102, 103.\n" +
                                "- Use `realloc` to resize the array to hold 5 values and store: 104, 105.\n" +
                                "- Print all 5 values.\n\n" +
                                "📤 Sample Output:\n" +
                                "DNA Samples: 101 102 103 104 105\n",
                        new String[] { "int", "malloc", "realloc", "free", "printf" }
                );

            case "treasure5":
                return new Challenge(
                        "📚 Student Record Management System\n\n" +
                                "🧩 Scenario: You are building a mini file-based student record management system for a school. It should allow entering, saving, and displaying student records using C programming concepts.\n\n" +
                                "🛠️ Task:\n" +
                                "- Define a `struct Student` with `char name[50]`, `int roll`, `float marks`.\n" +
                                "- Dynamically allocate memory for `N` students.\n" +
                                "- Write a function `void inputStudents(struct Student *s, int n)` to take input.\n" +
                                "- Write a function `void saveToFile(struct Student *s, int n)` to write all records to `students.txt` using `fprintf()`.\n" +
                                "- Write a function `void displayFromFile()` to read and display data from `students.txt`.\n" +
                                "- Use `malloc`, file handling, loops, and function pointers (optional) to manage the operations.\n" +
                                "- Free all dynamically allocated memory.\n\n" +
                                "📥 Sample Input:\n" +
                                "Enter number of students: 2\n" +
                                "Name: Ali, Roll: 101, Marks: 87.5\n" +
                                "Name: Sara, Roll: 102, Marks: 91.0\n\n" +
                                "📤 Sample Output (from file):\n" +
                                "Name: Ali, Roll: 101, Marks: 87.50\n" +
                                "Name: Sara, Roll: 102, Marks: 91.00\n",
                        new String[] { "struct", "char", "int", "float", "malloc", "free", "fopen", "fprintf", "fscanf", "fclose", "printf", "function", "pointer", "array", "for" }
                );


            case "treasure6":
                return new Challenge(
                        "🧙‍♂️ Function Pointer Spellbook\n\n" +
                                "🧩 Scenario: In a magical code lab, spells are functions. You must store spell functions in pointers and invoke them dynamically.\n\n" +
                                "🛠️ Task:\n" +
                                "- Create two functions: `fireSpell()` and `iceSpell()` that print messages.\n" +
                                "- Declare a function pointer and assign one of the spells based on user input.\n" +
                                "- Call the selected function using the pointer.\n\n" +
                                "📥 Sample Input:\n" +
                                "Choose spell (1 = Fire, 2 = Ice): 2\n\n" +
                                "📤 Sample Output:\n" +
                                "Casting Ice Spell... ❄️\n",
                        new String[] { "void", "function", "pointer", "scanf", "printf" }
                );
        }

        return null;

    }
    public int getRemainingTreasureCount() {
        int count = 2;
        for (Treasure treasure : treasures) {
            if (treasure.isSolved()) {
                count--;
            }
        }
        return count;
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
