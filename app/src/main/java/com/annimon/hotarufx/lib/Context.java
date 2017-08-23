package com.annimon.hotarufx.lib;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Context {

    private final Map<String, Value> variables;
    private final Map<String, Function> functions;

    public Context() {
        variables = new ConcurrentHashMap<>();
        functions = new ConcurrentHashMap<>();
    }

    public Map<String, Value> variables() {
        return variables;
    }

    public Map<String, Function> functions() {
        return functions;
    }
}
