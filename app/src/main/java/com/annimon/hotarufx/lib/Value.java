package com.annimon.hotarufx.lib;

public interface Value extends Comparable<Value> {

    Object raw();

    default boolean asBoolean() {
        return asInt() != 0;
    }

    default int asInt() {
        return asNumber().intValue();
    }

    default double asDouble() {
        return asNumber().doubleValue();
    }

    Number asNumber();

    String asString();

    int type();
}
