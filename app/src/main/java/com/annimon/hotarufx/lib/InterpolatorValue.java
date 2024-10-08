package com.annimon.hotarufx.lib;

import com.annimon.hotarufx.exceptions.TypeException;
import javafx.animation.Interpolator;

public class InterpolatorValue implements Value {

    private final Interpolator interpolator;

    public InterpolatorValue(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public Interpolator getInterpolator() {
        return interpolator;
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
    public Number asNumber() {
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

    @Override
    public String toString() {
        return interpolator.toString();
    }
}
