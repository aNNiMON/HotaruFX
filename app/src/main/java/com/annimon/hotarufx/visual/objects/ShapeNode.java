package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.TimeLine;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import static com.annimon.hotarufx.visual.PropertyType.*;

public abstract class ShapeNode extends ObjectNode {

    private final Shape shape;

    private PropertyTimelineHolder<Boolean> smooth;
    private PropertyTimelineHolder<Paint> fill, stroke;
    private PropertyTimelineHolder<Number> strokeWidth, strokeDashOffset, strokeMiterLimit;

    public ShapeNode(Shape shape) {
        super(shape);
        this.shape = shape;
        smooth = PropertyTimelineHolder.empty();
        fill = PropertyTimelineHolder.empty();
        stroke = PropertyTimelineHolder.empty();
        strokeWidth = PropertyTimelineHolder.empty();
        strokeDashOffset = PropertyTimelineHolder.empty();
        strokeMiterLimit = PropertyTimelineHolder.empty();
    }

    public PropertyTimeline<Boolean> smoothProperty() {
        return smooth.setIfEmptyThenGet(shape::smoothProperty);
    }

    public PropertyTimeline<Paint> fillProperty() {
        return fill.setIfEmptyThenGet(shape::fillProperty);
    }

    public PropertyTimeline<Paint> strokeProperty() {
        return stroke.setIfEmptyThenGet(shape::strokeProperty);
    }

    public PropertyTimeline<Number> strokeWidthProperty() {
        return strokeWidth.setIfEmptyThenGet(shape::strokeWidthProperty);
    }

    public PropertyTimeline<Number> strokeDashOffsetProperty() {
        return strokeDashOffset.setIfEmptyThenGet(shape::strokeDashOffsetProperty);
    }

    public PropertyTimeline<Number> strokeMiterLimitProperty() {
        return strokeMiterLimit.setIfEmptyThenGet(shape::strokeMiterLimitProperty);
    }

    @Override
    public void buildTimeline(TimeLine timeline) {
        super.buildTimeline(timeline);
        smooth.applyIfPresent(timeline);
        fill.applyIfPresent(timeline);
        stroke.applyIfPresent(timeline);
        strokeWidth.applyIfPresent(timeline);
        strokeDashOffset.applyIfPresent(timeline);
        strokeMiterLimit.applyIfPresent(timeline);
    }

    @Override
    public PropertyBindings propertyBindings(PropertyBindings bindings) {
        return super.propertyBindings(bindings)
                .add("smooth", BOOLEAN, this::smoothProperty)
                .add("fill", PAINT, this::fillProperty)
                .add("stroke", PAINT, this::strokeProperty)
                .add("strokeWidth", NUMBER, this::strokeWidthProperty)
                .add("strokeDashOffset", NUMBER, this::strokeDashOffsetProperty)
                .add("strokeMiterLimit", NUMBER, this::strokeMiterLimitProperty);
    }
}
