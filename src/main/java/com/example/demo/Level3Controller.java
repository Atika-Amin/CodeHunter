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

public class Level3Controller {

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
            "Arrays in C\n\nArrays are used to store multiple values of the same type in a single variable.\n\nExample:\n#include <stdio.h>\n\nint main() {\n    int numbers[5] = {1, 2, 3, 4, 5};\n    for(int i = 0; i < 5; i++) {\n        printf(\"%d\\n\", numbers[i]);\n    }\n    return 0;\n}\n\nExplanation:\n- `int numbers[5]`: Declares an array of 5 integers.\n- The `for` loop is used to iterate through the array and print each element.\n- Arrays allow you to work with multiple values in a single variable.",

            "Multi-Dimensional Arrays in C\n\nArrays can be multi-dimensional, such as matrices (2D arrays).\n\nExample:\n#include <stdio.h>\n\nint main() {\n    int matrix[2][3] = {{1, 2, 3}, {4, 5, 6}};\n    for(int i = 0; i < 2; i++) {\n        for(int j = 0; j < 3; j++) {\n            printf(\"%d \", matrix[i][j]);\n        }\n        printf(\"\\n\");\n    }\n    return 0;\n}\n\nExplanation:\n- `int matrix[2][3]`: Declares a 2x3 matrix.\n- The nested `for` loops are used to iterate through the 2D array.",

            "Strings in C\n\nStrings in C are arrays of characters, terminated by a null character (`\\0`).\n\nExample:\n#include <stdio.h>\n\nint main() {\n    char str[] = \"Hello, Code Hunter!\";\n    printf(\"%s\\n\", str);\n    return 0;\n}\n\nExplanation:\n- `char str[]`: Declares a string array.\n- `%s`: Format specifier used to print strings.\n- The string ends with a `\\0` to mark the end of the string.",

            "String Manipulation in C\n\nYou can perform various operations on strings, such as concatenation and comparison.\n\nExample:\n#include <stdio.h>\n#include <string.h>\n\nint main() {\n    char str1[20] = \"Hello, \";\n    char str2[] = \"Code Hunter!\";\n    strcat(str1, str2); // Concatenate str2 to str1\n    printf(\"%s\\n\", str1);\n    return 0;\n}\n\nExplanation:\n- `strcat()`: Concatenates `str2` to `str1`.\n- The `strcat()` function is used to append one string to another.",

            "String Length in C\n\nYou can find the length of a string using the `strlen()` function.\n\nExample:\n#include <stdio.h>\n#include <string.h>\n\nint main() {\n    char str[] = \"Code Hunter!\";\n    int length = strlen(str); // Get length of the string\n    printf(\"Length of string: %d\\n\", length);\n    return 0;\n}\n\nExplanation:\n- `strlen()`: Returns the length of the string, excluding the null character.",

            "Array of Strings in C\n\nYou can create arrays of strings to store multiple strings.\n\nExample:\n#include <stdio.h>\n\nint main() {\n    char *names[] = {\"Alice\", \"Bob\", \"Charlie\"};\n    for(int i = 0; i < 3; i++) {\n        printf(\"%s\\n\", names[i]);\n    }\n    return 0;\n}\n\nExplanation:\n- `char *names[]`: Declares an array of string pointers.\n- Each element of the array points to a string.",

            "Function Arrays in C\n\nYou can even pass arrays to functions to operate on them.\n\nExample:\n#include <stdio.h>\n\nvoid printArray(int arr[], int size) {\n    for(int i = 0; i < size; i++) {\n        printf(\"%d\\n\", arr[i]);\n    }\n}\n\nint main() {\n    int arr[3] = {1, 2, 3};\n    printArray(arr, 3); // Function call with array as parameter\n    return 0;\n}\n\nExplanation:\n- `int arr[]`: An array passed to the `printArray()` function.\n- The function prints each element of the array.",

            "Functions in C\n\nFunctions allow you to group a set of instructions together to perform a specific task.\n\nExample:\n#include <stdio.h>\n\nvoid greet() {\n    printf(\"Hello, Code Hunter!\\n\");\n}\n\nint main() {\n    greet(); // Function call\n    return 0;\n}\n\nExplanation:\n- `void greet()`: Function definition with no return value.\n- `greet();`: Function call in the `main()` function.\n- Functions help in code reuse and organization.",

            "Functions with Parameters\n\nFunctions can also take parameters (inputs) that are used within the function.\n\nExample:\n#include <stdio.h>\n\nvoid greetWithName(char name[]) {\n    printf(\"Hello, %s!\\n\", name);\n}\n\nint main() {\n    char name[] = \"Alice\";\n    greetWithName(name); // Function call with parameter\n    return 0;\n}\n\nExplanation:\n- `char name[]`: An array of characters passed as a parameter.\n- The function `greetWithName()` prints a personalized greeting using the `name` parameter.",

            "Functions with Return Values\n\nFunctions can return values. The return type must be specified.\n\nExample:\n#include <stdio.h>\n\nint add(int a, int b) {\n    return a + b;\n}\n\nint main() {\n    int result = add(5, 3); // Function call with return value\n    printf(\"Sum: %d\\n\", result);\n    return 0;\n}\n\nExplanation:\n- `int add(int a, int b)`: Function that takes two integers and returns their sum.\n- `return a + b;`: Returns the result of the addition.",

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
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/quiz3.fxml"));
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
