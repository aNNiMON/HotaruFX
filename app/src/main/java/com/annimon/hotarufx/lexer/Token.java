package com.annimon.hotarufx.lexer;

import lombok.Data;

@Data
public class Token {

    private final HotaruTokenId type;
    private final String text;
    private final int length;
    private final SourcePosition position;

    @Override
    public String toString() {
        return type.name() + " " + position + " " + text;
    }
}
