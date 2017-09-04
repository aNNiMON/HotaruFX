package com.annimon.hotarufx;

import com.annimon.hotarufx.ui.controller.EditorController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.val;

public class Main extends Application {

    private EditorController controller;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("HotaruFX");
        try {
            val loader = new FXMLLoader(getClass().getResource("/fxml/Editor.fxml"));
            val scene = new Scene(loader.load());
            controller = loader.getController();
            primaryStage.setScene(scene);
        } catch (IOException e) {
            // TODO: notice me!!
        }
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (controller != null) {
            controller.stop();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
