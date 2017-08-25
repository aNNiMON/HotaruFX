package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import javafx.scene.Node;

public abstract class ObjectNode {

    private final Node node;

    public ObjectNode(Node node) {
        this.node = node;
    }

    public void buildTimeline(TimeLine timeline) {

    }

    public abstract <R, T> R accept(NodeVisitor<R, T> visitor, T input);
}
