package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import static com.annimon.hotarufx.visual.PropertyType.NUMBER;
import static com.annimon.hotarufx.visual.PropertyType.STRING;

public class ArcNode extends ShapeNode {

    public final Arc arc;

    private final PropertyTimelineHolder<Number> centerX;
    private final PropertyTimelineHolder<Number> centerY;
    private final PropertyTimelineHolder<Number> radiusX;
    private final PropertyTimelineHolder<Number> radiusY;
    private final PropertyTimelineHolder<Number> startAngle;
    private final PropertyTimelineHolder<Number> length;
    private final PropertyTimelineHolder<String> type;

    public ArcNode() {
        this(new Arc());
    }

    private ArcNode(Arc arc) {
        super(arc);
        this.arc = arc;
        arc.setType(ArcType.ROUND);
        centerX = PropertyTimelineHolder.empty();
        centerY = PropertyTimelineHolder.empty();
        radiusX = PropertyTimelineHolder.empty();
        radiusY = PropertyTimelineHolder.empty();
        startAngle = PropertyTimelineHolder.empty();
        length = PropertyTimelineHolder.empty();
        type = PropertyTimelineHolder.empty();
    }

    public PropertyTimeline<Number> centerXProperty() {
        return centerX.setIfEmptyThenGet(arc::centerXProperty);
    }

    public PropertyTimeline<Number> centerYProperty() {
        return centerY.setIfEmptyThenGet(arc::centerYProperty);
    }

    public PropertyTimeline<Number> radiusXProperty() {
        return radiusX.setIfEmptyThenGet(arc::radiusXProperty);
    }

    public PropertyTimeline<Number> radiusYProperty() {
        return radiusY.setIfEmptyThenGet(arc::radiusYProperty);
    }

    public PropertyTimeline<Number> startAngleProperty() {
        return startAngle.setIfEmptyThenGet(arc::startAngleProperty);
    }

    public PropertyTimeline<Number> lengthProperty() {
        return length.setIfEmptyThenGet(arc::lengthProperty);
    }

    public PropertyTimeline<String> typeProperty() {
        return type.setIfEmptyThenGet(enumToString(ArcType.class, arc.typeProperty()));
    }

    @Override
    public void buildTimeline(TimeLine timeline) {
        super.buildTimeline(timeline);
        centerX.applyIfPresent(timeline);
        centerY.applyIfPresent(timeline);
        radiusX.applyIfPresent(timeline);
        radiusY.applyIfPresent(timeline);
        startAngle.applyIfPresent(timeline);
        length.applyIfPresent(timeline);
        type.applyIfPresent(timeline);
    }

    @Override
    public PropertyBindings propertyBindings(PropertyBindings bindings) {
        return super.propertyBindings(bindings)
                .add("cx", NUMBER, this::centerXProperty)
                .add("centerX", NUMBER, this::centerXProperty)
                .add("cy", NUMBER, this::centerYProperty)
                .add("centerY", NUMBER, this::centerYProperty)
                .add("radiusX", NUMBER, this::radiusXProperty)
                .add("radiusY", NUMBER, this::radiusYProperty)
                .add("angle", NUMBER, this::startAngleProperty)
                .add("startAngle", NUMBER, this::startAngleProperty)
                .add("length", NUMBER, this::lengthProperty)
                .add("type", STRING, this::typeProperty);
    }

    @Override
    public <R, T> R accept(NodeVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
