package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.lib.Value;

public interface Accessible extends Node {

    Value get();

    Value set(Value value);
}
