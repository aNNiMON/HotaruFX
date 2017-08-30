package com.annimon.hotarufx.visual;

import com.annimon.hotarufx.lib.NumberValue;
import com.annimon.hotarufx.lib.StringValue;
import com.annimon.hotarufx.lib.Types;
import com.annimon.hotarufx.lib.Value;
import java.util.function.Function;
import javafx.scene.paint.Color;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public enum PropertyType {

    NUMBER(toNumber(), o -> NumberValue.of((Number) o)),
    PAINT(v -> Color.valueOf(v.asString()), o -> new StringValue(o.toString()));

    private final Function<Value, Object> fromHFX;
    private final Function<Object, Value> toHFX;

    private static Function<Value, Object> toNumber() {
        return value -> {
            if (value.type() == Types.NUMBER) {
                return ((NumberValue) value).raw();
            }
            return value.asNumber();
        };
    }
}
