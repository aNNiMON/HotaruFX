package com.annimon.hotarufx.visual.visitors;

import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.VirtualScene;
import com.annimon.hotarufx.visual.objects.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RenderVisitor implements NodeVisitor<Void, VirtualScene> {

    private final TimeLine timeline;

    @Override
    public Void visit(ArcNode node, VirtualScene scene) {
        return render(node, scene);
    }

    @Override
    public Void visit(CircleNode node, VirtualScene scene) {
        return render(node, scene);
    }

    @Override
    public Void visit(EllipseNode node, VirtualScene scene) {
        return render(node, scene);
    }

    @Override
    public Void visit(GroupNode group, VirtualScene scene) {
        render(group, scene);
        for (ObjectNode node : group.nodes) {
            node.setRenderable(false);
            node.accept(this, scene);
        }
        return null;
    }

    @Override
    public Void visit(LineNode node, VirtualScene scene) {
        return render(node, scene);
    }

    @Override
    public Void visit(PolygonNode node, VirtualScene scene) {
        return render(node, scene);
    }

    @Override
    public Void visit(PolylineNode node, VirtualScene scene) {
        return render(node, scene);
    }

    @Override
    public Void visit(RectangleNode node, VirtualScene scene) {
        return render(node, scene);
    }

    @Override
    public Void visit(SVGPathNode node, VirtualScene scene) {
        return render(node, scene);
    }

    @Override
    public Void visit(TextNode node, VirtualScene scene) {
        return render(node, scene);
    }

    private Void render(ObjectNode node, VirtualScene scene) {
        node.buildTimeline(timeline);
        if (node.isRenderable()) {
            scene.add(node.getFxNode());
        }
        return null;
    }
}
