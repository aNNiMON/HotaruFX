package com.annimon.hotarufx.visual;

import com.annimon.hotarufx.lib.FontValue;
import com.annimon.hotarufx.lib.MapValue;
import com.annimon.hotarufx.lib.NodeValue;
import com.annimon.hotarufx.lib.NumberValue;
import com.annimon.hotarufx.lib.StringValue;
import com.annimon.hotarufx.lib.Types;
import com.annimon.hotarufx.lib.Value;
import com.annimon.hotarufx.visual.objects.ObjectNode;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import java.util.function.Function;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("ConstantConditions")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum PropertyType {

    BOOLEAN(Value::asBoolean, o -> NumberValue.fromBoolean(Boolean.TRUE.equals(o))),
    NUMBER(toNumber(), o -> NumberValue.of((Number) o)),
    STRING(Value::asString, o -> new StringValue(String.valueOf(o))),
    NODE(toNode(), fromNode()),
    CLIP_NODE(toClipNode(), fromNode()),
    PAINT(v -> Color.valueOf(v.asString()), o -> new StringValue(o.toString())),
    FONT(toFont(), object -> new FontValue((Font) object));

    private final Function<Value, Object> fromHFX;
    private final Function<Object, Value> toHFX;

    @SuppressWarnings("unchecked")
    public <T> Function<Value, T> getFromHFX() {
        return (Function<Value, T>) fromHFX;
    }

    @SuppressWarnings("unchecked")
    public <T> Function<T, Value> getToHFX() {
        return (Function<T, Value>) toHFX;
    }


    private static Function<Value, Object> toNumber() {
        return value -> {
            if (value.type() == Types.NUMBER) {
                return ((NumberValue) value).raw();
            }
            return value.asDouble();
        };
    }

    private static Function<Value, Object> toNode() {
        return v -> ((NodeValue)v).getNode().getFxNode();
    }

    private static Function<Value, Object> toClipNode() {
        return v -> {
            ObjectNode node = ((NodeValue) v).getNode();
            node.setRenderable(false);
            return node.getFxNode();
        };
    }

    private static Function<Object, Value> fromNode() {
        return object -> new NodeValue(new ObjectNode((Node) object) {
            @Override
            public <R, T> R accept(NodeVisitor<R, T> visitor, T input) {
                return null;
            }
        });
    }

    private static Function<Value, Object> toFont() {
        return value -> {
            if (value.type() == Types.MAP) {
                return FontValue.toFont((MapValue) value);
            }
            return Font.font(value.asDouble());
        };
    }
}
