package com.annimon.hotarufx.visual;

import com.annimon.hotarufx.ui.control.NodesGroup;
import javafx.scene.Node;

public class VirtualScene {

    private final NodesGroup group;

    private final int virtualWidth, virtualHeight;

    public VirtualScene(NodesGroup group, int virtualWidth, int virtualHeight) {
        this.group = group;
        this.virtualWidth = virtualWidth;
        this.virtualHeight = virtualHeight;
    }

    public NodesGroup getGroup() {
        return group;
    }

    public int getVirtualWidth() {
        return virtualWidth;
    }

    public int getVirtualHeight() {
        return virtualHeight;
    }

    public void add(Node node) {
        group.getChildren().add(node);
    }
}
