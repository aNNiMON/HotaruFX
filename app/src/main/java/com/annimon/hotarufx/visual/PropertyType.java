package com.annimon.hotarufx.visual;

import com.annimon.hotarufx.lib.MapValue;
import com.annimon.hotarufx.lib.NodeValue;
import com.annimon.hotarufx.lib.NumberValue;
import com.annimon.hotarufx.lib.StringValue;
import com.annimon.hotarufx.lib.Types;
import com.annimon.hotarufx.lib.Value;
import com.annimon.hotarufx.visual.objects.ObjectNode;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import java.util.HashMap;
import java.util.function.Function;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;

@SuppressWarnings("ConstantConditions")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum PropertyType {

    BOOLEAN(v -> v.asInt() != 0, o -> NumberValue.fromBoolean(Boolean.TRUE.equals(o))),
    NUMBER(toNumber(), o -> NumberValue.of((Number) o)),
    STRING(Value::asString, o -> new StringValue(String.valueOf(o))),
    NODE(toNode(), fromNode()),
    CLIP_NODE(toClipNode(), fromNode()),
    PAINT(v -> Color.valueOf(v.asString()), o -> new StringValue(o.toString())),
    FONT(toFont(), fromFont());

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
            return value.asNumber();
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
            // TODO: FontValue + FontBundle
            if (value.type() == Types.MAP) {
                val map = ((MapValue) value).getMap();
                val family = map.getOrDefault("family", new StringValue(Font.getDefault().getFamily())).asString();
                val weight = map.getOrDefault("weight", NumberValue.of(FontWeight.NORMAL.getWeight())).asInt();
                val isItalic = map.getOrDefault("italic", NumberValue.ZERO).asInt() != 0;
                val posture = isItalic ? FontPosture.ITALIC : FontPosture.REGULAR;
                val size = map.getOrDefault("size", NumberValue.MINUS_ONE).asNumber();
                return Font.font(family, FontWeight.findByWeight(weight), posture, size);
            }
            return Font.font(value.asNumber());
        };
    }

    private static Function<Object, Value> fromFont() {
        return object -> {
            val font = (Font) object;
            val map = new HashMap<String, Value>(4);
            map.put("family", new StringValue(font.getFamily()));
            map.put("isItalic", NumberValue.fromBoolean(font.getStyle().toLowerCase().contains("italic")));
            val weight = FontWeight.findByName(font.getStyle());
            map.put("weight", NumberValue.of(weight != null
                    ? (weight.getWeight())
                    : FontWeight.NORMAL.getWeight()));
            map.put("size", NumberValue.of(font.getSize()));
            return new MapValue(map);
        };
    }
}
