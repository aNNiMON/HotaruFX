package com.annimon.hotarufx.visual;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import lombok.Getter;

public class Composition {

    @Getter
    private final Group group;

    @Getter
    private final Scene scene;

    @Getter
    private final int
            virtualWidth, virtualHeight,
            sceneWidth, sceneHeight;
    @Getter
    private final double factor;

    public Composition(int sceneWidth, int sceneHeight, Paint background, Group group) {
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        virtualHeight = 1080;
        factor = virtualHeight / (double) sceneHeight;
        virtualWidth = (int) (sceneWidth * factor);
        group.setScaleX(1d / factor);
        group.setScaleY(1d / factor);
        group.setTranslateX(sceneWidth / 2);
        group.setTranslateY(sceneHeight / 2);
        this.group = group;
        this.scene = new Scene(group, sceneWidth, sceneHeight, background);
    }

    public void add(Node node) {
        group.getChildren().add(node);
    }

}
