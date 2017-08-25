package com.annimon.hotarufx.visual.visitors;

import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.VirtualScene;
import com.annimon.hotarufx.visual.objects.CircleNode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RenderVisitor implements NodeVisitor<Void, VirtualScene> {

    private final TimeLine timeline;

    @Override
    public Void visit(CircleNode node, VirtualScene scene) {
        node.buildTimeline(timeline);
        scene.add(node.circle);
        return null;
    }
}
