package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.*;
import javafx.scene.layout.Region;
import static com.annimon.hotarufx.visual.PropertyType.*;
import static com.annimon.hotarufx.visual.PropertyType.BOOLEAN;

public abstract class RegionNode extends ObjectNode {

    // ObjectProperty<Background> background
    // ObjectProperty<Insets> padding
    // ObjectProperty<Border> border
    // ObjectProperty<Shape> shape

    private final Region region;
    private final PropertyTimelineHolder<Boolean> cacheShape;
    private final PropertyTimelineHolder<Boolean> centerShape;
    private final PropertyTimelineHolder<Boolean> scaleShape;
    private final PropertyTimelineHolder<Boolean> snapToPixel;
    private final PropertyTimelineHolder<Number> minWidth, minHeight;
    private final PropertyTimelineHolder<Number> prefWidth, prefHeight;
    private final PropertyTimelineHolder<Number> maxWidth, maxHeight;

    protected RegionNode(Region region) {
        super(region);
        this.region = region;
        cacheShape = PropertyTimelineHolder.empty();
        centerShape = PropertyTimelineHolder.empty();
        scaleShape = PropertyTimelineHolder.empty();
        snapToPixel = PropertyTimelineHolder.empty();
        minWidth = PropertyTimelineHolder.empty();
        minHeight = PropertyTimelineHolder.empty();
        prefWidth = PropertyTimelineHolder.empty();
        prefHeight = PropertyTimelineHolder.empty();
        maxWidth = PropertyTimelineHolder.empty();
        maxHeight = PropertyTimelineHolder.empty();
    }

    public PropertyTimeline<Boolean> cacheShapeProperty() {
        return cacheShape.setIfEmptyThenGet(region::cacheShapeProperty);
    }

    public PropertyTimeline<Boolean> centerShapeProperty() {
        return centerShape.setIfEmptyThenGet(region::centerShapeProperty);
    }

    public PropertyTimeline<Boolean> scaleShapeProperty() {
        return scaleShape.setIfEmptyThenGet(region::scaleShapeProperty);
    }

    public PropertyTimeline<Boolean> snapToPixelProperty() {
        return snapToPixel.setIfEmptyThenGet(region::snapToPixelProperty);
    }

    public PropertyTimeline<Number> minWidthProperty() {
        return minWidth.setIfEmptyThenGet(region::minWidthProperty);
    }

    public PropertyTimeline<Number> minHeightProperty() {
        return minHeight.setIfEmptyThenGet(region::minHeightProperty);
    }

    public PropertyTimeline<Number> prefWidthProperty() {
        return prefWidth.setIfEmptyThenGet(region::prefWidthProperty);
    }

    public PropertyTimeline<Number> prefHeightProperty() {
        return prefHeight.setIfEmptyThenGet(region::prefHeightProperty);
    }

    public PropertyTimeline<Number> maxWidthProperty() {
        return maxWidth.setIfEmptyThenGet(region::maxWidthProperty);
    }

    public PropertyTimeline<Number> maxHeightProperty() {
        return maxHeight.setIfEmptyThenGet(region::maxHeightProperty);
    }

    @Override
    public void buildTimeline(TimeLine timeline) {
        super.buildTimeline(timeline);

        cacheShape.applyIfPresent(timeline);
        centerShape.applyIfPresent(timeline);
        scaleShape.applyIfPresent(timeline);
        snapToPixel.applyIfPresent(timeline);

        minWidth.applyIfPresent(timeline);
        minHeight.applyIfPresent(timeline);
        prefWidth.applyIfPresent(timeline);
        prefHeight.applyIfPresent(timeline);
        maxWidth.applyIfPresent(timeline);
        maxHeight.applyIfPresent(timeline);
    }

    @Override
    public PropertyBindings propertyBindings(PropertyBindings bindings) {
        return super.propertyBindings(bindings)
                .add("cacheShape", BOOLEAN, this::cacheShapeProperty)
                .add("centerShape", BOOLEAN, this::centerShapeProperty)
                .add("scaleShape", BOOLEAN, this::scaleShapeProperty)
                .add("snapToPixel", BOOLEAN, this::snapToPixelProperty)

                .add("minWidth", NUMBER, this::minWidthProperty)
                .add("minHeight", NUMBER, this::minHeightProperty)
                .add("prefWidth", NUMBER, this::prefWidthProperty)
                .add("prefHeight", NUMBER, this::prefHeightProperty)
                .add("maxWidth", NUMBER, this::maxWidthProperty)
                .add("maxHeight", NUMBER, this::maxHeightProperty);
    }
}
