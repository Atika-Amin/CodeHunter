package com.example.demo;

import java.util.Arrays;
import java.util.List;

public class DialogueManager {

    public List<String> getIntroDialogue(String mapName) {
        String username = Session.getUsername();  // Get from login session

        switch (mapName.toLowerCase()) {
            case "map1":
                return Arrays.asList(
                        "🌲 Welcome to the Forest, " + username + "!\n" +
                                "I am the guardian spirit of this woodland.\n" +
                                "Here, you’ll learn the basics of programming.\n" +
                                "Need help? I’ve got hints to guide your path!\n"
                );
            case "map2":
                return Arrays.asList(
                        "🏜️ Welcome to the Desert, " + username + "!\n" +
                                "This land tests your logic and memory.\n" +
                                "Careful — challenges are harder here.\n" +
                                "Ask for a hint if you're stuck.\n"
                );
            case "map3":
                return Arrays.asList(
                        "🌋 Welcome to the Volcano, " + username + "!\n" +
                                "Only brave coders reach this far.\n" +
                                "This level contains intense pointer puzzles.\n" +
                                "Need help? Say 'hint' or 'help'.\n"
                );
            default:
                return Arrays.asList(
                        "👋 Welcome, " + username + "!\n" +
                                "I'm your guide through this mysterious realm.\n" +
                                "Let me know if you need a hint or help.\n"
                );
        }
    }


    public static List<String> getResponseBasedOnInput(String input, String mapName) {
        input = input.toLowerCase();

        if (input.contains("hint") || input.contains("yes") || input.contains("help")) {
            return getAllChallengeHints(mapName);
        } else if (input.contains("exit") || input.contains("bye") || input.contains("thank you")) {
            return Arrays.asList(
                    "See you soon!\n" +
                            "Don't forget to save your progress.\n" +
                            "We hope you learned something new.\n" +
                            "Come back anytime, adventurer!\n"
            );
        } else {
            return Arrays.asList("I'm not sure how to respond to that.", "Try asking something else.");
        }
    }


    private static List<String> getAllChallengeHints(String mapName) {
        switch (mapName.toLowerCase()) {
            case "map1":
                return Arrays.asList(
                        "📦 Hint for Product Bill Generator:\n" +
                                "- Use `char[]` or `string` for product name.\n" +
                                "- Use `scanf` for inputs and `printf` for outputs.\n" +
                                "- Multiply `price * quantity` and store in a float.\n" +
                                "- Example format: `Total Cost: $%.2f`\n",

                        "📚 Hint for GPA Percentage Calculator:\n" +
                                "- Declare name (`char[]`), ID (`int`), GPA (`float`).\n" +
                                "- Use `percentage = GPA * 20`.\n" +
                                "- Output all fields using `printf`, format GPA and percentage to 2 decimal places.\n",

                        "📐 Hint for Circle Area Calculator:\n" +
                                "- Get radius using `scanf` (as float).\n" +
                                "- Use formula `area = 3.14 * radius * radius`.\n" +
                                "- Print with two decimal precision using `%.2f`.\n"
                );

            case "map2":
                return Arrays.asList(
                        // 💡 Hint for treasure2
                        "🏕️ Hint for Village Festival Access Checker:\n" +
                                "- Use `int` for people count and ages.\n" +
                                "- Read 4 ints for zones: Kids, Teen, Adult, Senior.\n" +
                                "- Loop ages, check ranges with `if-else`.\n" +
                                "- Print zone if available, else `Not Allowed`.\n",

                        // 💡 Hint for treasure3
                        "📅 Hint for Weekly Schedule Optimizer:\n" +
                                "- Use `switch` for day (1–7).\n" +
                                "- Map days to activities.\n" +
                                "- Use `break` after cases.\n" +
                                "- Use `default` for invalid input.\n" +
                                "- Input day count, then loop to read days.\n",

                        // 💡 Hint for treasure4
                        "➕ Hint for Sum of Multiples:\n" +
                                "- Read `int N`.\n" +
                                "- Loop 1 to N.\n" +
                                "- Check `% 3 == 0 || % 5 == 0`.\n" +
                                "- Add matching to sum.\n" +
                                "- Print sum.\n"
                );



            case "map3":
                return Arrays.asList(
                        // 💡 Hint for treasure2 - Grocery Price Calculator
                        "🏪 Hint for Grocery Price Calculator:\n" +
                                "- Use a `float` array for 5 prices.\n" +
                                "- Write `float calculateTotal(float arr[], int size)`.\n" +
                                "- Sum values with a `for` loop.\n" +
                                "- Print prices, call function, compute average.\n" +
                                "- Use `%.2f` for formatting.\n",

                        // 💡 Hint for treasure3 - Name Length Analyzer
                        "🧑‍🎓 Hint for Name Length Analyzer:\n" +
                                "- Use `char names[3][30]`.\n" +
                                "- Write `int getLength(char name[])` counting till `\\0`.\n" +
                                "- Loop names, call length function.\n" +
                                "- Print with `printf(\"Name: %s, Length: %d\\n\", ...)`.\n" +
                                "- Don’t use `strlen()`.\n",

                        // 💡 Hint for treasure4 - Favorite Songs Reverser
                        "🎶 Hint for Favorite Songs Reverser:\n" +
                                "- Use `char songs[4][30]`.\n" +
                                "- Write `void displayReverse(char songs[][30], int size)`.\n" +
                                "- Loop backward from `size-1` to 0.\n" +
                                "- Print each song.\n" +
                                "- Call function after input.\n"
                );

            case "map4":
                return Arrays.asList(
                        // 💡 Hint for treasure1 - Pointer Magic in Potion Lab
                        "🧪 Hint for Pointer Magic in Potion Lab:\n" +
                                "- Declare an `int` array.\n" +
                                "- Use pointer: `int *ptr = arr;`\n" +
                                "- Traverse with pointer arithmetic.\n" +
                                "- Use `scanf` and `printf`.\n" +
                                "- Avoid `arr[i]`, use pointer dereference.\n",

                        // 💡 Hint for treasure3 - Dynamic Shelter Allocation
                        "🏠 Hint for Dynamic Shelter Allocation:\n" +
                                "- Use `int *ages = malloc(N * sizeof(int));`\n" +
                                "- Input ages with `scanf`.\n" +
                                "- Calculate average and print.\n" +
                                "- Free memory with `free(ages);`\n" +
                                "- Use `%.2f` in `printf`.\n",

                        // 💡 Hint for treasure4 - Recursive Stair Climber
                        "🧗 Hint for Recursive Stair Climber:\n" +
                                "- Write recursive `int countWays(int n)`.\n" +
                                "- Base: `if(n==0||n==1) return 1;`\n" +
                                "- Recursive: sum of last two calls.\n" +
                                "- Use `scanf` and `printf`.\n" +
                                "- Watch for large `n` stack overflow.\n",

                        // 💡 Hint for treasure5 - Potion Swap with Pointers
                        "🔄 Hint for Potion Swap with Pointers:\n" +
                                "- Write `void swap(int *a, int *b)`.\n" +
                                "- Swap using temp variable.\n" +
                                "- Pass addresses with `&x, &y`.\n" +
                                "- Use `scanf` and `printf`.\n" +
                                "- Shows call-by-address.\n"
                );

            case "map5":
                return Arrays.asList(
                        // 💡 Hint for treasure2 - Mission Log Recorder
                        "🧑‍🚀 Hint for Mission Log Recorder:\n" +
                                "- Define `struct Log` with date and status.\n" +
                                "- Use `fopen(\"mission.txt\", \"w\")`.\n" +
                                "- Loop 3 times, get input with `scanf`.\n" +
                                "- Write using `fprintf`, then `fclose`.",

                        // 💡 Hint for treasure3 - Student File Reader
                        "📖 Hint for Student File Reader:\n" +
                                "- Open file with `fopen` in read mode.\n" +
                                "- Use `fscanf` or `fgets` + `sscanf`.\n" +
                                "- Loop till EOF, print with `printf`.\n" +
                                "- Close file with `fclose`.",

                        // 💡 Hint for treasure4 - DNA Memory Expansion
                        "🧬 Hint for DNA Memory Expansion:\n" +
                                "- Use `malloc` for 3 ints, store values.\n" +
                                "- Use `realloc` to expand to 5.\n" +
                                "- Add more values, print in loop.\n" +
                                "- Free memory at the end.",

                        // 💡 Hint for treasure5 - Student Record Management
                        "📚 Hint for Student Record System:\n" +
                                "- Use `struct`, `malloc`, and functions.\n" +
                                "- Input data, write to file with `fprintf`.\n" +
                                "- Read back using `fscanf`, print.\n" +
                                "- Use `free()` to release memory.",

                        // 💡 Hint for treasure6 - Function Pointer Spellbook
                        "🧙‍♂️ Hint for Function Pointer Spellbook:\n" +
                                "- Create two `void` functions.\n" +
                                "- Use `void (*ptr)()` for pointer.\n" +
                                "- Assign based on input, then call.\n" +
                                "- Use `scanf` and `ptr();`."
                );

            default:
                return Arrays.asList("🤔 No specific hints for this map yet.");

        }
    }
}
