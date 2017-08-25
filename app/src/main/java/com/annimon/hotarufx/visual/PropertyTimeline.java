package com.annimon.hotarufx.visual;

import com.annimon.hotarufx.exceptions.KeyFrameDuplicationException;
import java.util.Map;
import java.util.TreeMap;
import javafx.beans.value.WritableValue;
import lombok.Getter;
import lombok.val;

@Getter
public class PropertyTimeline<T> {

    private final WritableValue<T> property;
    private final Map<KeyFrame, T> keyFrames;

    public PropertyTimeline(WritableValue<T> property) {
        this.property = property;
        keyFrames = new TreeMap<>();
    }

    public PropertyTimeline<T> add(KeyFrame keyFrame, T value) {
        val previous = keyFrames.put(keyFrame, value);
        if (previous != null) {
            throw new KeyFrameDuplicationException(keyFrame);
        }
        return this;
    }
}
