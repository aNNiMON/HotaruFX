package com.annimon.hotarufx.visual;

import com.annimon.hotarufx.lib.NumberValue;
import com.annimon.hotarufx.lib.StringValue;
import com.annimon.hotarufx.lib.Types;
import com.annimon.hotarufx.lib.Value;
import java.util.function.Function;
import javafx.scene.paint.Color;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum PropertyType {

    NUMBER(toNumber(), o -> NumberValue.of((Number) o)),
    STRING(Value::asString, o -> new StringValue(String.valueOf(o))),
    PAINT(v -> Color.valueOf(v.asString()), o -> new StringValue(o.toString()));

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
}
