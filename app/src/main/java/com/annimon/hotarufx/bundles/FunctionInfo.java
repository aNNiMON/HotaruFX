package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.Function;

public final class FunctionInfo {

    public static FunctionInfo of(Function function) {
        return of((Context c) -> function);
    }

    public static FunctionInfo of(java.util.function.Function<Context, Function> extractor) {
        return new FunctionInfo(extractor);
    }

    private final java.util.function.Function<Context, Function> functionExtractor;

    private FunctionInfo(java.util.function.Function<Context, Function> functionExtractor) {
        this.functionExtractor = functionExtractor;
    }

    public Function extract(Context context) {
        return functionExtractor.apply(context);
    }
}
