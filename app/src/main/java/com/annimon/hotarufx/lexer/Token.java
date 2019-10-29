package com.annimon.hotarufx.lexer;

import java.util.Objects;

public class Token {

    private final HotaruTokenId type;
    private final String text;
    private final int length;
    private final SourcePosition position;

    public Token(HotaruTokenId type, String text, int length, SourcePosition position) {
        this.type = type;
        this.text = text;
        this.length = length;
        this.position = position;
    }

    public HotaruTokenId getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public int getLength() {
        return length;
    }

    public SourcePosition getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return length == token.length &&
                type == token.type &&
                Objects.equals(text, token.text) &&
                Objects.equals(position, token.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, text, length, position);
    }

    @Override
    public String toString() {
        return type.name() + " " + position + " " + text;
    }
}
