package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiConsumer;

public final class BundleLoader {

    public static List<Class<? extends Bundle>> runtimeBundles() {
        return List.of(
                CompositionBundle.class,
                NodesBundle.class,
                NodeUtilsBundle.class,
                InterpolatorsBundle.class,
                FontBundle.class
        );
    }

    public static void loadSingle(Context context, Class<? extends Bundle> clazz) {
        load(context, List.of(clazz));
    }

    public static Map<IdentifierType, Set<String>> identifiers() {
        final var identifiers = new EnumMap<IdentifierType, Set<String>>(IdentifierType.class);
        apply(runtimeBundles(), identifiers, ((bundle, map) -> map.putAll(bundle.identifiers())));
        return identifiers;
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
                final var ctor = clazz.getDeclaredConstructor();
                final var bundle = ctor.newInstance();
                action.accept(bundle, obj);
            } catch (IllegalAccessException | InstantiationException
                    | NoSuchMethodException | InvocationTargetException ignored) {
                // ignored
            }
        }
    }

    private BundleLoader() { }
}
