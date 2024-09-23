package com.annimon.hotarufx.visual;

import java.util.function.Supplier;

public record Property(
        PropertyType type,
        Supplier<PropertyTimeline<?>> property) {

}
