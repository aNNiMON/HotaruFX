package com.annimon.hotarufx.exceptions;

import com.annimon.hotarufx.visual.KeyFrame;

public class KeyFrameDuplicationException extends RuntimeException {

    public KeyFrameDuplicationException(KeyFrame keyFrame) {
        super("Key frame " + keyFrame.getFrame() + " already exists in timeline");
    }
}
