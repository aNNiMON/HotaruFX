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
    private final double frameRate;

    @Getter
    private final TimeLine timeLine;

    public Composition(int sceneWidth, int sceneHeight, double frameRate) {
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.frameRate = frameRate;
        virtualHeight = 1080;
        factor = virtualHeight / (double) sceneHeight;
        virtualWidth = (int) (sceneWidth * factor);
        timeLine = new TimeLine();
    }

    public VirtualScene newScene(KeyFrame keyFrame) {
        val group = new Group();
        group.setScaleX(1d / factor);
        group.setScaleY(1d / factor);
        group.setTranslateX(sceneWidth / 2);
        group.setTranslateY(sceneHeight / 2);
        val scene = new VirtualScene(group, virtualWidth, virtualHeight);
        timeLine.add(keyFrame, scene);
        return scene;
    }

    public Scene produceAnimationScene(VirtualScene scene) {
        return new Scene(scene.getGroup(), sceneWidth, sceneHeight, Color.WHITE);
    }
}
