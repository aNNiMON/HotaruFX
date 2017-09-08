package com.annimon.hotarufx.lib;

import com.annimon.hotarufx.exceptions.TypeException;
import javafx.animation.Interpolator;
import lombok.Getter;

public class InterpolatorValue implements Value {

    @Getter
    private final Interpolator interpolator;

    public InterpolatorValue(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    @Override
    public int type() {
        return Types.INTERPOLATOR;
    }

    @Override
    public Object raw() {
        return interpolator;
    }

    @Override
    public int asInt() {
        throw new TypeException("Cannot cast interpolator to integer");
    }

    @Override
    public double asNumber() {
        throw new TypeException("Cannot cast interpolator to number");
    }

    @Override
    public String asString() {
        throw new TypeException("Cannot cast interpolator to string");
    }

    @Override
    public int compareTo(Value o) {
        return 0;
    }
}
