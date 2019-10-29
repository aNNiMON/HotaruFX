package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.parser.visitors.ResultVisitor;

public class PropertyNode extends ASTNode {

    public final Node node;
    public final String property;

    public PropertyNode(Node node, String property) {
        this.node = node;
        this.property = property;
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
