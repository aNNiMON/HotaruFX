package com.annimon.hotarufx.visual;

import java.util.function.Supplier;

public final class Property {

    private final PropertyType type;
    private final Supplier<PropertyTimeline<?>> property;

    public Property(PropertyType type, Supplier<PropertyTimeline<?>> property) {
        this.type = type;
        this.property = property;
    }

    public PropertyType getType() {
        return type;
    }

    public Supplier<PropertyTimeline<?>> getProperty() {
        return property;
    }
}
