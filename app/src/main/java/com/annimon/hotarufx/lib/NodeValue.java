package com.annimon.hotarufx.lib;

import com.annimon.hotarufx.exceptions.TypeException;
import com.annimon.hotarufx.visual.Property;
import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.objects.ObjectNode;
import javafx.scene.paint.Paint;
import lombok.val;

public class NodeValue implements Value {

    private final ObjectNode node;
    private final PropertyBindings bindings;

    public NodeValue(ObjectNode node) {
        this.node = node;
        bindings = node.propertyBindings();
    }

    @Override
    public int type() {
        return Types.NODE;
    }

    public ObjectNode getNode() {
        return node;
    }

    @Override
    public Object raw() {
        return node;
    }

    @SuppressWarnings("unchecked")
    public void fill(MapValue map) {
        map.getMap().forEach((key, value) -> {
            if (!bindings.containsKey(key)) return;
            final Property property = bindings.get(key);
            val timeline = property.getProperty().get();
            val type = property.getType();
            switch (type) {
                case NUMBER:
                    ((PropertyTimeline<Number>) timeline).getProperty().setValue(
                            type.<Number>getFromHFX().apply(value));
                    break;
                case PAINT:
                    ((PropertyTimeline<Paint>) timeline).getProperty().setValue(
                            type.<Paint>getFromHFX().apply(value));
                    break;
            }
        });
    }

    @Override
    public int asInt() {
        throw new TypeException("Cannot cast node to integer");
    }

    @Override
    public double asNumber() {
        throw new TypeException("Cannot cast node to number");
    }

    @Override
    public String asString() {
        throw new TypeException("Cannot cast node to string");
    }

    @Override
    public int compareTo(Value o) {
        return 0;
    }
}
