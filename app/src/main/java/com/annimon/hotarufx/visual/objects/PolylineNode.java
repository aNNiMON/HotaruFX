package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import java.util.List;
import javafx.scene.shape.Polyline;

public class PolylineNode extends ShapeNode {

    public final Polyline polyline;

    public PolylineNode(List<Double> points) {
        this(new Polyline(), points);
    }

    private PolylineNode(Polyline polyline, List<Double> points) {
        super(polyline);
        this.polyline = polyline;
        polyline.getPoints().addAll(points);
    }

    @Override
    public <R, T> R accept(NodeVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
