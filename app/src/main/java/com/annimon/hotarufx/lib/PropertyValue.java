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
import lombok.Getter;
import lombok.val;

public class PropertyValue implements Value {

    @Getter
    private final Property property;
    private final Map<String, Value> fields;

    public PropertyValue(Property property) {
        this.property = property;
        fields = new HashMap<>();
        fields.put("add", new FunctionValue(add()));
        fields.put("clear", new FunctionValue(clear()));
    }

    @Override
    public int type() {
        return Types.PROPERTY;
    }

    public Value getField(String name) {
        val field = fields.get(name);
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
            val type = property.getType();
            switch (type) {
                case BOOLEAN:
                    ((PropertyTimeline<Boolean>)property.getProperty().get()).add(
                            KeyFrame.of(args[0].asInt()),
                            type.<Boolean>getFromHFX().apply(args[1]),
                            interpolator
                    );
                    break;
                case NUMBER:
                    ((PropertyTimeline<Number>)property.getProperty().get()).add(
                            KeyFrame.of(args[0].asInt()),
                            type.<Number>getFromHFX().apply(args[1]),
                            interpolator
                    );
                    break;
                case STRING:
                    ((PropertyTimeline<String>)property.getProperty().get()).add(
                            KeyFrame.of(args[0].asInt()),
                            type.<String>getFromHFX().apply(args[1]),
                            interpolator
                    );
                    break;
                case NODE:
                case CLIP_NODE:
                    ((PropertyTimeline<Node>)property.getProperty().get()).add(
                            KeyFrame.of(args[0].asInt()),
                            type.<Node>getFromHFX().apply(args[1]),
                            interpolator
                    );
                    break;
                case PAINT:
                    ((PropertyTimeline<Paint>)property.getProperty().get()).add(
                            KeyFrame.of(args[0].asInt()),
                            type.<Paint>getFromHFX().apply(args[1]),
                            interpolator
                    );
                    break;
                case FONT:
                    ((PropertyTimeline<Font>)property.getProperty().get()).add(
                            KeyFrame.of(args[0].asInt()),
                            type.<Font>getFromHFX().apply(args[1]),
                            interpolator
                    );
                    break;
            }
            return this;
        };
    }

    private Function clear() {
        return args -> {
            property.getProperty().get().clear();
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
