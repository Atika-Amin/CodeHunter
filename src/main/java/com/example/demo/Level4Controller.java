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

public class Level4Controller {
    @FXML
    private ImageView prevButton, nextButton, musicIcon;

    @FXML
    private Button startQuizButton;
    @FXML
    private TextFlow slideTextFlow;
    @FXML
    private Group level1Group;

    private final String[] slides = {
            // Slide 1: Introduction to Pointers
            "What is a Pointer in C?\n\n" +
                    "A pointer is a variable that stores the memory address of another variable.\n\n" +
                    "Example 1:\n" +
                    "#include <stdio.h>\n" +
                    "int main() {\n" +
                    "    int a = 10;\n" +
                    "    int *p = &a;\n" +
                    "    printf(\"%d\", *p);\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- `*p` accesses the value at the address of `a`\n" +
                    "- `&a` gives the address of variable `a`\n" +
                    "- This prints: 10\n\n" +
                    "Example 2:\n" +
                    "#include <stdio.h>\n" +
                    "int main() {\n" +
                    "    char ch = 'C';\n" +
                    "    char *ptr = &ch;\n" +
                    "    printf(\"%c\", *ptr);  // prints C\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- Pointers can store addresses of other data types (here, `char` type)\n" +
                    "- `*ptr` accesses the value at the address of `ch`",

            // Slide 2: Pointer Declaration and Usage
            "Using Pointers in C\n\n" +
                    "You declare a pointer with an asterisk `*` and assign it using the address-of `&` operator.\n\n" +
                    "Example 1:\n" +
                    "int x = 5;\n" +
                    "int *ptr = &x;\n" +
                    "printf(\"%d\", *ptr);  // prints 5\n\n" +
                    "Explanation:\n" +
                    "- `ptr` stores the address of `x`\n" +
                    "- `*ptr` accesses the value at that address\n\n" +
                    "Example 2:\n" +
                    "#include <stdio.h>\n" +
                    "int main() {\n" +
                    "    int a = 10;\n" +
                    "    int *p = &a;\n" +
                    "    *p = 20;  // change value of a through pointer\n" +
                    "    printf(\"%d\", a);  // prints 20\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- You can modify the value of the variable directly through the pointer\n" +
                    "- `*p = 20` changes the value of `a` to 20",

            // Slide 3: Pointers and Arrays
            "Pointers and Arrays\n\n" +
                    "Array names act as pointers to the first element.\n\n" +
                    "Example 1:\n" +
                    "int arr[3] = {10, 20, 30};\n" +
                    "int *p = arr;\n" +
                    "printf(\"%d\", *(p+1));  // prints 20\n\n" +
                    "Explanation:\n" +
                    "- `arr` is equivalent to `&arr[0]`\n" +
                    "- `*(p+1)` accesses the second element: 20\n\n" +
                    "Example 2:\n" +
                    "#include <stdio.h>\n" +
                    "int main() {\n" +
                    "    int arr[3] = {10, 20, 30};\n" +
                    "    int *ptr = arr;\n" +
                    "    printf(\"%d\", *(ptr+2));  // prints 30\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- Array elements can be accessed by pointer arithmetic\n" +
                    "- `*(ptr+2)` accesses the third element of the array",

            // Slide 4: Dynamic Memory Allocation
            "Dynamic Memory with malloc\n\n" +
                    "`malloc` allocates memory at runtime from the heap.\n\n" +
                    "Example 1:\n" +
                    "#include <stdlib.h>\n" +
                    "int *p = (int *) malloc(sizeof(int) * 3);\n" +
                    "p[0] = 1; p[1] = 2; p[2] = 3;\n\n" +
                    "Explanation:\n" +
                    "- `malloc` allocates memory dynamically during program execution\n" +
                    "- You must free the allocated memory after use using `free(p)`\n\n" +
                    "Example 2:\n" +
                    "#include <stdio.h>\n" +
                    "#include <stdlib.h>\n" +
                    "int main() {\n" +
                    "    int *arr = (int *) malloc(4 * sizeof(int));\n" +
                    "    for(int i = 0; i < 4; i++) {\n" +
                    "        arr[i] = i + 1;\n" +
                    "    }\n" +
                    "    for(int i = 0; i < 4; i++) {\n" +
                    "        printf(\"%d \", arr[i]);  // prints 1 2 3 4\n" +
                    "    }\n" +
                    "    free(arr);\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- `malloc` dynamically allocates memory for the array\n" +
                    "- After use, we free the allocated memory using `free(arr)`",

            // Slide 5: Memory Management
            "Freeing Memory in C\n\n" +
                    "Use `free()` to release memory allocated with `malloc` or `calloc`.\n\n" +
                    "Example 1:\n" +
                    "int *arr = (int *) malloc(5 * sizeof(int));\n" +
                    "// use the array\n" +
                    "free(arr);\n\n" +
                    "Explanation:\n" +
                    "- `free()` prevents memory leaks\n" +
                    "- Always match each `malloc` with `free()`\n\n" +
                    "Example 2:\n" +
                    "#include <stdlib.h>\n" +
                    "int *p = (int *) malloc(sizeof(int) * 10);\n" +
                    "if (p != NULL) {\n" +
                    "    for (int i = 0; i < 10; i++) {\n" +
                    "        p[i] = i;\n" +
                    "    }\n" +
                    "    free(p);\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- Always check for `NULL` before using a dynamically allocated pointer\n" +
                    "- `free()` should only be called on a non-NULL pointer",

            // Slide 6: Null and Dangling Pointers
            "Null & Dangling Pointers\n\n" +
                    "- A **null pointer** points to nothing: `int *p = NULL;`\n" +
                    "- A **dangling pointer** points to freed memory.\n\n" +
                    "Avoiding Errors:\n" +
                    "1. Always set pointer to NULL after `free()`:\n" +
                    "   free(p); p = NULL;\n" +
                    "2. Never dereference a NULL or freed pointer!\n\n" +
                    "Example 1:\n" +
                    "#include <stdio.h>\n" +
                    "int main() {\n" +
                    "    int *p = (int *) malloc(sizeof(int));\n" +
                    "    *p = 5;\n" +
                    "    free(p);\n" +
                    "    p = NULL;  // Prevent dangling pointer\n" +
                    "    printf(\"Pointer after free: %p\", p);  // prints NULL\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- Setting the pointer to `NULL` after freeing prevents it from becoming a dangling pointer\n\n" +
                    "Example 2:\n" +
                    "int *p = NULL;\n" +
                    "if (p != NULL) {\n" +
                    "    // Safe to dereference p\n" +
                    "}\n" +
                    "Explanation:\n" +
                    "- Always check if the pointer is `NULL` before dereferencing to avoid segmentation faults!",

            // Slide 7: Introduction to Recursion
            "Introduction to Recursion\n\n" +
                    "Recursion is a technique where a function calls itself to solve a smaller version of the problem.\n\n" +
                    "Key Concepts:\n" +
                    "- Every recursive function must have a **base case** to stop recursion.\n" +
                    "- Each recursive call should bring the problem closer to the base case.\n\n" +
                    "Example 1: Factorial using Recursion\n" +
                    "#include <stdio.h>\n" +
                    "int factorial(int n) {\n" +
                    "    if (n == 0) return 1;  // base case\n" +
                    "    return n * factorial(n - 1);  // recursive call\n" +
                    "}\n" +
                    "int main() {\n" +
                    "    printf(\"%d\", factorial(5));  // prints 120\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- `factorial(5)` calls `factorial(4)`, then `factorial(3)`, ... until `factorial(0)`\n" +
                    "- The base case stops the recursion and the stack unwinds\n\n" +

                    "Example 2: Fibonacci Sequence\n" +
                    "#include <stdio.h>\n" +
                    "int fibonacci(int n) {\n" +
                    "    if (n == 0) return 0;\n" +
                    "    if (n == 1) return 1;\n" +
                    "    return fibonacci(n-1) + fibonacci(n-2);\n" +
                    "}\n" +
                    "int main() {\n" +
                    "    for(int i = 0; i < 6; i++) {\n" +
                    "        printf(\"%d \", fibonacci(i));  // prints 0 1 1 2 3 5\n" +
                    "    }\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- Each call splits into two smaller calls until base cases are reached\n" +
                    "- Recursive calls build up the Fibonacci sequence\n\n" +
                    "⚠️ Recursive functions may consume more memory (stack space), so deep recursion can cause stack overflow."


    };

    private int currentSlideIndex = 0;

    @FXML
    public void initialize() {
        updateSlide();
        applyZoomInAnimation(level1Group);
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
            Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/com/example/demo/quiz4.fxml"));
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
