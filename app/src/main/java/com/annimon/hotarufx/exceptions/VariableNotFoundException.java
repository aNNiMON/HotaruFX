package com.annimon.hotarufx.exceptions;

import com.annimon.hotarufx.lexer.SourcePosition;

public class VariableNotFoundException extends HotaruRuntimeException {

    public VariableNotFoundException(String variable, SourcePosition start, SourcePosition end) {
        super("Variable " + variable + " does not exists", start, end);
    }
}
