package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.parser.visitors.ResultVisitor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnaryNode extends ASTNode {

    public enum Operator { NEGATE };

    public final Operator operator;
    public final Node node;

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
