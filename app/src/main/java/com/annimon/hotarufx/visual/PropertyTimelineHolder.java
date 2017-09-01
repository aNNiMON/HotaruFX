package com.annimon.hotarufx.visual;

import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.animation.KeyValue;
import javafx.beans.value.WritableValue;

public final class PropertyTimelineHolder<T> {

    public static <T> PropertyTimelineHolder<T> empty() {
        return new PropertyTimelineHolder<>(null);
    }

    private PropertyTimeline<T> propertyTimeline;

    private PropertyTimelineHolder(PropertyTimeline<T> value) {
        this.propertyTimeline = value;
    }

    public boolean isPresent() {
        return propertyTimeline != null;
    }

    public boolean isEmpty() {
        return propertyTimeline == null;
    }

    public void ifPresent(Consumer<PropertyTimeline<T>> consumer) {
        if (isPresent()) {
            consumer.accept(propertyTimeline);
        }
    }

    public void applyIfPresent(TimeLine timeline) {
        if (isEmpty()) return;
        propertyTimeline.getKeyFrames().forEach((keyFrame, value) -> {
            timeline.addKeyFrame(keyFrame, new KeyValue(propertyTimeline.getProperty(), value));
        });
    }

    public PropertyTimelineHolder<T> setIfEmpty(Supplier<PropertyTimeline<T>> supplier) {
        if (isEmpty()) {
            propertyTimeline = supplier.get();
        }
        return this;
    }

    public PropertyTimeline<T> setIfEmptyThenGet(Supplier<WritableValue<T>> supplier) {
        return setIfEmpty(wrap(supplier)).get();
    }

    public PropertyTimeline<T> get() {
        return propertyTimeline;
    }

    private Supplier<PropertyTimeline<T>> wrap(Supplier<WritableValue<T>> supplier) {
        return () -> new PropertyTimeline<>(supplier.get());
    }
}
