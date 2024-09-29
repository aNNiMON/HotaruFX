package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import java.util.ArrayList;
import java.util.List;
import static com.annimon.hotarufx.visual.PropertyType.NUMBER;
import static com.annimon.hotarufx.visual.PropertyType.STRING;

public class TextFlowNode extends RegionNode {

    public final BorderPane pane;
    public final TextFlow textFlow;
    public final List<ObjectNode> nodes;

    private final PropertyTimelineHolder<Number> lineSpacing;
    private final PropertyTimelineHolder<Number> tabSize;
    private final PropertyTimelineHolder<String> textAlignmentProperty;

    public TextFlowNode(List<ObjectNode> nodes) {
        this(new TextFlow(), nodes);
    }

    private TextFlowNode(TextFlow textFlow, List<ObjectNode> nodes) {
        super(textFlow);
        this.pane = new BorderPane(textFlow);
        this.textFlow = textFlow;
        this.nodes = new ArrayList<>(nodes);
        final var fxNodes = nodes.stream()
                .map(ObjectNode::getFxNode)
                .toList();
        textFlow.getChildren().addAll(fxNodes);
        tabSize = PropertyTimelineHolder.empty();
        lineSpacing = PropertyTimelineHolder.empty();
        textAlignmentProperty = PropertyTimelineHolder.empty();
    }

    public PropertyTimeline<Number> lineSpacingProperty() {
        return lineSpacing.setIfEmptyThenGet(textFlow::lineSpacingProperty);
    }

    public PropertyTimeline<Number> tabSizeProperty() {
        return tabSize.setIfEmptyThenGet(textFlow::tabSizeProperty);
    }

    public PropertyTimeline<String> textAlignmentProperty() {
        return textAlignmentProperty.setIfEmptyThenGet(
                enumToString(TextAlignment.class, textFlow.textAlignmentProperty()));
    }

    @Override
    public void buildTimeline(TimeLine timeline) {
        super.buildTimeline(timeline);
        lineSpacing.applyIfPresent(timeline);
        tabSize.applyIfPresent(timeline);
        textAlignmentProperty.applyIfPresent(timeline);
        for (ObjectNode node : nodes) {
            node.buildTimeline(timeline);
        }
    }

    @Override
    public PropertyBindings propertyBindings(PropertyBindings bindings) {
        return super.propertyBindings(bindings)
                .add("lineSpacing", NUMBER, this::lineSpacingProperty)
                .add("tabSize", NUMBER, this::tabSizeProperty)
                .add("halign", STRING, this::textAlignmentProperty)
                .add("textAlignment", STRING, this::textAlignmentProperty);
    }

    @Override
    public Node getFxNode() {
        return pane;
    }

    @Override
    public <R, T> R accept(NodeVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
