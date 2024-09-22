package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static com.annimon.hotarufx.visual.PropertyType.BOOLEAN;
import static com.annimon.hotarufx.visual.PropertyType.NUMBER;

public class ImageNode extends ObjectNode {

    public final ImageView imageView;

    private final PropertyTimelineHolder<Number> x;
    private final PropertyTimelineHolder<Number> y;
    private final PropertyTimelineHolder<Number> fitWidth;
    private final PropertyTimelineHolder<Number> fitHeight;
    private final PropertyTimelineHolder<Boolean> preserveRatio;
    private final PropertyTimelineHolder<Boolean> smooth;

    public ImageNode(String url) {
        this(url, new ImageView());
    }

    private ImageNode(String url, ImageView imageView) {
        super(imageView);
        this.imageView = imageView;
        imageView.imageProperty().setValue(new Image(url));
        x = PropertyTimelineHolder.empty();
        y = PropertyTimelineHolder.empty();
        fitWidth = PropertyTimelineHolder.empty();
        fitHeight = PropertyTimelineHolder.empty();
        preserveRatio = PropertyTimelineHolder.empty();
        smooth = PropertyTimelineHolder.empty();
    }

    public PropertyTimeline<Number> xProperty() {
        return x.setIfEmptyThenGet(imageView::xProperty);
    }

    public PropertyTimeline<Number> yProperty() {
        return y.setIfEmptyThenGet(imageView::yProperty);
    }

    public PropertyTimeline<Number> fitWidthProperty() {
        return fitWidth.setIfEmptyThenGet(imageView::fitWidthProperty);
    }

    public PropertyTimeline<Number> fitHeightProperty() {
        return fitHeight.setIfEmptyThenGet(imageView::fitHeightProperty);
    }

    public PropertyTimeline<Boolean> preserveRatioProperty() {
        return preserveRatio.setIfEmptyThenGet(imageView::preserveRatioProperty);
    }

    public PropertyTimeline<Boolean> smoothProperty() {
        return smooth.setIfEmptyThenGet(imageView::smoothProperty);
    }

    @Override
    public void buildTimeline(TimeLine timeline) {
        super.buildTimeline(timeline);
        x.applyIfPresent(timeline);
        y.applyIfPresent(timeline);
        fitWidth.applyIfPresent(timeline);
        fitHeight.applyIfPresent(timeline);
        preserveRatio.applyIfPresent(timeline);
        smooth.applyIfPresent(timeline);
    }

    @Override
    public PropertyBindings propertyBindings(PropertyBindings bindings) {
        return super.propertyBindings(bindings)
                .add("x", NUMBER, this::xProperty)
                .add("y", NUMBER, this::yProperty)
                .add("width", NUMBER, this::fitWidthProperty)
                .add("fitWidth", NUMBER, this::fitWidthProperty)
                .add("height", NUMBER, this::fitHeightProperty)
                .add("fitHeight", NUMBER, this::fitHeightProperty)
                .add("ratio", BOOLEAN, this::preserveRatioProperty)
                .add("preserveRatio", BOOLEAN, this::preserveRatioProperty)
                .add("smooth", BOOLEAN, this::smoothProperty);
    }

    @Override
    public <R, T> R accept(NodeVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
