package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.lib.Value;
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
    public Value get() {
        return null;
    }

    @Override
    public Value set(Value value) {
        return null;
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
