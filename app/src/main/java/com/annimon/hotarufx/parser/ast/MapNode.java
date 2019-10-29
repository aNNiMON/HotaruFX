package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.parser.visitors.ResultVisitor;
import java.util.Map;

public class MapNode extends ASTNode {

    public final Map<String, Node> elements;

    public MapNode(Map<String, Node> elements) {
        this.elements = elements;
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
