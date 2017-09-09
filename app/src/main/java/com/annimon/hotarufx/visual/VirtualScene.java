package com.annimon.hotarufx.visual;

import com.annimon.hotarufx.ui.control.NodesGroup;
import javafx.scene.Node;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VirtualScene {

    @Getter
    private final NodesGroup group;

    @Getter
    private final int virtualWidth, virtualHeight;

    public void add(Node node) {
        group.getChildren().add(node);
    }
}
