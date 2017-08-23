package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.lib.Value;

public interface Accessible extends Node {

    <T> Value get(ResultVisitor<Value, T> visitor, T input);

    <T> Value set(ResultVisitor<Value, T> visitor, Value value, T input);
}
