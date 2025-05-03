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

public class Level2Controller {
    @FXML
    private ImageView prevButton, nextButton, musicIcon;

    @FXML
    private Button startQuizButton;
    @FXML
    private TextFlow slideTextFlow;
    @FXML
    private Group level1Group;

    private final String[] slides = {
            // Slide 1: if-else
            "🧠 if-else in C\n\n" +
                    "Control flow allows your program to make decisions.\n" +
                    "The `if-else` statement lets you choose between two actions based on a condition.\n\n" +
                    "Example:\n" +
                    "#include <stdio.h>\n" +
                    "int main() {\n" +
                    "    int age = 20;\n" +
                    "    if(age >= 18) {\n" +
                    "        printf(\"Adult\\n\");\n" +
                    "    } else {\n" +
                    "        printf(\"Minor\\n\");\n" +
                    "    }\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- Checks if `age` is 18 or more.\n" +
                    "- If true, it prints \"Adult\".\n" +
                    "- If false, it prints \"Minor\".\n" +
                    "- `if` runs only if condition is true. `else` runs if it's false.",

            // Slide 2: Nested if
            "🔍 Nested if in C\n\n" +
                    "A nested `if` means an `if` inside another `if`. Useful when checking multiple levels.\n\n" +
                    "Example:\n" +
                    "#include <stdio.h>\n" +
                    "int main() {\n" +
                    "    int marks = 85;\n" +
                    "    if(marks >= 50) {\n" +
                    "        if(marks >= 80) {\n" +
                    "            printf(\"Excellent\\n\");\n" +
                    "        } else {\n" +
                    "            printf(\"Passed\\n\");\n" +
                    "        }\n" +
                    "    } else {\n" +
                    "        printf(\"Failed\\n\");\n" +
                    "    }\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- First it checks if `marks >= 50`.\n" +
                    "- If yes, then checks if `marks >= 80`.\n" +
                    "- If both true → \"Excellent\".\n" +
                    "- If only first is true → \"Passed\".\n" +
                    "- If first is false → \"Failed\".",

            // Slide 3: switch-case
            " Switch-case in C\n\n" +
                    "`switch` is used to perform different actions based on the value of a variable.\n\n" +
                    "Example:\n" +
                    "#include <stdio.h>\n" +
                    "int main() {\n" +
                    "    int day = 3;\n" +
                    "    switch(day) {\n" +
                    "        case 1: printf(\"Monday\\n\"); break;\n" +
                    "        case 2: printf(\"Tuesday\\n\"); break;\n" +
                    "        case 3: printf(\"Wednesday\\n\"); break;\n" +
                    "        default: printf(\"Invalid day\\n\");\n" +
                    "    }\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- `switch(day)` checks the value of `day`.\n" +
                    "- Matches with `case 3`, prints \"Wednesday\".\n" +
                    "- `break` stops checking further cases.\n" +
                    "- If no match, `default` runs.",

            // Slide 4: for loop
            "for loop in C\n\n" +
                    "`for` loops are used when you know how many times to repeat.\n\n" +
                    "Example:\n" +
                    "#include <stdio.h>\n" +
                    "int main() {\n" +
                    "    for(int i = 1; i <= 5; i++) {\n" +
                    "        printf(\"%d \", i);\n" +
                    "    }\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- `i = 1` → start\n" +
                    "- `i <= 5` → run while this is true\n" +
                    "- `i++` → increase by 1 every loop\n" +
                    "- Prints: 1 2 3 4 5",

            // Slide 5: while loop
            " while loop in C\n\n" +
                    "`while` loop checks the condition **before** each run.\n\n" +
                    "Example:\n" +
                    "#include <stdio.h>\n" +
                    "int main() {\n" +
                    "    int i = 1;\n" +
                    "    while(i <= 5) {\n" +
                    "        printf(\"%d \", i);\n" +
                    "        i++;\n" +
                    "    }\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- `i` starts at 1\n" +
                    "- Loop runs as long as `i <= 5`\n" +
                    "- After printing, `i` is increased\n" +
                    "- Output: 1 2 3 4 5",

            // Slide 6: do-while loop
            " do-while loop in C\n\n" +
                    "`do-while` runs the code first, then checks the condition.\n\n" +
                    "Example:\n" +
                    "#include <stdio.h>\n" +
                    "int main() {\n" +
                    "    int i = 1;\n" +
                    "    do {\n" +
                    "        printf(\"%d \", i);\n" +
                    "        i++;\n" +
                    "    } while(i <= 5);\n" +
                    "    return 0;\n" +
                    "}\n\n" +
                    "Explanation:\n" +
                    "- Runs at least once no matter what\n" +
                    "- After running, checks if `i <= 5`\n" +
                    "- If true, repeats\n" +
                    "- Output: 1 2 3 4 5"
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
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/quiz2.fxml"));
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
