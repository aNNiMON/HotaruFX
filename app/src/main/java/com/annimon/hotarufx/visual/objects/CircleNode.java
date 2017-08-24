package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.VirtualScene;
import javafx.scene.shape.Circle;
import lombok.Getter;

public class CircleNode implements ObjectNode {

    @Getter
    private final Circle circle;

    public CircleNode() {
        circle = new Circle();
    }

    @Override
    public void render(VirtualScene scene) {
        scene.add(circle);
    }
}
