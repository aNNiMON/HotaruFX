package com.annimon.hotarufx.ui.control;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;

public class NodesGroup extends Group {

    private final double width, height;

    public NodesGroup(double width, double height) {
        this.width = width;
        this.height = height;
        setAutoSizeChildren(false);
        setManaged(false);
    }

    @Override
    public ObservableList<Node> getChildren() {
        return super.getChildren();
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
