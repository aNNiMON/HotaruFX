package com.annimon.hotarufx.exceptions;

import com.annimon.hotarufx.lexer.SourcePosition;

public class ParseException extends RuntimeException {

    public ParseException() {
        super();
    }

    public ParseException(String string, SourcePosition pos) {
        super(string + " at " + pos.toString());
    }
}