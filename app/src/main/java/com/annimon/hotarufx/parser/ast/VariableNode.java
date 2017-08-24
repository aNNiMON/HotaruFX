package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.lib.Value;
import com.annimon.hotarufx.parser.visitors.ResultVisitor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VariableNode extends ASTNode implements Accessible {

    public final String name;

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }

    @Override
    public <T> Value get(ResultVisitor<Value, T> visitor, T input) {
        return visitor.get(this, input);
    }

    @Override
    public <T> Value set(ResultVisitor<Value, T> visitor, Value value, T input) {
        return visitor.set(this, value, input);
    }
}
