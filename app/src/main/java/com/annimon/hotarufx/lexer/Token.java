package com.annimon.hotarufx.lexer;

public record Token(
        HotaruTokenId type,
        String text,
        int length,
        SourcePosition position
) {
    @Override
    public String toString() {
        return type.name() + " " + position + " " + text;
    }
}
