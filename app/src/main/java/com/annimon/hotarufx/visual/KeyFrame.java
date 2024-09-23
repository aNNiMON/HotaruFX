package com.annimon.hotarufx.visual;

import java.util.Objects;

public final class KeyFrame implements Comparable<KeyFrame> {

    public static KeyFrame of(int frame) {
        return new KeyFrame(frame);
    }

    private KeyFrame(int frame) {
        this.frame = frame;
    }

    private final int frame;

    public int getFrame() {
        return frame;
    }

    @Override
    public int compareTo(KeyFrame o) {
        return Integer.compare(frame, o.frame);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyFrame keyFrame = (KeyFrame) o;
        return frame == keyFrame.frame;
    }

    @Override
    public int hashCode() {
        return Objects.hash(frame);
    }
}
