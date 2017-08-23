package com.annimon.hotarufx.exceptions;

import com.annimon.hotarufx.lexer.SourcePosition;

public class FunctionNotFoundException extends HotaruRuntimeException {

    public FunctionNotFoundException(String name, SourcePosition start, SourcePosition end) {
        super("Function " + name + " does not exists", start, end);
    }
}
