package com.annimon.hotarufx.parser.ast;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AssignNode extends ASTNode {

    public final Accessible target;
    public final Node value;

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
