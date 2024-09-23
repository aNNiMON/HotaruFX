package com.annimon.hotarufx.visual;

import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;

public final class TimeLine {

    private final double frameRate;
    private final Timeline fxTimeline;

    public TimeLine(double frameRate) {
        this.frameRate = frameRate;
        fxTimeline = new Timeline(frameRate);
    }

    public double getFrameRate() {
        return frameRate;
    }

    public Timeline getFxTimeline() {
        return fxTimeline;
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
            case PAUSED -> fxTimeline.play();
            case RUNNING -> fxTimeline.pause();
            case STOPPED -> fxTimeline.playFromStart();
        }
    }

    public void seekFrame(final int value) {
        fxTimeline.pause();
        final var offset = Duration.millis(1000d * Math.abs(value) / frameRate);
        final var now = fxTimeline.getCurrentTime();
        final var newDuration = value > 0 ? now.add(offset) : now.subtract(offset);
        fxTimeline.jumpTo(newDuration);
    }

    public void seek(final int sec) {
        final var now = fxTimeline.getCurrentTime();
        fxTimeline.jumpTo(now.add(Duration.seconds(sec)));
    }
}
