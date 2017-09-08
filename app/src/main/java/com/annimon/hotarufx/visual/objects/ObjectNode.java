package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.exceptions.HotaruRuntimeException;
import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import java.util.EnumSet;
import java.util.function.Supplier;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.WritableValue;
import javafx.scene.Node;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import static com.annimon.hotarufx.visual.PropertyType.*;

public abstract class ObjectNode {

    private final Node node;
    private PropertyTimelineHolder<Boolean> visible;
    private PropertyTimelineHolder<Number> opacity;
    private PropertyTimelineHolder<Node> clip;
    private PropertyTimelineHolder<String> style;
    private PropertyTimelineHolder<Number> rotate;
    private PropertyTimelineHolder<Number> translateX, translateY, translateZ;
    private PropertyTimelineHolder<Number> scaleX, scaleY, scaleZ;
    private PropertyTimelineHolder<Number> layoutX, layoutY;
    @Getter @Setter
    private boolean isRenderable;

    public ObjectNode(Node node) {
        this.node = node;
        visible = PropertyTimelineHolder.empty();
        opacity = PropertyTimelineHolder.empty();
        clip = PropertyTimelineHolder.empty();
        style = PropertyTimelineHolder.empty();
        rotate = PropertyTimelineHolder.empty();
        translateX = PropertyTimelineHolder.empty();
        translateY = PropertyTimelineHolder.empty();
        translateZ = PropertyTimelineHolder.empty();
        scaleX = PropertyTimelineHolder.empty();
        scaleY = PropertyTimelineHolder.empty();
        scaleZ = PropertyTimelineHolder.empty();
        layoutX = PropertyTimelineHolder.empty();
        layoutY = PropertyTimelineHolder.empty();
        isRenderable = true;
    }

    public Node getFxNode() {
        return node;
    }

    public PropertyTimeline<Boolean> visibleProperty() {
        return visible.setIfEmptyThenGet(node::visibleProperty);
    }

    public PropertyTimeline<Number> opacityProperty() {
        return opacity.setIfEmptyThenGet(node::opacityProperty);
    }

    public PropertyTimeline<Node> clipProperty() {
        return clip.setIfEmptyThenGet(node::clipProperty);
    }

    public PropertyTimeline<String> styleProperty() {
        return style.setIfEmptyThenGet(node::styleProperty);
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

    public PropertyTimeline<Number> scaleXProperty() {
        return scaleX.setIfEmptyThenGet(node::scaleXProperty);
    }

    public PropertyTimeline<Number> scaleYProperty() {
        return scaleY.setIfEmptyThenGet(node::scaleYProperty);
    }

    public PropertyTimeline<Number> scaleZProperty() {
        return scaleZ.setIfEmptyThenGet(node::scaleZProperty);
    }

    public PropertyTimeline<Number> layoutXProperty() {
        return layoutX.setIfEmptyThenGet(node::layoutXProperty);
    }

    public PropertyTimeline<Number> layoutYProperty() {
        return layoutY.setIfEmptyThenGet(node::layoutYProperty);
    }

    public void buildTimeline(TimeLine timeline) {
        visible.applyIfPresent(timeline);
        opacity.applyIfPresent(timeline);
        clip.applyIfPresent(timeline);
        style.applyIfPresent(timeline);
        rotate.applyIfPresent(timeline);
        translateX.applyIfPresent(timeline);
        translateY.applyIfPresent(timeline);
        translateZ.applyIfPresent(timeline);
        scaleX.applyIfPresent(timeline);
        scaleY.applyIfPresent(timeline);
        scaleZ.applyIfPresent(timeline);
        layoutX.applyIfPresent(timeline);
        layoutY.applyIfPresent(timeline);
    }

    public final PropertyBindings propertyBindings() {
        return propertyBindings(new PropertyBindings());
    }

    protected PropertyBindings propertyBindings(PropertyBindings bindings) {
        return bindings
                .add("visible", BOOLEAN, this::visibleProperty)
                .add("opacity", NUMBER, this::opacityProperty)
                .add("clip", CLIP_NODE, this::clipProperty)
                .add("css", STRING, this::styleProperty)
                .add("style", STRING, this::styleProperty)
                .add("rotate", NUMBER, this::rotateProperty)
                .add("translateX", NUMBER, this::translateXProperty)
                .add("translateY", NUMBER, this::translateYProperty)
                .add("translateZ", NUMBER, this::translateZProperty)
                .add("scaleX", NUMBER, this::scaleXProperty)
                .add("scaleY", NUMBER, this::scaleYProperty)
                .add("scaleZ", NUMBER, this::scaleZProperty)
                .add("layoutX", NUMBER, this::layoutXProperty)
                .add("layoutY", NUMBER, this::layoutYProperty);
    }

    protected <T extends Enum<T>> Supplier<WritableValue<String>> enumToString(Class<T> enumClass, ObjectProperty<T> property) {
        return () -> {
            val stringProperty = new SimpleStringProperty();
            stringProperty.bindBidirectional(property, new StringConverter<T>() {
                @Override
                public String toString(T object) {
                    return object.name();
                }

                @Override
                public T fromString(String string) {
                    try {
                        return Enum.valueOf(enumClass, string);
                    } catch (IllegalArgumentException e) {
                        try {
                            val number = (int) Double.parseDouble(string);
                            return enumClass.cast(EnumSet.allOf(enumClass).toArray()[number]);
                        } catch (Exception ex) {
                            throw new HotaruRuntimeException("No constant " + string
                                    + " for type " + enumClass.getSimpleName());
                        }
                    }
                }
            });
            return stringProperty;
        };
    }

    public abstract <R, T> R accept(NodeVisitor<R, T> visitor, T input);
}
