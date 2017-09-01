package com.annimon.hotarufx.visual.visitors;

import com.annimon.hotarufx.visual.objects.CircleNode;
import com.annimon.hotarufx.visual.objects.RectangleNode;

public interface NodeVisitor<R, T> {

    R visit(CircleNode node, T input);
    R visit(RectangleNode node, T input);
}
