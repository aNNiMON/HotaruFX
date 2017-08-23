package com.annimon.hotarufx.parser.visitors;

import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.NumberValue;
import com.annimon.hotarufx.lib.Value;
import com.annimon.hotarufx.parser.ast.*;

public class InterpreterVisitor implements ResultVisitor<Value, Context> {

    @Override
    public Value visit(AccessNode node, Context context) {
        return NumberValue.ZERO;
    }

    @Override
    public Value visit(AssignNode node, Context context) {
        return NumberValue.ZERO;
    }

    @Override
    public Value visit(BlockNode node, Context context) {
        return NumberValue.ZERO;
    }

    @Override
    public Value visit(FunctionNode node, Context context) {
        return NumberValue.ZERO;
    }

    @Override
    public Value visit(MapNode node, Context context) {
        return NumberValue.ZERO;
    }

    @Override
    public Value visit(UnaryNode node, Context context) {
        return NumberValue.ZERO;
    }

    @Override
    public Value visit(ValueNode node, Context context) {
        return NumberValue.ZERO;
    }

    @Override
    public Value visit(VariableNode node, Context context) {
        return NumberValue.ZERO;
    }
}
