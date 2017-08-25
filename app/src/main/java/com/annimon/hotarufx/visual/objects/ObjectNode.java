package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;

public abstract class ObjectNode {

    public abstract <R, T> R accept(NodeVisitor<R, T> visitor, T input);

    public void buildTimeline(TimeLine timeline) {
        
    }
}
