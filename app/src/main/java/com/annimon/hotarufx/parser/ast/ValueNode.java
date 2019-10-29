package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.lib.NumberValue;
import com.annimon.hotarufx.lib.StringValue;
import com.annimon.hotarufx.lib.Value;
import com.annimon.hotarufx.parser.visitors.ResultVisitor;

public class ValueNode extends ASTNode {

    public final Value value;

    public ValueNode(Value value) {
        this.value = value;
    }

    public ValueNode(Number value) {
        this(NumberValue.of(value));
    }

    public ValueNode(String value) {
        this(new StringValue(value));
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
