package com.annimon.hotarufx.visual;

import java.util.Map;
import java.util.TreeMap;
import javafx.beans.value.WritableValue;
import lombok.Getter;

@Getter
public class PropertyTimeline<T> {

    private final WritableValue<T> property;
    private final Map<KeyFrame, T> keyFrames;

    public PropertyTimeline(WritableValue<T> property) {
        this.property = property;
        keyFrames = new TreeMap<>();
    }

    public PropertyTimeline<T> add(KeyFrame keyFrame, T value) {
        keyFrames.put(keyFrame, value);
        return this;
    }
}
