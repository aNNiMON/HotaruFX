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

    @Override
    public void buildTimeline(TimeLine timeline) {
        super.buildTimeline(timeline);
        fill.applyIfPresent(timeline);
        stroke.applyIfPresent(timeline);
    }

    @Override
    public PropertyBindings propertyBindings(PropertyBindings bindings) {
        return super.propertyBindings(bindings)
                .add("fill", PAINT, this::fillProperty)
                .add("stroke", PAINT, this::strokeProperty);
    }
}
