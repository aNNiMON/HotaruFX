package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.TimeLine;
import java.util.function.Consumer;
import javafx.animation.KeyValue;
import javafx.scene.paint.Paint;

public class PropertyConsumers {

    public static Consumer<PropertyTimeline<Number>> numberConsumer(TimeLine timeline) {
        return genericConsumer(timeline);
    }

    public static Consumer<PropertyTimeline<String>> stringConsumer(TimeLine timeline) {
        return genericConsumer(timeline);
    }

    public static Consumer<PropertyTimeline<Paint>> paintConsumer(TimeLine timeline) {
        return genericConsumer(timeline);
    }

    public static <T> Consumer<PropertyTimeline<T>> genericConsumer(TimeLine timeline) {
        return t -> {
            t.getKeyFrames().forEach((keyFrame, value) -> {
                timeline.addKeyFrame(keyFrame, new KeyValue(t.getProperty(), value));
            });
        };
    }
}
