package com.annimon.hotarufx.visual;

import javafx.animation.Interpolator;

public class KeyFrameValue<T> {

    private final T value;
    private final Interpolator interpolator;

    public KeyFrameValue(T value, Interpolator interpolator) {
        this.value = value;
        this.interpolator = interpolator;
    }

    public KeyFrameValue(T value) {
        this(value, Interpolator.LINEAR);
    }

    public T getValue() {
        return value;
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }
}
