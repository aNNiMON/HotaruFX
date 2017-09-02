package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import java.util.List;
import javafx.scene.shape.Polygon;

public class PolygonNode extends ShapeNode {

    public final Polygon polygon;

    public PolygonNode(List<Double> points) {
        this(new Polygon(), points);
    }

    private PolygonNode(Polygon polygon, List<Double> points) {
        super(polygon);
        this.polygon = polygon;
        polygon.getPoints().addAll(points);
    }

    @Override
    public <R, T> R accept(NodeVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
