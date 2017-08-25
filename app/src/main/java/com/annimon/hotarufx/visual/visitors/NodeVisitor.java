package com.annimon.hotarufx.visual.visitors;

import com.annimon.hotarufx.visual.objects.CircleNode;

public interface NodeVisitor<R, T> {

    R visit(CircleNode node, T input);
}
