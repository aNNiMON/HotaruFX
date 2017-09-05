package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.Function;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FunctionInfo {

    public static FunctionInfo of(FunctionType type, Function function) {
        return of(type, (Context c) -> function);
    }

    public static FunctionInfo of(FunctionType type, java.util.function.Function<Context, Function> extractor) {
        return new FunctionInfo(type, extractor);
    }

    @Getter
    private final FunctionType type;
    private final java.util.function.Function<Context, Function> functionExtractor;

    public Function extract(Context context) {
        return functionExtractor.apply(context);
    }
}
