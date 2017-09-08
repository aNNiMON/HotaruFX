package com.annimon.hotarufx.visual;

import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import lombok.Getter;
import lombok.val;

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

    public void togglePause() {
        switch (fxTimeline.getStatus()) {
            case PAUSED:
                fxTimeline.play();
                break;
            case RUNNING:
                fxTimeline.pause();
                break;
            case STOPPED:
                fxTimeline.playFromStart();
                break;
        }
    }

    public void seekFrame(final int value) {
        fxTimeline.pause();
        val offset = Duration.millis(1000d * Math.abs(value) / frameRate);
        val now = fxTimeline.getCurrentTime();
        val newDuration = value > 0 ? now.add(offset) : now.subtract(offset);
        fxTimeline.jumpTo(newDuration);
    }

    public void seek(final int sec) {
        val now = fxTimeline.getCurrentTime();
        fxTimeline.jumpTo(now.add(Duration.seconds(sec)));
    }
}
