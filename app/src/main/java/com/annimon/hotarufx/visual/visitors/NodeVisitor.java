package com.annimon.hotarufx.visual.visitors;

import com.annimon.hotarufx.visual.objects.CircleNode;
import com.annimon.hotarufx.visual.objects.GroupNode;
import com.annimon.hotarufx.visual.objects.RectangleNode;
import com.annimon.hotarufx.visual.objects.SVGPathNode;
import com.annimon.hotarufx.visual.objects.TextNode;

public interface NodeVisitor<R, T> {

    R visit(CircleNode node, T input);
    R visit(GroupNode node, T input);
    R visit(RectangleNode node, T input);
    R visit(SVGPathNode node, T input);
    R visit(TextNode node, T input);
}
