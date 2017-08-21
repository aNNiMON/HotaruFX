package com.annimon.hotarufx.exceptions;

import com.annimon.hotarufx.lexer.SourcePosition;

public class LexerException extends RuntimeException {

    public LexerException(String message) {
        super(message);
    }

    public LexerException(SourcePosition position, String message) {
        super(position.toString() + " " + message);
    }
}
