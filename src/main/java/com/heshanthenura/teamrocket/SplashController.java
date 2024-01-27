package com.heshanthenura.teamrocket;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @FXML
    private AnchorPane root;

    private Stage stage;
    private Scene scene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            scene = root.getScene();
            stage = (Stage) scene.getWindow();

            // Use StageStyle.TRANSPARENT for the splash screen
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), scene.getRoot());
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.setOnFinished(event -> {
                try {
                    stage.close();
                    loadMainScene();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            fadeTransition.play();
        });
    }

    private void loadMainScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 300);
        Stage newStage = new Stage();
        newStage.setTitle("Demo");
        newStage.setScene(scene);
        newStage.setMaximized(true);
        newStage.show();
    }
}
