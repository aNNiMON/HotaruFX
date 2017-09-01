package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import javafx.scene.shape.Circle;
import static com.annimon.hotarufx.visual.PropertyType.*;
import static com.annimon.hotarufx.visual.objects.PropertyConsumers.*;

public class CircleNode extends ShapeNode {

    public final Circle circle;

    private PropertyTimelineHolder<Number> centerX, centerY, radius;

    public CircleNode() {
        this(new Circle());
    }

    private CircleNode(Circle circle) {
        super(circle);
        this.circle = circle;
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

    @Override
    public void buildTimeline(TimeLine timeline) {
        super.buildTimeline(timeline);
        centerX.ifPresent(numberConsumer(timeline));
        centerY.ifPresent(numberConsumer(timeline));
        radius.ifPresent(numberConsumer(timeline));
    }

    @Override
    public PropertyBindings propertyBindings(PropertyBindings bindings) {
        return super.propertyBindings(bindings)
                .add("cx", NUMBER, this::centerXProperty)
                .add("centerX", NUMBER, this::centerXProperty)
                .add("cy", NUMBER, this::centerYProperty)
                .add("centerY", NUMBER, this::centerYProperty)
                .add("radius", NUMBER, this::radiusProperty);
    }

    @Override
    public <R, T> R accept(NodeVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
