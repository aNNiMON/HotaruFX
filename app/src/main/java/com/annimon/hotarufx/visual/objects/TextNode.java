package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import static com.annimon.hotarufx.visual.PropertyType.*;
import static com.annimon.hotarufx.visual.objects.PropertyConsumers.*;

public class TextNode extends ShapeNode {

    public final Text text;

    private PropertyTimelineHolder<Number> x, y;
    private PropertyTimelineHolder<String> textProperty;
    private PropertyTimelineHolder<Font> font;

    public TextNode() {
        this(new Text());
    }

    private TextNode(Text text) {
        super(text);
        this.text = text;
        x = PropertyTimelineHolder.empty();
        y = PropertyTimelineHolder.empty();
        textProperty = PropertyTimelineHolder.empty();
        font = PropertyTimelineHolder.empty();
    }

    public PropertyTimeline<Number> xProperty() {
        return x.setIfEmptyThenGet(text::xProperty);
    }

    public PropertyTimeline<Number> yProperty() {
        return y.setIfEmptyThenGet(text::yProperty);
    }

    public PropertyTimeline<String> textProperty() {
        return textProperty.setIfEmptyThenGet(text::textProperty);
    }

    public PropertyTimeline<Font> fontProperty() {
        return font.setIfEmptyThenGet(text::fontProperty);
    }

    @Override
    public void buildTimeline(TimeLine timeline) {
        super.buildTimeline(timeline);
        x.ifPresent(numberConsumer(timeline));
        y.ifPresent(numberConsumer(timeline));
        font.ifPresent(genericConsumer(timeline));
        textProperty.ifPresent(stringConsumer(timeline));
    }

    @Override
    public PropertyBindings propertyBindings(PropertyBindings bindings) {
        return super.propertyBindings(bindings)
                .add("x", NUMBER, this::xProperty)
                .add("y", NUMBER, this::yProperty)
                .add("text", STRING, this::textProperty)
                .add("font", FONT, this::fontProperty);
    }

    @Override
    public <R, T> R accept(NodeVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
