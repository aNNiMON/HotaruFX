package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.parser.visitors.ResultVisitor;
import java.util.List;

public class ArrayNode extends ASTNode {

    public final List<Node> elements;

    public ArrayNode(List<Node> elements) {
        this.elements = elements;
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
