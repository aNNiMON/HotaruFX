package com.annimon.hotarufx.lib;

import com.annimon.hotarufx.visual.Composition;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Context {

    private final Map<String, Value> variables;
    private final Map<String, Function> functions;
    private Composition composition;

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

    public Composition composition() {
        return composition;
    }

    public void composition(Composition composition) {
        this.composition = composition;
    }
}
