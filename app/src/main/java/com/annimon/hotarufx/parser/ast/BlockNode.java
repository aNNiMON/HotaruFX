package com.annimon.hotarufx.parser.ast;

import java.util.ArrayList;
import java.util.List;

public class BlockNode extends ASTNode {

    public final List<Node> statements = new ArrayList<>();

    public void add(Node node) {
        statements.add(node);
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T t) {
        return visitor.visit(this, t);
    }
}
