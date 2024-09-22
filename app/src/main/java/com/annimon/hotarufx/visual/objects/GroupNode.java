package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import static com.annimon.hotarufx.visual.PropertyType.BOOLEAN;

public class GroupNode extends ObjectNode {

    public final Group group;
    public final List<ObjectNode> nodes;

    private final PropertyTimelineHolder<Boolean> autoSizeChildren;

    public GroupNode(List<ObjectNode> nodes) {
        this(new Group(), nodes);
    }

    private GroupNode(Group group, List<ObjectNode> nodes) {
        super(group);
        this.group = group;
        this.nodes = new ArrayList<>(nodes);
        final var fxNodes = nodes.stream()
                .map(ObjectNode::getFxNode)
                .toList();
        group.getChildren().addAll(fxNodes);
        autoSizeChildren = PropertyTimelineHolder.empty();
    }

    public PropertyTimeline<Boolean> autoSizeChildrenProperty() {
        return autoSizeChildren.setIfEmptyThenGet(group::autoSizeChildrenProperty);
    }

    @Override
    public void buildTimeline(TimeLine timeline) {
        super.buildTimeline(timeline);
        autoSizeChildren.applyIfPresent(timeline);
        for (ObjectNode node : nodes) {
            node.buildTimeline(timeline);
        }

    }

    @Override
    public PropertyBindings propertyBindings(PropertyBindings bindings) {
        return super.propertyBindings(bindings)
                .add("autoSize", BOOLEAN, this::autoSizeChildrenProperty)
                .add("autoSizeChildren", BOOLEAN, this::autoSizeChildrenProperty);
    }

    @Override
    public <R, T> R accept(NodeVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
