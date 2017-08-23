package com.annimon.hotarufx.parser.visitors;

import com.annimon.hotarufx.exceptions.FunctionNotFoundException;
import com.annimon.hotarufx.exceptions.TypeException;
import com.annimon.hotarufx.exceptions.VariableNotFoundException;
import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.Function;
import com.annimon.hotarufx.lib.MapValue;
import com.annimon.hotarufx.lib.NumberValue;
import com.annimon.hotarufx.lib.StringValue;
import com.annimon.hotarufx.lib.Types;
import com.annimon.hotarufx.lib.Value;
import com.annimon.hotarufx.parser.ast.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.val;

public class InterpreterVisitor implements ResultVisitor<Value, Context> {

    @Override
    public Value visit(AccessNode node, Context context) {
        return node.get(this, context);
    }

    @Override
    public Value visit(AssignNode node, Context context) {
        val value = node.value.accept(this, context);
        return node.target.set(this, value, context);
    }

    @Override
    public Value visit(BlockNode node, Context context) {
        Value last = NumberValue.ZERO;
        for (Node statement : node.statements) {
            last = statement.accept(this, context);
        }
        return last;
    }

    @Override
    public Value visit(FunctionNode node, Context context) {
        val functionName = node.functionNode.accept(this, context).asString();
        val function = context.functions().get(functionName);
        if (function == null)
            throw new FunctionNotFoundException(functionName, node.start(), node.end());
        val args = node.arguments.stream()
                .map(n -> n.accept(this, context))
                .toArray(Value[]::new);
        return function.execute(args);
    }

    @Override
    public Value visit(MapNode node, Context context) {
        Map<String, Value> map = new HashMap<>(node.elements.size());
        for (Map.Entry<String, Node> entry : node.elements.entrySet()) {
            map.put(entry.getKey(), entry.getValue().accept(this, context));
        }
        return new MapValue(map);
    }

    @Override
    public Value visit(UnaryNode node, Context context) {
        switch (node.operator) {
            case NEGATE:
                val value = node.node.accept(this, context);
                if (value.type() == Types.STRING) {
                    final StringBuilder sb = new StringBuilder(value.asString());
                    return new StringValue(sb.reverse().toString());
                }
                if (value.type() == Types.NUMBER) {
                    final Number number = (Number) value.raw();
                    if (number instanceof Double) {
                        return NumberValue.of(-number.doubleValue());
                    }
                    if (number instanceof Float) {
                        return NumberValue.of(-number.floatValue());
                    }
                    if (number instanceof Long) {
                        return NumberValue.of(-number.longValue());
                    }
                }
                return NumberValue.of(-value.asInt());

            default:
                return NumberValue.ZERO;
        }
    }

    @Override
    public Value visit(ValueNode node, Context context) {
        return node.value;
    }

    @Override
    public Value visit(VariableNode node, Context context) {
        return node.get(this, context);
    }

    @Override
    public Value get(AccessNode node, Context context) {
        Value container = node.root.accept(this, context);
        return getContainer(node.indices, context, container)
                .orElseThrow(() -> new TypeException("Map expected", node.start(), node.end()));
    }

    @Override
    public Value set(AccessNode node, Value value, Context context) {
        Value container = node.root.accept(this, context);
        int lastIndex = node.indices.size() - 1;
        Supplier<TypeException> exceptionSupplier = () ->
                new TypeException("Map expected", node.start(), node.end());
        container = getContainer(node.indices.subList(0, lastIndex), context, container)
                .orElseThrow(exceptionSupplier);
        switch (container.type()) {
            case Types.MAP:
                val key = node.indices.get(lastIndex).accept(this, context).asString();
                ((MapValue) container).getMap().put(key, value);
                break;
            default:
                throw exceptionSupplier.get();
        }
        return value;
    }

    private Optional<Value> getContainer(List<Node> nodes, Context context, Value container) {
        for (Node index : nodes) {
            switch (container.type()) {
                case Types.MAP:
                    val key = index.accept(this, context).asString();
                    container = ((MapValue) container).getMap().get(key);
                    break;
                default:
                    return Optional.empty();
            }
        }
        return Optional.of(container);
    }

    @Override
    public Value get(VariableNode node, Context context) {
        val result = context.variables().get(node.name);
        if (result == null)
            throw new VariableNotFoundException(node.name, node.start(), node.end());
        return result;
    }

    @Override
    public Value set(VariableNode node, Value value, Context context) {
        context.variables().put(node.name, value);
        return value;
    }
}
