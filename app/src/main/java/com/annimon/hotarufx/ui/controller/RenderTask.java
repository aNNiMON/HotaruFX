package com.annimon.hotarufx.ui.controller;

import com.annimon.hotarufx.visual.Composition;
import com.annimon.hotarufx.visual.TimeLine;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.util.Duration;
import javax.imageio.ImageIO;

public class RenderTask extends Task<Boolean> {

    private final File directory;
    private final Composition composition;
    private final Scene scene;
    private final TimeLine timeLine;
    private final double frameRate;

    public RenderTask(File directory, Composition composition, Scene scene) {
        this.directory = directory;
        this.composition = composition;
        this.scene = scene;
        this.timeLine = composition.getTimeline();
        frameRate = timeLine.getFrameRate();
    }

    @Override
    protected Boolean call() throws Exception {
        final var fxTimeline = timeLine.getFxTimeline();

        final var totalFrames = toFrame(fxTimeline.getTotalDuration());
        int frame = 0;
        while (frame < totalFrames) {
            updateProgress(frame, totalFrames);
            updateMessage(String.format("%d / %d", frame + 1, totalFrames));
            fxTimeline.jumpTo(toDuration(frame));

            final var image = newImage();

            final var latch = new CountDownLatch(1);
            Platform.runLater(() -> {
                scene.snapshot(image);
                latch.countDown();
            });
            latch.await();

            final var file = new File(directory, String.format("frame_%05d.png", frame + 1));
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            frame++;
        }
        return Boolean.TRUE;
    }

    private WritableImage newImage() {
        return new WritableImage(composition.getSceneWidth(), composition.getSceneHeight());
    }

    private int toFrame(Duration d) {
        return (int) (d.toMillis() * frameRate / 1000d);
    }

    private Duration toDuration(int frame) {
        return Duration.millis(frame * 1000d / frameRate);
    }
}
