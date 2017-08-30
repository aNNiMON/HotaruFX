package com.annimon.hotarufx.visual;

import java.util.HashMap;
import java.util.function.Supplier;

public final class PropertyBindings extends HashMap<String, Property> {

    public PropertyBindings add(String key, PropertyType type, Supplier<PropertyTimeline<?>> property) {
        super.put(key, new Property(type, property));
        return this;
    }

    public PropertyBindings merge(PropertyBindings bindings) {
        super.putAll(bindings);
        return this;
    }
}
