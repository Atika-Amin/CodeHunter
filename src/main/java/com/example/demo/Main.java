package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Font.loadFont(getClass().getResourceAsStream("/com/example/demo/fonts/PressStart2P-Regular.ttf"), 12);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/login.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 960, 640));
        primaryStage.setTitle("Login Screen");
        MusicManager.startMusic();
        primaryStage.show();
        //System.out.println(Font.getFamilies());
    }

    public static void main(String[] args) {
        launch(args);
    }
}