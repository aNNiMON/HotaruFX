package com.annimon.hotarufx.visual;

import com.annimon.hotarufx.ui.control.NodesGroup;
import javafx.scene.Node;

public record VirtualScene(
        NodesGroup group,
        int virtualWidth, int virtualHeight) {

    public void add(Node node) {
        group.getChildren().add(node);
    }
}
