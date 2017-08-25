package com.annimon.hotarufx;

import com.annimon.hotarufx.visual.Composition;
import com.annimon.hotarufx.visual.KeyFrame;
import com.annimon.hotarufx.visual.objects.CircleNode;
import com.annimon.hotarufx.visual.visitors.RenderVisitor;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lombok.val;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        val composition = new Composition(1280, 720, 30);
        val scene = composition.getScene();

        val colors = new Paint[] {Color.GREEN, Color.RED};
        val halfWidth = scene.getVirtualWidth() / 2;
        val halfHeight = scene.getVirtualHeight() / 2;
        val renderVisitor = new RenderVisitor(composition.getTimeline());
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                val node = new CircleNode();
                val colorIndex = Math.abs(x * y);
                node.circle.setFill(colors[colorIndex]);
                node.circle.setCenterX(x * halfWidth);
                node.circle.setCenterY(y * halfHeight);
                node.circle.setRadius(50);
                node.radiusProperty()
                        .add(KeyFrame.of(30), 70)
                        .add(KeyFrame.of(90), 20)
                        .add(KeyFrame.of(300), 70);
                node.fillProperty()
                        .add(KeyFrame.of(150), colors[1 - colorIndex])
                        .add(KeyFrame.of(300), colors[colorIndex]);
                if (x == 0 && y == 0) {
                    node.centerXProperty()
                            .add(KeyFrame.of(60), 0)
                            .add(KeyFrame.of(90), -400)
                            .add(KeyFrame.of(150), 400)
                            .add(KeyFrame.of(180), 0);
                    node.centerYProperty()
                            .add(KeyFrame.of(180), 0)
                            .add(KeyFrame.of(210), -400)
                            .add(KeyFrame.of(270), 400)
                            .add(KeyFrame.of(300), 0);
                    node.radiusProperty()
                            .add(KeyFrame.of(320), 180);
                    node.fillProperty()
                            .add(KeyFrame.of(300), node.circle.getFill())
                            .add(KeyFrame.of(320), Color.WHITE);
                }
                node.accept(renderVisitor, scene);
            }
        }

        primaryStage.setTitle("HotaruFX");
        primaryStage.setScene(composition.produceAnimationScene());
        composition.getTimeline().getFxTimeline().play();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
