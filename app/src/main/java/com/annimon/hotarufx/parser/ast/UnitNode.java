package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.parser.visitors.ResultVisitor;

public class UnitNode extends ASTNode {

    public enum Unit {MILLISECONDS, SECONDS}

    public final Node value;
    public final Unit operator;

    public UnitNode(Node value, Unit operator) {
        this.value = value;
        this.operator = operator;
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
