package com.annimon.hotarufx.exceptions;

import com.annimon.hotarufx.lexer.SourcePosition;

public class HotaruRuntimeException extends RuntimeException {

    public HotaruRuntimeException(String s) {
        super(s);
    }

    public HotaruRuntimeException(String string, SourcePosition start, SourcePosition end) {
        super(string + " at " + start + " .. " + end);
    }
}