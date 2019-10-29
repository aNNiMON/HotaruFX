package com.annimon.hotarufx.lexer;

import java.util.Objects;

public class SourcePosition {

    private final int position;
    private final int row;
    private final int column;

    public SourcePosition(int position, int row, int column) {
        this.position = position;
        this.row = row;
        this.column = column;
    }

    public int getPosition() {
        return position;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SourcePosition that = (SourcePosition) o;
        return position == that.position &&
                row == that.row &&
                column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, row, column);
    }

    @Override
    public String toString() {
        return "[" + row + ", " + column + "]";
    }
}
