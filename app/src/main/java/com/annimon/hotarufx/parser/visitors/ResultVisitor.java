package com.annimon.hotarufx.parser.visitors;

import com.annimon.hotarufx.lib.Value;
import com.annimon.hotarufx.parser.ast.*;

public interface ResultVisitor<R, T> {

    R visit(AccessNode node, T t);
    R visit(ArrayNode node, T t);
    R visit(AssignNode node, T t);
    R visit(BlockNode node, T t);
    R visit(FunctionNode node, T t);
    R visit(MapNode node, T t);
    R visit(PropertyNode node, T t);
    R visit(UnaryNode node, T t);
    R visit(ValueNode node, T t);
    R visit(VariableNode node, T t);

    Value get(AccessNode node, T t);
    Value set(AccessNode node, Value value, T t);
    Value get(VariableNode node, T t);
    Value set(VariableNode node, Value value, T t);
}
