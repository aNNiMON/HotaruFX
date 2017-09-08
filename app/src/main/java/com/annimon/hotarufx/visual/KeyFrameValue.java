package com.annimon.hotarufx.visual;

import javafx.animation.Interpolator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KeyFrameValue<T> {

    private final T value;
    private final Interpolator interpolator;

    public KeyFrameValue(T value) {
        this(value, Interpolator.LINEAR);
    }
}
