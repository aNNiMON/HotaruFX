package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import java.util.Map;
import java.util.Set;

public interface Bundle {

    Map<String, FunctionInfo> functionsInfo();

    default Map<IdentifierType, Set<String>> identifiers() {
        return Map.of();
    }

    default void load(Context context) {
        functionsInfo().forEach((name, info) -> {
            context.functions().put(name, info.extract(context));
        });
    }
}
