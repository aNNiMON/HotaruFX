package com.annimon.hotarufx.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import lombok.val;

public final class Exceptions {

    private Exceptions() {
        throw new IllegalStateException("Oh là là!");
    }

    public static String stackTraceToString(Throwable throwable) {
        val sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
