package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.Composition;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.val;

public class CircleNode implements ObjectNode {

    @Getter
    private final Circle circle;

    public CircleNode() {
        circle = new Circle();
    }

    @Override
    public void render(Composition composition) {
        composition.add(circle);
    }
}
