package com.annimon.hotarufx.lexer;

import lombok.Data;

@Data
public class SourcePosition {

    private final int position;
    private final int row;
    private final int column;

    @Override
    public String toString() {
        return "[" + row + ", " + column + "]";
    }
}
