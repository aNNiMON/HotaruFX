package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import javafx.scene.Node;
import static com.annimon.hotarufx.visual.PropertyType.*;
import static com.annimon.hotarufx.visual.objects.PropertyConsumers.*;

public abstract class ObjectNode {

    private final Node node;
    private PropertyTimelineHolder<Number> translateX, translateY, translateZ;

    public ObjectNode(Node node) {
        this.node = node;
        translateX = PropertyTimelineHolder.empty();
        translateY = PropertyTimelineHolder.empty();
        translateZ = PropertyTimelineHolder.empty();
    }

    public PropertyTimeline<Number> translateXProperty() {
        return translateX.setIfEmptyThenGet(node::translateXProperty);
    }

    public PropertyTimeline<Number> translateYProperty() {
        return translateY.setIfEmptyThenGet(node::translateYProperty);
    }

    public PropertyTimeline<Number> translateZProperty() {
        return translateZ.setIfEmptyThenGet(node::translateZProperty);
    }

    public void buildTimeline(TimeLine timeline) {
        translateX.ifPresent(numberConsumer(timeline));
        translateY.ifPresent(numberConsumer(timeline));
        translateZ.ifPresent(numberConsumer(timeline));
    }

    public final PropertyBindings propertyBindings() {
        return propertyBindings(new PropertyBindings());
    }

    protected PropertyBindings propertyBindings(PropertyBindings bindings) {
        return bindings
                .add("translateX", NUMBER, this::translateXProperty)
                .add("translateY", NUMBER, this::translateYProperty)
                .add("translateZ", NUMBER, this::translateZProperty);
    }

    public abstract <R, T> R accept(NodeVisitor<R, T> visitor, T input);
}
