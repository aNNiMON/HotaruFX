package com.annimon.hotarufx.visual.visitors;

import com.annimon.hotarufx.visual.objects.*;

public interface NodeVisitor<R, T> {

    R visit(ArcNode node, T input);
    R visit(CircleNode node, T input);
    R visit(EllipseNode node, T input);
    R visit(GroupNode node, T input);
    R visit(LineNode node, T input);
    R visit(PolygonNode node, T input);
    R visit(PolylineNode node, T input);
    R visit(RectangleNode node, T input);
    R visit(SVGPathNode node, T input);
    R visit(TextNode node, T input);
}
