package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.TimeLine;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import static com.annimon.hotarufx.visual.PropertyType.*;

public abstract class ShapeNode extends ObjectNode {

    private final Shape shape;

    private final PropertyTimelineHolder<Boolean> smooth;
    private final PropertyTimelineHolder<Paint> fill;
    private final PropertyTimelineHolder<Paint> stroke;
    private final PropertyTimelineHolder<Number> strokeWidth;
    private final PropertyTimelineHolder<Number> strokeDashOffset;
    private final PropertyTimelineHolder<Number> strokeMiterLimit;
    private final PropertyTimelineHolder<String> strokeType;
    private final PropertyTimelineHolder<String> strokeLineCap;
    private final PropertyTimelineHolder<String> strokeLineJoin;

    public ShapeNode(Shape shape) {
        super(shape);
        this.shape = shape;
        smooth = PropertyTimelineHolder.empty();
        fill = PropertyTimelineHolder.empty();
        stroke = PropertyTimelineHolder.empty();
        strokeWidth = PropertyTimelineHolder.empty();
        strokeType = PropertyTimelineHolder.empty();
        strokeDashOffset = PropertyTimelineHolder.empty();
        strokeMiterLimit = PropertyTimelineHolder.empty();
        strokeLineCap = PropertyTimelineHolder.empty();
        strokeLineJoin = PropertyTimelineHolder.empty();
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

    public PropertyTimeline<String> strokeTypeProperty() {
        return strokeType.setIfEmptyThenGet(enumToString(StrokeType.class, shape.strokeTypeProperty()));
    }

    public PropertyTimeline<Number> strokeDashOffsetProperty() {
        return strokeDashOffset.setIfEmptyThenGet(shape::strokeDashOffsetProperty);
    }

    public PropertyTimeline<Number> strokeMiterLimitProperty() {
        return strokeMiterLimit.setIfEmptyThenGet(shape::strokeMiterLimitProperty);
    }

    public PropertyTimeline<String> strokeLineCapProperty() {
        return strokeLineCap.setIfEmptyThenGet(enumToString(StrokeLineCap.class, shape.strokeLineCapProperty()));
    }

    public PropertyTimeline<String> strokeLineJoinProperty() {
        return strokeLineJoin.setIfEmptyThenGet(enumToString(StrokeLineJoin.class, shape.strokeLineJoinProperty()));
    }

    @Override
    public void buildTimeline(TimeLine timeline) {
        super.buildTimeline(timeline);
        smooth.applyIfPresent(timeline);
        fill.applyIfPresent(timeline);
        stroke.applyIfPresent(timeline);
        strokeType.applyIfPresent(timeline);
        strokeWidth.applyIfPresent(timeline);
        strokeDashOffset.applyIfPresent(timeline);
        strokeMiterLimit.applyIfPresent(timeline);
        strokeLineCap.applyIfPresent(timeline);
        strokeLineJoin.applyIfPresent(timeline);
    }

    @Override
    public PropertyBindings propertyBindings(PropertyBindings bindings) {
        return super.propertyBindings(bindings)
                .add("smooth", BOOLEAN, this::smoothProperty)
                .add("fill", PAINT, this::fillProperty)
                .add("stroke", PAINT, this::strokeProperty)
                .add("strokeType", STRING, this::strokeTypeProperty)
                .add("strokeWidth", NUMBER, this::strokeWidthProperty)
                .add("strokeDashOffset", NUMBER, this::strokeDashOffsetProperty)
                .add("strokeMiterLimit", NUMBER, this::strokeMiterLimitProperty)
                .add("strokeLineCap", STRING, this::strokeLineCapProperty)
                .add("strokeLineJoin", STRING, this::strokeLineJoinProperty);
    }
}
