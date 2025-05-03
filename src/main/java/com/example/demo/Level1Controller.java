package com.example.demo;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class Level1Controller {

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
    private Group level1Group;

    private final String[] slides = {
            "Introduction to C\n\nC is a foundational programming language known for its efficiency and control over system-level resources. It is widely used in operating systems, embedded systems, and more.\n\nExample:\n#include <stdio.h>\n\nint main() {\n    printf(\"Hello, Code Hunters!\\n\");\n    return 0;\n}\n\nExplanation:\n- `#include <stdio.h>`: Brings in the standard I/O library.\n- `int main()`: Main function where execution begins.\n- `printf()`: Displays text to the output screen.\n- `\\n`: Moves to the next line after printing.",

            "Variable Initialization in C\n\nVariables store data. You can initialize them when you declare them.\n\nExample:\n#include <stdio.h>\n\nint main() {\n    int score = 100;\n    float temperature = 36.5;\n    char grade = 'B';\n\n    printf(\"Score: %d, Temp: %.1f, Grade: %c\", score, temperature, grade);\n    return 0;\n}\n\nExplanation:\n- `int`, `float`, and `char` are data types.\n- Initialization uses `=` to assign values.\n- `%d`, `%.1f`, and `%c` are used for formatting output.",

            "Data Types in C\n\nC offers multiple data types based on the type of information you want to store.\n\nExample:\n#include <stdio.h>\n\nint main() {\n    int age = 21;\n    float height = 5.9f;\n    double balance = 12345.67;\n    char initial = 'H';\n\n    printf(\"Age: %d, Height: %.1f, Balance: %.2lf, Initial: %c\", age, height, balance, initial);\n    return 0;\n}\n\nExplanation:\n- `float` is used for small decimals, `double` for more precision.\n- `f` after a number ensures it's treated as a float.\n- `%lf` is used for `double`, not `float`.",

            "Operators in C\n\nOperators perform operations on variables and values. They're grouped into categories like arithmetic, relational, and assignment.\n\nExample:\n#include <stdio.h>\n\nint main() {\n    int x = 15, y = 4;\n\n    printf(\"Subtraction: %d\\n\", x - y);\n    printf(\"Multiplication: %d\\n\", x * y);\n    printf(\"Is x greater than y? %d\", x > y);\n    return 0;\n}\n\nExplanation:\n- `-`, `*` are arithmetic operators.\n- `>` is a relational operator that returns `1` (true) or `0` (false).\n- C evaluates expressions left to right.",

            "Input in C\n\nUse `scanf()` to get data from the user. Always use `&` before variable names (except for strings).\n\nExample:\n#include <stdio.h>\n\nint main() {\n    float radius;\n\n    printf(\"Enter radius: \");\n    scanf(\"%f\", &radius);\n    printf(\"You entered: %.2f\", radius);\n    return 0;\n}\n\nExplanation:\n- `%f`: Format specifier for floats.\n- `&radius`: Passes the memory address to store the input value.",

            "Output in C\n\nUse `printf()` to show results, combining text and variables using format specifiers.\n\nExample:\n#include <stdio.h>\n\nint main() {\n    char city[20] = \"Dhaka\";\n    int population = 21000000;\n\n    printf(\"City: %s\\n\", city);\n    printf(\"Population: %d\", population);\n    return 0;\n}\n\nExplanation:\n- `%s`: Used for strings.\n- `%d`: Used for integers.\n- `\\n`: Adds a line break in the output."
    };



    private int currentSlideIndex = 0;
    @FXML
    public void initialize() {
        updateSlide();
        applyZoomInAnimation(level1Group);
        // Update icon initially based on music state
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
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/quiz1.fxml"));
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
    // Optional: Implement goBack for home icon click
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
