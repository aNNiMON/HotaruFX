package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.parser.visitors.ResultVisitor;

public class AssignNode extends ASTNode {

    public final Accessible target;
    public final Node value;

    public AssignNode(Accessible target, Node value) {
        this.target = target;
        this.value = value;
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
