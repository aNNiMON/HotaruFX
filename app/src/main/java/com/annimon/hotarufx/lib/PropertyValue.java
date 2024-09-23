package com.annimon.hotarufx.lib;

import com.annimon.hotarufx.exceptions.HotaruRuntimeException;
import com.annimon.hotarufx.exceptions.TypeException;
import com.annimon.hotarufx.visual.KeyFrame;
import com.annimon.hotarufx.visual.Property;
import com.annimon.hotarufx.visual.PropertyTimeline;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.Interpolator;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class PropertyValue implements Value {

    private final Property property;
    private final Map<String, Value> fields;

    public PropertyValue(Property property) {
        this.property = property;
        fields = new HashMap<>();
        fields.put("add", new FunctionValue(add()));
        fields.put("clear", new FunctionValue(clear()));
    }

    public Property getProperty() {
        return property;
    }

    @Override
    public int type() {
        return Types.PROPERTY;
    }

    public Value getField(String name) {
        final var field = fields.get(name);
        if (field == null) {
            throw new HotaruRuntimeException("PropertyValue does not have " + name + " field");
        }
        return field;
    }

    @SuppressWarnings("unchecked")
    private Function add() {
        return args -> {
            Validator.with(args).checkOrOr(2, 3);
            final Interpolator interpolator;
            if (args.length == 2) {
                interpolator = Interpolator.LINEAR;
            } else {
                if (args[2].type() != Types.INTERPOLATOR) {
                    throw new TypeException("Interpolator required at third argument");
                }
                interpolator = ((InterpolatorValue) args[2]).getInterpolator();
            }
            final var type = property.type();
            switch (type) {
                case BOOLEAN -> ((PropertyTimeline<Boolean>) property.property().get()).add(
                        KeyFrame.of(args[0].asInt()),
                        type.<Boolean>getFromHFX().apply(args[1]),
                        interpolator
                );
                case NUMBER -> ((PropertyTimeline<Number>) property.property().get()).add(
                        KeyFrame.of(args[0].asInt()),
                        type.<Number>getFromHFX().apply(args[1]),
                        interpolator
                );
                case STRING -> ((PropertyTimeline<String>) property.property().get()).add(
                        KeyFrame.of(args[0].asInt()),
                        type.<String>getFromHFX().apply(args[1]),
                        interpolator
                );
                case NODE, CLIP_NODE -> ((PropertyTimeline<Node>) property.property().get()).add(
                        KeyFrame.of(args[0].asInt()),
                        type.<Node>getFromHFX().apply(args[1]),
                        interpolator
                );
                case PAINT -> ((PropertyTimeline<Paint>) property.property().get()).add(
                        KeyFrame.of(args[0].asInt()),
                        type.<Paint>getFromHFX().apply(args[1]),
                        interpolator
                );
                case FONT -> ((PropertyTimeline<Font>) property.property().get()).add(
                        KeyFrame.of(args[0].asInt()),
                        type.<Font>getFromHFX().apply(args[1]),
                        interpolator
                );
            }
            return this;
        };
    }

    private Function clear() {
        return args -> {
            property.property().get().clear();
            return this;
        };
    }

    @Override
    public Object raw() {
        return property;
    }

    @Override
    public Number asNumber() {
        throw new TypeException("Cannot cast property to number");
    }

    @Override
    public String asString() {
        throw new TypeException("Cannot cast property to string");
    }

    @Override
    public int compareTo(Value o) {
        return 0;
    }
}
