package com.example.demo;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Level5Controller {
    @FXML
    private ImageView prevButton;
    @FXML private ImageView musicIcon;
    @FXML
    private ImageView nextButton;

    @FXML
    private Button startQuizButton;
    @FXML
    private TextFlow slideTextFlow;
    @FXML
    private Group level3Group;

    private final String[] slides = {
            // Slide 1: Introduction to Structures
            " What is a Structure in C?\n\n" +
                    "A structure is a user-defined data type that allows grouping of variables of different types under a single name.\n\n" +
                    "Example:\n" +
                    "#include <stdio.h>\n" +
                    "struct Person {\n" +
                    "    char name[50];\n" +
                    "    int age;\n" +
                    "};\n" +
                    "int main() {\n" +
                    "    struct Person p1 = {\"John\", 25};\n" +
                    "    printf(\"Name: %s, Age: %d\", p1.name, p1.age);\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- `struct Person` defines a structure.\n" +
                    "- It stores the name and age of a person as different types.\n" +
                    "- `p1.name` accesses the name, and `p1.age` accesses the age.",

            // Slide 2: File Handling in C
            // Slide 2: Writing to a File in C
            " File Handling in C: Writing to a File\n\n" +
                    "In C, file handling allows reading from and writing to files.\n\n" +
                    "Example (Writing to a file):\n" +
                    "#include <stdio.h>\n" +
                    "int main() {\n" +
                    "    FILE *file = fopen(\"example.txt\", \"w\");  // Open the file in write mode\n" +
                    "    if (file != NULL) {\n" +
                    "        fprintf(file, \"Hello, File!\\n\");  // Write text into the file\n" +
                    "        fprintf(file, \"This is a second line.\\n\");  // Writing more lines\n" +
                    "        fclose(file);  // Close the file\n" +
                    "    }\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- `fopen(\"example.txt\", \"w\")` opens the file `example.txt` in write mode.\n" +
                    "- If the file doesn't exist, it will be created; if it exists, it will be overwritten.\n" +
                    "- `fprintf(file, \"Hello, File!\")` writes a string to the file.\n" +
                    "- `fclose(file)` closes the file after writing.",


            // Slide 3: Reading from a File
            " Reading from a File in C\n\n" +
                    "You can read from a file using functions like `fscanf` or `fgets`.\n\n" +
                    "Example:\n" +
                    "#include <stdio.h>\n" +
                    "int main() {\n" +
                    "    FILE *file = fopen(\"example.txt\", \"r\");\n" +
                    "    if (file != NULL) {\n" +
                    "        char buffer[100];\n" +
                    "        fgets(buffer, 100, file);\n" +
                    "        printf(\"%s\", buffer);\n" +
                    "        fclose(file);\n" +
                    "    }\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- `fgets(buffer, 100, file)` reads a line from the file into `buffer`.\n" +
                    "- This allows reading data from a file line by line.",

            // Slide 4: Advanced Topics - Pointers to Functions
            "  Advanced Topics - Pointers to Functions\n\n" +
                    "You can use pointers to functions to call functions dynamically.\n\n" +
                    "Example:\n" +
                    "#include <stdio.h>\n" +
                    "void greet() {\n" +
                    "    printf(\"Hello, World!\\n\");\n" +
                    "}\n" +
                    "int main() {\n" +
                    "    void (*func_ptr)() = greet;\n" +
                    "    func_ptr();  // Calls greet()\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- `void (*func_ptr)()` is a pointer to the `greet` function.\n" +
                    "- `func_ptr()` calls the `greet` function using the pointer.",

            // Slide 5: Advanced Topics - Dynamic Memory Reallocation
            " Advanced Topics - Dynamic Memory Reallocation\n\n" +
                    "The `realloc` function is used to change the size of dynamically allocated memory.\n\n" +
                    "Example:\n" +
                    "#include <stdio.h>\n" +
                    "#include <stdlib.h>\n" +
                    "int main() {\n" +
                    "    int *arr = (int *)malloc(3 * sizeof(int));\n" +
                    "    arr[0] = 1; arr[1] = 2; arr[2] = 3;\n" +
                    "    arr = (int *)realloc(arr, 5 * sizeof(int));\n" +
                    "    arr[3] = 4; arr[4] = 5;\n" +
                    "    for (int i = 0; i < 5; i++) {\n" +
                    "        printf(\"%d \", arr[i]);\n" +
                    "    }\n" +
                    "    free(arr);\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- `realloc` changes the size of the previously allocated memory.\n" +
                    "- It returns a new pointer to the resized memory block.",

            // Slide 6: Advanced Topics - Bitwise Operators
            " Bitwise Operators in C\n\n" +
                    "Bitwise operators operate on the binary representations of integers.\n\n" +
                    "Example:\n" +
                    "#include <stdio.h>\n" +
                    "int main() {\n" +
                    "    int x = 5, y = 3;\n" +
                    "    printf(\"x & y = %d\\n\", x & y);  // Bitwise AND\n" +
                    "    printf(\"x | y = %d\\n\", x | y);  // Bitwise OR\n" +
                    "    printf(\"x ^ y = %d\\n\", x ^ y);  // Bitwise XOR\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- `x & y` performs bitwise AND, `x | y` performs bitwise OR, and `x ^ y` performs bitwise XOR.\n" +
                    "- These operators are useful in low-level programming and optimizing code."
    };



    private int currentSlideIndex = 0;

    @FXML
    public void initialize() {
        updateSlide();
        applyZoomInAnimation(level3Group);
        updateMusicIcon();
    }

    private void showSlide(int index) {
        slideTextFlow.getChildren().clear(); // Clear previous content

        String slide = slides[index];
        String[] lines = slide.split("\n", 2); // Split into title and rest

        Text title = new Text(lines[0] + "\n");
        title.setFont(Font.font("System", FontWeight.BOLD, 16));

        Text body = new Text(lines.length > 1 ? lines[1] : "");
        body.setFont(Font.font("System", FontWeight.NORMAL, 14));

        slideTextFlow.getChildren().addAll(title, body);
    }

    private void updateSlide() {
        showSlide(currentSlideIndex);
        prevButton.setVisible(currentSlideIndex > 0);
        nextButton.setVisible(currentSlideIndex < slides.length - 1);
        startQuizButton.setVisible(currentSlideIndex == slides.length - 1);
    }

    @FXML
    private void goToNextSlide() {
        SoundUtil.playClick();
        if (currentSlideIndex < slides.length - 1) {
            currentSlideIndex++;
            updateSlide();
        }
    }

    @FXML
    private void goToPreviousSlide() {
        SoundUtil.playClick();
        if (currentSlideIndex > 0) {
            currentSlideIndex--;
            updateSlide();
        }
    }

    @FXML
    private void startQuiz() {
        SoundUtil.playClick();
        try {
            Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/com/example/demo/quiz5.fxml"));
            Stage stage = (Stage) slideTextFlow.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void applyZoomInAnimation(Group targetGroup) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1.2), targetGroup);
        scaleTransition.setFromX(0.8);
        scaleTransition.setFromY(0.8);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
    }

    @FXML
    private void toggleMusic(MouseEvent event) {
        SoundUtil.playClick();
        MusicManager.toggleMusic();
        updateMusicIcon();
    }

    private void updateMusicIcon() {
        String iconPath = MusicManager.isMusicPlaying()
                ? "/com/example/demo/Frames/music_on.png"
                : "/com/example/demo/Frames/music_off.png";
        musicIcon.setImage(new Image(getClass().getResourceAsStream(iconPath)));
    }

    @FXML
    private void goBack() {
        SoundUtil.playClick();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
            Stage stage = (Stage) slideTextFlow.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
