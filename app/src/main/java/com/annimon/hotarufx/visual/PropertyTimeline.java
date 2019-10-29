package com.annimon.hotarufx.visual;

import java.util.Map;
import java.util.TreeMap;
import javafx.animation.Interpolator;
import javafx.beans.value.WritableValue;

public class PropertyTimeline<T> {

    private final WritableValue<T> property;
    private final Map<KeyFrame, KeyFrameValue<T>> keyFrames;

    public PropertyTimeline(WritableValue<T> property) {
        this.property = property;
        keyFrames = new TreeMap<>();
    }

    public WritableValue<T> getProperty() {
        return property;
    }

    public Map<KeyFrame, KeyFrameValue<T>> getKeyFrames() {
        return keyFrames;
    }

    public PropertyTimeline<T> add(KeyFrame keyFrame, T value) {
        keyFrames.put(keyFrame, new KeyFrameValue<>(value));
        return this;
    }

    public PropertyTimeline<T> add(KeyFrame keyFrame, T value, Interpolator interpolator) {
        keyFrames.put(keyFrame, new KeyFrameValue<>(value, interpolator));
        return this;
    }

    public PropertyTimeline<T> clear() {
        keyFrames.clear();
        return this;
    }
}
