package com.heshanthenura.teamrocket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("splash.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 300);
        scene.setFill(Color.TRANSPARENT); // Set scene background to transparent
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Demo");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}