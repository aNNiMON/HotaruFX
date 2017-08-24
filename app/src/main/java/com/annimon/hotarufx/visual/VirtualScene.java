package com.annimon.hotarufx.visual;

import javafx.scene.Group;
import javafx.scene.Node;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VirtualScene {

    @Getter
    private final Group group;

    @Getter
    private final int virtualWidth, virtualHeight;

    public void add(Node node) {
        group.getChildren().add(node);
    }
}
