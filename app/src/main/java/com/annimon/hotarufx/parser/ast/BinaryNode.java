package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.parser.visitors.ResultVisitor;

public class BinaryNode extends ASTNode {

    public enum Operator { ADDITION, SUBTRACTION }

    public final Operator operator;
    public final Node node1;
    public final Node node2;

    public BinaryNode(Operator operator, Node node1, Node node2) {
        this.operator = operator;
        this.node1 = node1;
        this.node2 = node2;
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
