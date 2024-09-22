package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import javafx.scene.shape.Line;
import static com.annimon.hotarufx.visual.PropertyType.NUMBER;

public class LineNode extends ShapeNode {

    public final Line line;

    private final PropertyTimelineHolder<Number> startX;
    private final PropertyTimelineHolder<Number> startY;
    private final PropertyTimelineHolder<Number> endX;
    private final PropertyTimelineHolder<Number> endY;

    public LineNode() {
        this(new Line());
    }

    private LineNode(Line line) {
        super(line);
        this.line = line;
        startX = PropertyTimelineHolder.empty();
        startY = PropertyTimelineHolder.empty();
        endX = PropertyTimelineHolder.empty();
        endY = PropertyTimelineHolder.empty();
    }

    public PropertyTimeline<Number> startXProperty() {
        return startX.setIfEmptyThenGet(line::startXProperty);
    }

    public PropertyTimeline<Number> startYProperty() {
        return startY.setIfEmptyThenGet(line::startYProperty);
    }

    public PropertyTimeline<Number> endXProperty() {
        return endX.setIfEmptyThenGet(line::endXProperty);
    }

    public PropertyTimeline<Number> endYProperty() {
        return endY.setIfEmptyThenGet(line::endYProperty);
    }

    @Override
    public void buildTimeline(TimeLine timeline) {
        super.buildTimeline(timeline);
        startX.applyIfPresent(timeline);
        startY.applyIfPresent(timeline);
        endX.applyIfPresent(timeline);
        endY.applyIfPresent(timeline);
    }

    @Override
    public PropertyBindings propertyBindings(PropertyBindings bindings) {
        return super.propertyBindings(bindings)
                .add("startX", NUMBER, this::startXProperty)
                .add("startY", NUMBER, this::startYProperty)
                .add("endX", NUMBER, this::endXProperty)
                .add("endY", NUMBER, this::endYProperty);
    }

    @Override
    public <R, T> R accept(NodeVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
