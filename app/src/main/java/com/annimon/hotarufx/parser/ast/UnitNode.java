package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.parser.visitors.ResultVisitor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnitNode extends ASTNode {

    public enum Unit {MILLISECONDS, SECONDS};

    public final Node value;
    public final Unit operator;

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
