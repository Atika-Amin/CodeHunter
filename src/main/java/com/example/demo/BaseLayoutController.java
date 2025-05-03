package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;

public class BaseLayoutController {

    @FXML
    private StackPane contentArea;

    public void setContent(Parent node) {
        contentArea.getChildren().setAll(node);
    }

    @FXML
    private void handleLogout(MouseEvent event) {
        System.out.println("Logout clicked");

        try {
            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/com/example/demo/login.fxml"));
            Parent loginView = loginLoader.load();
            setContent(loginView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
