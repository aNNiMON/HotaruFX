package com.annimon.hotarufx.visual;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import lombok.Getter;

public class Composition {

    @Getter
    private final Group group;

    @Getter
    private final Scene scene;

    @Getter
    private final int width, height;

    public Composition(int width, int height, Paint background, Group group) {
        this.width = width;
        this.height = height;
        this.group = group;
        this.scene = new Scene(group, width, height, background);
    }

    public void add(Node node) {
        group.getChildren().add(node);
    }

}
