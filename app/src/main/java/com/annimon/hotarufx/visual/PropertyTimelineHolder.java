package com.annimon.hotarufx.visual;

import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.beans.value.WritableValue;

public final class PropertyTimelineHolder<T> {

    public static <T> PropertyTimelineHolder<T> empty() {
        return new PropertyTimelineHolder<>(null);
    }

    private PropertyTimeline<T> value;

    private PropertyTimelineHolder(PropertyTimeline<T> value) {
        this.value = value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public boolean isEmpty() {
        return value == null;
    }

    public void ifPresent(Consumer<PropertyTimeline<T>> consumer) {
        if (isPresent()) {
            consumer.accept(value);
        }
    }

    public PropertyTimelineHolder<T> setIfEmpty(Supplier<PropertyTimeline<T>> supplier) {
        if (isEmpty()) {
            value = supplier.get();
        }
        return this;
    }

    public PropertyTimeline<T> setIfEmptyThenGet(Supplier<WritableValue<T>> supplier) {
        return setIfEmpty(wrap(supplier)).get();
    }

    public PropertyTimeline<T> get() {
        return value;
    }

    private Supplier<PropertyTimeline<T>> wrap(Supplier<WritableValue<T>> supplier) {
        return () -> new PropertyTimeline<>(supplier.get());
    }
}
