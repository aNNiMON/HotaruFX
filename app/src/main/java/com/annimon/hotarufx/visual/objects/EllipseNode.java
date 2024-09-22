package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import javafx.scene.shape.Ellipse;
import static com.annimon.hotarufx.visual.PropertyType.NUMBER;

public class EllipseNode extends ShapeNode {

    public final Ellipse ellipse;

    private final PropertyTimelineHolder<Number> centerX;
    private final PropertyTimelineHolder<Number> centerY;
    private final PropertyTimelineHolder<Number> radiusX;
    private final PropertyTimelineHolder<Number> radiusY;

    public EllipseNode() {
        this(new Ellipse());
    }

    private EllipseNode(Ellipse ellipse) {
        super(ellipse);
        this.ellipse = ellipse;
        centerX = PropertyTimelineHolder.empty();
        centerY = PropertyTimelineHolder.empty();
        radiusX = PropertyTimelineHolder.empty();
        radiusY = PropertyTimelineHolder.empty();
    }

    public PropertyTimeline<Number> centerXProperty() {
        return centerX.setIfEmptyThenGet(ellipse::centerXProperty);
    }

    public PropertyTimeline<Number> centerYProperty() {
        return centerY.setIfEmptyThenGet(ellipse::centerYProperty);
    }

    public PropertyTimeline<Number> radiusXProperty() {
        return radiusX.setIfEmptyThenGet(ellipse::radiusXProperty);
    }

    public PropertyTimeline<Number> radiusYProperty() {
        return radiusY.setIfEmptyThenGet(ellipse::radiusYProperty);
    }

    @Override
    public void buildTimeline(TimeLine timeline) {
        super.buildTimeline(timeline);
        centerX.applyIfPresent(timeline);
        centerY.applyIfPresent(timeline);
        radiusX.applyIfPresent(timeline);
        radiusY.applyIfPresent(timeline);
    }

    @Override
    public PropertyBindings propertyBindings(PropertyBindings bindings) {
        return super.propertyBindings(bindings)
                .add("cx", NUMBER, this::centerXProperty)
                .add("centerX", NUMBER, this::centerXProperty)
                .add("cy", NUMBER, this::centerYProperty)
                .add("centerY", NUMBER, this::centerYProperty)
                .add("radiusX", NUMBER, this::radiusXProperty)
                .add("radiusY", NUMBER, this::radiusYProperty);
    }

    @Override
    public <R, T> R accept(NodeVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
