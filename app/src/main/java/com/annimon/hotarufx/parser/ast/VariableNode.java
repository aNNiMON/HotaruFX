package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.lib.Value;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VariableNode extends ASTNode implements Accessible {

    public final String name;

    @Override
    public Value get() {
        return null;
    }

    @Override
    public Value set(Value value) {
        return value;
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
