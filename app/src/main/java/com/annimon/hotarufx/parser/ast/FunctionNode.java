package com.annimon.hotarufx.parser.ast;

import java.util.ArrayList;
import java.util.List;

public class FunctionNode extends ASTNode {

    public final Node functionNode;
    public final List<Node> arguments;

    public FunctionNode(Node functionNode) {
        this.functionNode = functionNode;
        arguments = new ArrayList<>();
    }

    public void addArgument(Node arg) {
        arguments.add(arg);
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
