package com.annimon.hotarufx;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.val;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("HotaruFX");
        try {
            val loader = new FXMLLoader(getClass().getResource("/fxml/Editor.fxml"));
            val scene = new Scene(loader.load());
            primaryStage.setScene(scene);
        } catch (IOException e) {
            // TODO: notice me!!
        }
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
