package com.annimon.hotarufx.visual;

import com.annimon.hotarufx.exceptions.KeyFrameDuplicationException;
import java.util.Map;
import java.util.TreeMap;
import lombok.Getter;
import lombok.val;

public class TimeLine {

    @Getter
    private final Map<KeyFrame, VirtualScene> keyFrames;

    public TimeLine() {
        keyFrames = new TreeMap<>();
    }

    public void add(KeyFrame keyFrame, VirtualScene scene) {
        val previous = keyFrames.put(keyFrame, scene);
        if (previous != null) {
            throw new KeyFrameDuplicationException(keyFrame);
        }
    }
}
