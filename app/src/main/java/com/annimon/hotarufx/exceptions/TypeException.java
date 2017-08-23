package com.annimon.hotarufx.exceptions;

import com.annimon.hotarufx.lexer.SourcePosition;

public class TypeException extends HotaruRuntimeException {

    public TypeException(String message) {
        super(message);
    }

    public TypeException(String message, SourcePosition start, SourcePosition end) {
        super(message, start, end);
    }
}