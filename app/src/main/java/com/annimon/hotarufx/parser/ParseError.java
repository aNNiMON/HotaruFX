package com.annimon.hotarufx.parser;

import com.annimon.hotarufx.lexer.SourcePosition;

public record ParseError(Exception exception, SourcePosition pos) {

    @Override
    public String toString() {
        return "ParseError " + exception.getMessage();
    }
}