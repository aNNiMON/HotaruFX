package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.Function;

public class FunctionInfo {

    public static FunctionInfo of(FunctionType type, Function function) {
        return of(type, (Context c) -> function);
    }

    public static FunctionInfo of(FunctionType type, java.util.function.Function<Context, Function> extractor) {
        return new FunctionInfo(type, extractor);
    }

    private final FunctionType type;
    private final java.util.function.Function<Context, Function> functionExtractor;

    private FunctionInfo(FunctionType type, java.util.function.Function<Context, Function> functionExtractor) {
        this.type = type;
        this.functionExtractor = functionExtractor;
    }

    public FunctionType getType() {
        return type;
    }

    public Function extract(Context context) {
        return functionExtractor.apply(context);
    }
}
