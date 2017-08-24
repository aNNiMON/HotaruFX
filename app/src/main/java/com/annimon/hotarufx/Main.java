package com.annimon.hotarufx;

import com.annimon.hotarufx.visual.Composition;
import com.annimon.hotarufx.visual.objects.CircleNode;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lombok.val;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        val group = new Group();
        val composition = new Composition(1280, 720, Color.WHITE, group);

        val colors = new Paint[] {Color.GREEN, Color.RED};
        val halfWidth = composition.getVirtualWidth() / 2;
        val halfHeight = composition.getVirtualHeight() / 2;
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                val circle = new CircleNode();
                circle.getCircle().setFill(colors[Math.abs(x * y)]);
                circle.getCircle().setCenterX(x * halfWidth);
                circle.getCircle().setCenterY(y * halfHeight);
                circle.getCircle().setRadius(50);
                circle.render(composition);
            }
        }

        primaryStage.setTitle("HotaruFX");
        primaryStage.setScene(composition.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
