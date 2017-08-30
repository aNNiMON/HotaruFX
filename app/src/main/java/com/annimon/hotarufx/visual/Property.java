package com.annimon.hotarufx.visual;

import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class Property {

    @Getter
    private final PropertyType type;

    @Getter
    private final Supplier<PropertyTimeline<?>> property;

}
