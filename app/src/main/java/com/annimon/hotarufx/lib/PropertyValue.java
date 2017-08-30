package com.annimon.hotarufx.lib;

import com.annimon.hotarufx.exceptions.HotaruRuntimeException;
import com.annimon.hotarufx.exceptions.TypeException;
import com.annimon.hotarufx.visual.KeyFrame;
import com.annimon.hotarufx.visual.Property;
import com.annimon.hotarufx.visual.PropertyTimeline;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Paint;
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
            Validator.with(args).checkAtLeast(2);
            val type = property.getType();
            switch (type) {
                case NUMBER:
                    ((PropertyTimeline<Number>)property.getProperty().get()).add(
                            KeyFrame.of(args[0].asInt()),
                            type.<Number>getFromHFX().apply(args[1])
                    );
                    break;
                case PAINT:
                    ((PropertyTimeline<Paint>)property.getProperty().get()).add(
                            KeyFrame.of(args[0].asInt()),
                            type.<Paint>getFromHFX().apply(args[1])
                    );
                    break;
            }
            return this;
        };
    }

    @Override
    public Object raw() {
        return property;
    }

    @Override
    public int asInt() {
        throw new TypeException("Cannot cast property to integer");
    }

    @Override
    public double asNumber() {
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
