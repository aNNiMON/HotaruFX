package com.annimon.hotarufx;

import com.annimon.hotarufx.visual.Composition;
import com.annimon.hotarufx.visual.KeyFrame;
import com.annimon.hotarufx.visual.objects.CircleNode;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.val;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        val composition = new Composition(1280, 720, 30);
        val scene = composition.newScene(KeyFrame.of(0));

        val colors = new Paint[] {Color.GREEN, Color.RED};
        val halfWidth = scene.getVirtualWidth() / 2;
        val halfHeight = scene.getVirtualHeight() / 2;
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                val circle = new CircleNode();
                circle.getCircle().setFill(colors[Math.abs(x * y)]);
                circle.getCircle().setCenterX(x * halfWidth);
                circle.getCircle().setCenterY(y * halfHeight);
                circle.getCircle().setRadius(50);
                circle.render(scene);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
