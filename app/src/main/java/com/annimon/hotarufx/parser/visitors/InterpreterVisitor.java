package com.annimon.hotarufx.parser.visitors;

import com.annimon.hotarufx.exceptions.FunctionNotFoundException;
import com.annimon.hotarufx.exceptions.TypeException;
import com.annimon.hotarufx.exceptions.VariableNotFoundException;
import com.annimon.hotarufx.lib.*;
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
    public Value visit(ArrayNode node, Context context) {
        val elements = node.elements.stream()
                .map(el -> el.accept(this, context))
                .toArray(Value[]::new);
        return new ArrayValue(elements);
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
        val value = node.functionNode.accept(this, context);
        final Function function;
        switch (value.type()) {
            case Types.FUNCTION:
                function = ((FunctionValue) value).getValue();
                break;
            default:
                val functionName = value.asString();
                function = context.functions().get(functionName);
                if (function == null)
                    throw new FunctionNotFoundException(functionName, node.start(), node.end());
                break;
        }
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
    public Value visit(PropertyNode node, Context context) {
        val value = node.node.accept(this, context);
        if (value.type() != Types.NODE) {
            throw new TypeException("Node value expected");
        }
        val nodeValue = (NodeValue) value;
        return nodeValue.getProperty(node.property);
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
    public Value visit(UnitNode node, Context context) {
        val value = node.value.accept(this, context);
        val frameRate = context.composition().getTimeline().getFrameRate();
        final double frame;
        switch (node.operator) {
            case SECONDS:
                frame = value.asNumber() * frameRate;
                break;

            case MILLISECONDS:
            default:
                frame = (value.asNumber() * frameRate) / 1000d;
                break;
        }
        return NumberValue.of(frame);
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
                .orElseThrow(() -> new TypeException("Unable to get property from non-accessible type", node.start(), node.end()));
    }

    @Override
    public Value set(AccessNode node, Value value, Context context) {
        Value container = node.root.accept(this, context);
        int lastIndex = node.indices.size() - 1;
        Supplier<TypeException> exceptionSupplier = () ->
                new TypeException("Unable to set property to non-accessible type", node.start(), node.end());
        container = getContainer(node.indices.subList(0, lastIndex), context, container)
                .orElseThrow(exceptionSupplier);
        switch (container.type()) {
            case Types.MAP: {
                val key = node.indices.get(lastIndex).accept(this, context).asString();
                ((MapValue) container).getMap().put(key, value);
            } break;

            case Types.NODE: {
                val key = node.indices.get(lastIndex).accept(this, context).asString();
                ((NodeValue) container).set(key, value);
            } break;

            default:
                throw exceptionSupplier.get();
        }
        return value;
    }

    private Optional<Value> getContainer(List<Node> nodes, Context context, Value container) {
        for (Node index : nodes) {
            switch (container.type()) {
                case Types.MAP: {
                    val key = index.accept(this, context).asString();
                    container = ((MapValue) container).getMap().get(key);
                } break;

                case Types.NODE: {
                    val key = index.accept(this, context).asString();
                    container = ((NodeValue) container).get(key);
                } break;

                case Types.PROPERTY: {
                    val key = index.accept(this, context).asString();
                    val propertyValue = (PropertyValue) container;
                    container = propertyValue.getField(key);
                } break;

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
