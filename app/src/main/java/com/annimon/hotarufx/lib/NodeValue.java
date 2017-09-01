package com.annimon.hotarufx.lib;

import com.annimon.hotarufx.exceptions.HotaruRuntimeException;
import com.annimon.hotarufx.exceptions.TypeException;
import com.annimon.hotarufx.visual.Property;
import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.objects.ObjectNode;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
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

    public void fill(MapValue map) {
        map.getMap().forEach(this::set);
    }

    @SuppressWarnings("unchecked")
    public Value get(String key) {
        if (!bindings.containsKey(key)) {
            throw new HotaruRuntimeException("Unable to get property " + key + " from node value");
        }
        final Property property = bindings.get(key);
        val timeline = property.getProperty().get();
        val type = property.getType();
        switch (type) {
            case NUMBER:
                return type.<Number>getToHFX().apply(
                        ((PropertyTimeline<Number>) timeline).getProperty().getValue());
            case STRING:
                return type.<String>getToHFX().apply(
                        ((PropertyTimeline<String>) timeline).getProperty().getValue());
            case NODE:
            case CLIP_NODE:
                return type.<Node>getToHFX().apply(
                        ((PropertyTimeline<Node>) timeline).getProperty().getValue());
            case PAINT:
                return type.<Paint>getToHFX().apply(
                        ((PropertyTimeline<Paint>) timeline).getProperty().getValue());
            case FONT:
                return type.<Font>getToHFX().apply(
                        ((PropertyTimeline<Font>) timeline).getProperty().getValue());
            default:
                throw new TypeException("Unknown type of node property");
        }
    }

    @SuppressWarnings("unchecked")
    public void set(String key, Value value) {
        if (!bindings.containsKey(key)) return;
        final Property property = bindings.get(key);
        val timeline = property.getProperty().get();
        val type = property.getType();
        switch (type) {
            case NUMBER:
                ((PropertyTimeline<Number>) timeline).getProperty().setValue(
                        type.<Number>getFromHFX().apply(value));
                break;
            case STRING:
                ((PropertyTimeline<String>) timeline).getProperty().setValue(
                        type.<String>getFromHFX().apply(value));
                break;
            case PAINT:
                ((PropertyTimeline<Paint>) timeline).getProperty().setValue(
                        type.<Paint>getFromHFX().apply(value));
                break;
            case NODE:
            case CLIP_NODE:
                ((PropertyTimeline<Node>) timeline).getProperty().setValue(
                        type.<Node>getFromHFX().apply(value));
                break;
            case FONT:
                ((PropertyTimeline<Font>) timeline).getProperty().setValue(
                        type.<Font>getFromHFX().apply(value));
                break;
        }
    }

    public Value getProperty(String key) {
        val property = bindings.get(key);
        if (property == null) {
            throw new HotaruRuntimeException("Unable to get property " + key + " from node value");
        }
        return new PropertyValue(property);
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
