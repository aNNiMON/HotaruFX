package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.lib.Value;
import com.annimon.hotarufx.parser.visitors.ResultVisitor;

public interface Accessible extends Node {

    <T> Value get(ResultVisitor<Value, T> visitor, T input);

    <T> Value set(ResultVisitor<Value, T> visitor, Value value, T input);
}
