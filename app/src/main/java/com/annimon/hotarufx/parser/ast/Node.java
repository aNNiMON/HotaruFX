package com.annimon.hotarufx.parser.ast;

public interface Node {

    <R, T> R accept(ResultVisitor<R, T> visitor, T input);
}
