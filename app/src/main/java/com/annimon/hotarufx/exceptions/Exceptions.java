package com.annimon.hotarufx.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class Exceptions {

    private Exceptions() {
        throw new IllegalStateException("Oh là là!");
    }

    public static String stackTraceToString(Throwable throwable) {
        final var sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
