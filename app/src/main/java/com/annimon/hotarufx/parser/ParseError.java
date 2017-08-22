package com.annimon.hotarufx.parser;

import com.annimon.hotarufx.lexer.SourcePosition;

public class ParseError {

    private final Exception exception;
    private final SourcePosition pos;

    public ParseError(Exception exception, SourcePosition pos) {
        this.exception = exception;
        this.pos = pos;
    }

    public Exception getException() {
        return exception;
    }

    public SourcePosition getPosition() {
        return pos;
    }

    @Override
    public String toString() {
        return "ParseError " + exception.getMessage() + " at " + pos.toString();
    }
}