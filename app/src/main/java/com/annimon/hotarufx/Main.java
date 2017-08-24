package com.annimon.hotarufx;

import com.annimon.hotarufx.visual.Composition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.val;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        val group = new Group();
        val composition = new Composition(1280, 720, Color.WHITE, group);
        primaryStage.setTitle("HotaruFX");
        primaryStage.setScene(composition.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
