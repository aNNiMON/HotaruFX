package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.parser.visitors.ResultVisitor;

public interface Node {

    <R, T> R accept(ResultVisitor<R, T> visitor, T input);
}
