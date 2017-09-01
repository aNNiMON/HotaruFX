package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import javafx.scene.Node;
import lombok.Getter;
import lombok.Setter;
import static com.annimon.hotarufx.visual.PropertyType.*;

public abstract class ObjectNode {

    private final Node node;
    private PropertyTimelineHolder<Node> clip;
    private PropertyTimelineHolder<Number> rotate;
    private PropertyTimelineHolder<Number> translateX, translateY, translateZ;
    @Getter @Setter
    private boolean isUsedAsClip;

    public ObjectNode(Node node) {
        this.node = node;
        clip = PropertyTimelineHolder.empty();
        rotate = PropertyTimelineHolder.empty();
        translateX = PropertyTimelineHolder.empty();
        translateY = PropertyTimelineHolder.empty();
        translateZ = PropertyTimelineHolder.empty();
        isUsedAsClip = false;
    }

    public Node getFxNode() {
        return node;
    }

    public PropertyTimeline<Node> clipProperty() {
        return clip.setIfEmptyThenGet(node::clipProperty);
    }

    public PropertyTimeline<Number> rotateProperty() {
        return rotate.setIfEmptyThenGet(node::rotateProperty);
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
        clip.applyIfPresent(timeline);
        rotate.applyIfPresent(timeline);
        translateX.applyIfPresent(timeline);
        translateY.applyIfPresent(timeline);
        translateZ.applyIfPresent(timeline);
    }

    public final PropertyBindings propertyBindings() {
        return propertyBindings(new PropertyBindings());
    }

    protected PropertyBindings propertyBindings(PropertyBindings bindings) {
        return bindings
                .add("clip", CLIP_NODE, this::clipProperty)
                .add("rotate", NUMBER, this::rotateProperty)
                .add("translateX", NUMBER, this::translateXProperty)
                .add("translateY", NUMBER, this::translateYProperty)
                .add("translateZ", NUMBER, this::translateZProperty);
    }

    public abstract <R, T> R accept(NodeVisitor<R, T> visitor, T input);
}
