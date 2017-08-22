package com.annimon.hotarufx.parser.ast;

public interface ResultVisitor<R, T> {

    R visit(AccessNode node, T t);
    R visit(AssignNode node, T t);
    R visit(BlockNode node, T t);
    R visit(FunctionNode node, T t);
    R visit(MapNode node, T t);
    R visit(UnaryNode node, T t);
    R visit(ValueNode node, T t);
    R visit(VariableNode node, T t);

}
