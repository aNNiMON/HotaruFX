package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import java.util.Optional;
import javafx.scene.shape.Circle;

public class CircleNode extends ObjectNode {

    public final Circle circle;

    private Optional<PropertyTimeline<Number>> centerX, centerY, radius;

    public CircleNode() {
        circle = new Circle();
        centerX = Optional.empty();
        centerY = Optional.empty();
        radius = Optional.empty();
    }

    public PropertyTimeline<Number> centerXProperty() {
        if (!centerX.isPresent()) {
            centerX = Optional.of(new PropertyTimeline<>(circle.centerXProperty()));
        }
        return centerX.get();
    }

    public PropertyTimeline<Number> centerYProperty() {
        if (!centerY.isPresent()) {
            centerY = Optional.of(new PropertyTimeline<>(circle.centerYProperty()));
        }
        return centerY.get();
    }

    public PropertyTimeline<Number> radiusProperty() {
        if (!radius.isPresent()) {
            radius = Optional.of(new PropertyTimeline<>(circle.radiusProperty()));
        }
        return radius.get();
    }

    public void buildTimeline(TimeLine timeline) {
        super.buildTimeline(timeline);
        centerX.ifPresent(PropertyConsumers.numberConsumer(timeline));
        centerY.ifPresent(PropertyConsumers.numberConsumer(timeline));
        radius.ifPresent(PropertyConsumers.numberConsumer(timeline));
    }

    @Override
    public <R, T> R accept(NodeVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
