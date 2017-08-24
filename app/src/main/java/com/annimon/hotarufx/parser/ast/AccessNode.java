package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.lib.Value;
import com.annimon.hotarufx.parser.visitors.ResultVisitor;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccessNode extends ASTNode implements Accessible {

    @Getter
    public final Node root;
    public final List<Node> indices;

    public AccessNode(String variable, List<Node> indices) {
        this(new VariableNode(variable), indices);
    }

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
