package com.annimon.hotarufx.visual;

import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import lombok.Getter;

public class TimeLine {

    @Getter
    private final double frameRate;

    @Getter
    private final Timeline fxTimeline;

    public TimeLine(double frameRate) {
        this.frameRate = frameRate;
        fxTimeline = new Timeline(frameRate);
    }

    public void addKeyFrame(KeyFrame keyFrame, KeyValue fxKeyValue) {
        fxTimeline.getKeyFrames().add(new javafx.animation.KeyFrame(
                duration(keyFrame), fxKeyValue));
    }

    private Duration duration(KeyFrame keyFrame) {
        return Duration.millis(1000d * keyFrame.getFrame() / frameRate);
    }
}
