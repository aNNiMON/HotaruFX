package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import javafx.geometry.VPos;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import static com.annimon.hotarufx.visual.PropertyType.*;

public class TextNode extends ShapeNode {

    public final Text text;

    private PropertyTimelineHolder<Number> x, y;
    private PropertyTimelineHolder<String> textProperty;
    private PropertyTimelineHolder<Font> font;
    private PropertyTimelineHolder<Number> lineSpacing, wrappingWidth;
    private PropertyTimelineHolder<Boolean> strikethrough, underline;
    private PropertyTimelineHolder<String> boundsType, fontSmoothingType;
    private PropertyTimelineHolder<String> textAlignment, textOrigin;

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
        lineSpacing = PropertyTimelineHolder.empty();
        wrappingWidth = PropertyTimelineHolder.empty();
        strikethrough = PropertyTimelineHolder.empty();
        underline = PropertyTimelineHolder.empty();
        boundsType = PropertyTimelineHolder.empty();
        fontSmoothingType = PropertyTimelineHolder.empty();
        textAlignment = PropertyTimelineHolder.empty();
        textOrigin = PropertyTimelineHolder.empty();
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

    public PropertyTimeline<Number> lineSpacingProperty() {
        return lineSpacing.setIfEmptyThenGet(text::lineSpacingProperty);
    }

    public PropertyTimeline<Number> wrappingWidthProperty() {
        return wrappingWidth.setIfEmptyThenGet(text::wrappingWidthProperty);
    }

    public PropertyTimeline<Boolean> strikethroughProperty() {
        return strikethrough.setIfEmptyThenGet(text::strikethroughProperty);
    }

    public PropertyTimeline<Boolean> underlineProperty() {
        return underline.setIfEmptyThenGet(text::underlineProperty);
    }

    public PropertyTimeline<String> boundsTypeProperty() {
        return boundsType.setIfEmptyThenGet(enumToString(TextBoundsType.class, text.boundsTypeProperty()));
    }

    public PropertyTimeline<String> fontSmoothingTypeProperty() {
        return fontSmoothingType.setIfEmptyThenGet(enumToString(FontSmoothingType.class, text.fontSmoothingTypeProperty()));
    }

    public PropertyTimeline<String> textAlignmentProperty() {
        return textAlignment.setIfEmptyThenGet(enumToString(TextAlignment.class, text.textAlignmentProperty()));
    }

    public PropertyTimeline<String> textOriginProperty() {
        return textOrigin.setIfEmptyThenGet(enumToString(VPos.class, text.textOriginProperty()));
    }

    @Override
    public void buildTimeline(TimeLine timeline) {
        super.buildTimeline(timeline);
        x.applyIfPresent(timeline);
        y.applyIfPresent(timeline);
        textProperty.applyIfPresent(timeline);
        font.applyIfPresent(timeline);
        lineSpacing.applyIfPresent(timeline);
        wrappingWidth.applyIfPresent(timeline);
        strikethrough.applyIfPresent(timeline);
        underline.applyIfPresent(timeline);
        boundsType.applyIfPresent(timeline);
        fontSmoothingType .applyIfPresent(timeline);
        textAlignment.applyIfPresent(timeline);
        textOrigin.applyIfPresent(timeline);
    }

    @Override
    public PropertyBindings propertyBindings(PropertyBindings bindings) {
        return super.propertyBindings(bindings)
                .add("x", NUMBER, this::xProperty)
                .add("y", NUMBER, this::yProperty)
                .add("text", STRING, this::textProperty)
                .add("font", FONT, this::fontProperty)
                .add("lineSpacing", NUMBER, this::lineSpacingProperty)
                .add("wrappingWidth", NUMBER, this::wrappingWidthProperty)
                .add("strike", BOOLEAN, this::strikethroughProperty)
                .add("strikethrough", BOOLEAN, this::strikethroughProperty)
                .add("underline", BOOLEAN, this::underlineProperty)
                .add("boundsType", STRING, this::boundsTypeProperty)
                .add("fontSmoothingType", STRING, this::fontSmoothingTypeProperty)
                .add("halign", STRING, this::textAlignmentProperty)
                .add("textAlignment", STRING, this::textAlignmentProperty)
                .add("valign", STRING, this::textOriginProperty)
                .add("textOrigin", STRING, this::textOriginProperty);
    }

    @Override
    public <R, T> R accept(NodeVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
