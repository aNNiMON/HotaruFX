package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.TimeLine;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

public abstract class ShapeNode extends ObjectNode {

    private final Shape shape;

    private PropertyTimelineHolder<Paint> fill, stroke;

    public ShapeNode(Shape shape) {
        super(shape);
        this.shape = shape;
        fill = PropertyTimelineHolder.empty();
        stroke = PropertyTimelineHolder.empty();
    }

    public PropertyTimeline<Paint> fillProperty() {
        return fill.setIfEmptyThenGet(shape::fillProperty);
    }

    public PropertyTimeline<Paint> strokeProperty() {
        return stroke.setIfEmptyThenGet(shape::strokeProperty);
    }

    public void buildTimeline(TimeLine timeline) {
        super.buildTimeline(timeline);
        fill.ifPresent(PropertyConsumers.paintConsumer(timeline));
        stroke.ifPresent(PropertyConsumers.paintConsumer(timeline));
    }
}
