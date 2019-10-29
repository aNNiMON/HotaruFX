package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.parser.visitors.ResultVisitor;

public class UnaryNode extends ASTNode {

    public enum Operator { NEGATE };

    public final Operator operator;
    public final Node node;

    public UnaryNode(Operator operator, Node node) {
        this.operator = operator;
        this.node = node;
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
