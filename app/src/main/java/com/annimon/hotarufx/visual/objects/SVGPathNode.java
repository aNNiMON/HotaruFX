package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.SVGPath;
import static com.annimon.hotarufx.visual.PropertyType.STRING;

public class SVGPathNode extends ShapeNode {

    public final SVGPath svgPath;

    private PropertyTimelineHolder<String> content;
    private PropertyTimelineHolder<String> fillRule;

    public SVGPathNode() {
        this(new SVGPath());
    }

    private SVGPathNode(SVGPath svgPath) {
        super(svgPath);
        this.svgPath = svgPath;
        content = PropertyTimelineHolder.empty();
        fillRule = PropertyTimelineHolder.empty();
    }

    public PropertyTimeline<String> contentProperty() {
        return content.setIfEmptyThenGet(svgPath::contentProperty);
    }

    public PropertyTimeline<String> fillRuleProperty() {
        return fillRule.setIfEmptyThenGet(enumToString(FillRule.class, svgPath.fillRuleProperty()));
    }

    @Override
    public void buildTimeline(TimeLine timeline) {
        super.buildTimeline(timeline);
        content.applyIfPresent(timeline);
        fillRule.applyIfPresent(timeline);
    }

    @Override
    public PropertyBindings propertyBindings(PropertyBindings bindings) {
        return super.propertyBindings(bindings)
                .add("content", STRING, this::contentProperty)
                .add("fillRule", STRING, this::fillRuleProperty);
    }

    @Override
    public <R, T> R accept(NodeVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
