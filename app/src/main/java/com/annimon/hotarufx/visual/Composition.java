package com.annimon.hotarufx.visual;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.val;

public class Composition {

    @Getter
    private final int
            virtualWidth, virtualHeight,
            sceneWidth, sceneHeight;
    @Getter
    private final double factor;

    @Getter
    private final TimeLine timeline;

    @Getter
    private final VirtualScene scene;

    public Composition(int sceneWidth, int sceneHeight, double frameRate) {
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        virtualHeight = 1080;
        factor = virtualHeight / (double) sceneHeight;
        virtualWidth = (int) (sceneWidth * factor);
        timeline = new TimeLine(frameRate);
        scene = newScene();
    }

    private VirtualScene newScene() {
        val group = new Group();
        group.setScaleX(1d / factor);
        group.setScaleY(1d / factor);
        group.setTranslateX(sceneWidth / 2);
        group.setTranslateY(sceneHeight / 2);
        return new VirtualScene(group, virtualWidth, virtualHeight);
    }

    public Scene produceAnimationScene() {
        return new Scene(scene.getGroup(), sceneWidth, sceneHeight, Color.WHITE);
    }
}
