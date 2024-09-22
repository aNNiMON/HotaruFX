package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import javafx.scene.shape.Rectangle;
import static com.annimon.hotarufx.visual.PropertyType.*;

public class RectangleNode extends ShapeNode {

    public final Rectangle rectangle;

    private final PropertyTimelineHolder<Number> x;
    private final PropertyTimelineHolder<Number> y;
    private final PropertyTimelineHolder<Number> width;
    private final PropertyTimelineHolder<Number> height;
    private final PropertyTimelineHolder<Number> arcWidth;
    private final PropertyTimelineHolder<Number> arcHeight;

    public RectangleNode() {
        this(new Rectangle());
    }

    private RectangleNode(Rectangle rectangle) {
        super(rectangle);
        this.rectangle = rectangle;
        x = PropertyTimelineHolder.empty();
        y = PropertyTimelineHolder.empty();
        width = PropertyTimelineHolder.empty();
        height = PropertyTimelineHolder.empty();
        arcWidth = PropertyTimelineHolder.empty();
        arcHeight = PropertyTimelineHolder.empty();
    }

    public PropertyTimeline<Number> xProperty() {
        return x.setIfEmptyThenGet(rectangle::xProperty);
    }

    public PropertyTimeline<Number> yProperty() {
        return y.setIfEmptyThenGet(rectangle::yProperty);
    }

    public PropertyTimeline<Number> widthProperty() {
        return width.setIfEmptyThenGet(rectangle::widthProperty);
    }

    public PropertyTimeline<Number> heightProperty() {
        return height.setIfEmptyThenGet(rectangle::heightProperty);
    }

    public PropertyTimeline<Number> arcWidthProperty() {
        return arcWidth.setIfEmptyThenGet(rectangle::arcWidthProperty);
    }

    public PropertyTimeline<Number> arcHeightProperty() {
        return arcHeight.setIfEmptyThenGet(rectangle::arcHeightProperty);
    }

    @Override
    public void buildTimeline(TimeLine timeline) {
        super.buildTimeline(timeline);
        x.applyIfPresent(timeline);
        y.applyIfPresent(timeline);
        width.applyIfPresent(timeline);
        height.applyIfPresent(timeline);
        arcWidth.applyIfPresent(timeline);
        arcHeight.applyIfPresent(timeline);
    }

    @Override
    public PropertyBindings propertyBindings(PropertyBindings bindings) {
        return super.propertyBindings(bindings)
                .add("x", NUMBER, this::xProperty)
                .add("y", NUMBER, this::yProperty)
                .add("width", NUMBER, this::widthProperty)
                .add("height", NUMBER, this::heightProperty)
                .add("arcWidth", NUMBER, this::arcWidthProperty)
                .add("arcHeight", NUMBER, this::arcHeightProperty);
    }

    @Override
    public <R, T> R accept(NodeVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
