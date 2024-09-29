package com.annimon.hotarufx.ui.control;

import javafx.scene.Group;

public class NodesGroup extends Group {

    private final double width;
    private final double height;

    public NodesGroup(double width, double height) {
        this.width = width;
        this.height = height;
        setAutoSizeChildren(false);
        setManaged(false);
    }

    @Override
    public double prefWidth(double unused) {
        return width;
    }

    @Override
    public double prefHeight(double unused) {
        return height;
    }

    @Override
    protected double computePrefWidth(double unused) {
        return width;
    }

    @Override
    protected double computePrefHeight(double unused) {
        return height;
    }

    @Override
    public boolean isResizable() {
        return false;
    }
}
