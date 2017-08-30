package com.annimon.hotarufx.exceptions;

import com.annimon.hotarufx.lexer.SourcePosition;

public class ArgumentsMismatchException extends HotaruRuntimeException {

    public ArgumentsMismatchException(String message) {
        super(message);
    }

    public ArgumentsMismatchException(String message, SourcePosition start, SourcePosition end) {
        super(message, start, end);
    }
}
