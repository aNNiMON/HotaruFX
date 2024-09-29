package com.annimon.hotarufx.visual;

import com.annimon.hotarufx.ui.control.NodesGroup;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Composition {

    private final int virtualWidth;
    private final int virtualHeight;
    private final int sceneWidth;
    private final int sceneHeight;
    private final double factor;

    private final TimeLine timeline;
    private final VirtualScene scene;
    private final Paint background;

    public Composition() {
        this(1280, 720);
    }

    public Composition(double frameRate) {
        this(1280, 720, frameRate);
    }

    public Composition(int sceneWidth, int sceneHeight) {
        this(sceneWidth, sceneHeight, 30);
    }

    public Composition(int sceneWidth, int sceneHeight, double frameRate) {
        this(sceneWidth, sceneHeight, frameRate, Color.WHITE);
    }

    public Composition(int sceneWidth, int sceneHeight, double frameRate, Paint background) {
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.background = background;
        virtualHeight = 1080;
        factor = virtualHeight / (double) sceneHeight;
        virtualWidth = (int) (sceneWidth * factor);
        timeline = new TimeLine(frameRate);
        scene = newScene();
    }

    public int getVirtualWidth() {
        return virtualWidth;
    }

    public int getVirtualHeight() {
        return virtualHeight;
    }

    public int getSceneWidth() {
        return sceneWidth;
    }

    public int getSceneHeight() {
        return sceneHeight;
    }

    public double getFactor() {
        return factor;
    }

    public TimeLine getTimeline() {
        return timeline;
    }

    public VirtualScene getScene() {
        return scene;
    }

    public Paint getBackground() {
        return background;
    }

    private VirtualScene newScene() {
        final var group = new NodesGroup(sceneWidth, sceneHeight);
        group.setScaleX(1d / factor);
        group.setScaleY(1d / factor);
        group.setTranslateX(sceneWidth / 2f);
        group.setTranslateY(sceneHeight / 2f);
        return new VirtualScene(group, virtualWidth, virtualHeight);
    }

    public Scene producePreviewScene() {
        final var fxScene = new Scene(scene.group(), sceneWidth, sceneHeight, background);
        fxScene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case SPACE -> timeline.togglePause();
                case BACK_SPACE -> {
                    timeline.getFxTimeline().stop();
                    timeline.getFxTimeline().playFromStart();
                }
                case LEFT, RIGHT -> {
                    int sign = e.getCode() == KeyCode.LEFT ? -1 : 1;
                    if (e.isShiftDown()) {
                        timeline.seek(sign);
                    } else if (e.isControlDown()) {
                        timeline.seek(10 * sign);
                    } else if (e.isAltDown()) {
                        timeline.seek(30 * sign);
                    } else {
                        timeline.seekFrame(sign);
                    }
                }
            }
        });
        return fxScene;
    }

    public Scene produceRendererScene() {
        return new Scene(scene.group(), sceneWidth, sceneHeight, background);
    }
}
