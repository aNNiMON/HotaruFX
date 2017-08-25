package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import javafx.scene.shape.Circle;

public class CircleNode extends ObjectNode {

    public final Circle circle;

    private PropertyTimelineHolder<Number> centerX, centerY, radius;

    public CircleNode() {
        circle = new Circle();
        centerX = PropertyTimelineHolder.empty();
        centerY = PropertyTimelineHolder.empty();
        radius = PropertyTimelineHolder.empty();
    }

    public PropertyTimeline<Number> centerXProperty() {
        return centerX.setIfEmptyThenGet(circle::centerXProperty);
    }

    public PropertyTimeline<Number> centerYProperty() {
        return centerY.setIfEmptyThenGet(circle::centerYProperty);
    }

    public PropertyTimeline<Number> radiusProperty() {
        return radius.setIfEmptyThenGet(circle::radiusProperty);
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
