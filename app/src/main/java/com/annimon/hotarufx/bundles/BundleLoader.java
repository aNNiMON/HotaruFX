package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import lombok.val;

public final class BundleLoader {

    public static List<Class<? extends Bundle>> runtimeBundles() {
        return Arrays.asList(
                CompositionBundle.class,
                NodesBundle.class,
                NodeUtilsBundle.class,
                InterpolatorsBundle.class,
                FontBundle.class
        );
    }

    public static void loadSingle(Context context, Class<? extends Bundle> clazz) {
        load(context, Collections.singletonList(clazz));
    }

    public static Map<String, FunctionType> functions() {
        val functions = new HashMap<String, FunctionType>();
        apply(runtimeBundles(), functions, ((bundle, map) -> map.putAll(bundle.functions())));
        return functions;
    }

    public static void load(Context context, List<Class<? extends Bundle>> bundles) {
        apply(bundles, context, Bundle::load);
    }

    private static <T> void apply(List<Class<? extends Bundle>> bundles,
                                  T obj, BiConsumer<Bundle, T> action) {
        if (action == null || bundles == null || bundles.isEmpty()) {
            return;
        }

        for (Class<? extends Bundle> clazz : bundles) {
            try {
                val bundle = clazz.newInstance();
                action.accept(bundle, obj);
            } catch (IllegalAccessException | InstantiationException ignore) {}
        }
    }
}
