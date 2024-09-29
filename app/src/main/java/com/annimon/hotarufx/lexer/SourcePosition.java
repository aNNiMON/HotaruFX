package com.annimon.hotarufx.lexer;

public record SourcePosition(int position, int row, int column) {

    @Override
    public String toString() {
        return "[" + row + ", " + column + "]";
    }
}
