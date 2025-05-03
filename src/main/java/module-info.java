module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.prefs;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires jbcrypt;
    requires javafx.media;
    requires java.desktop;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}