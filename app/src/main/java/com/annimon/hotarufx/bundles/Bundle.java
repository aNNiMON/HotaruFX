package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import java.util.Map;
import java.util.stream.Collectors;

public interface Bundle {

    Map<String, FunctionInfo> functionsInfo();

    default Map<String, FunctionType> functions() {
        return functionsInfo().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getType()));
    }

    default void load(Context context) {
        functionsInfo().forEach((name, info) -> {
            context.functions().put(name, info.extract(context));
        });
    }
}
