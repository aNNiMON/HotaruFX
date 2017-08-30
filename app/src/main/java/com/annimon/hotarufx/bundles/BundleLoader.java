package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import java.util.Collections;
import java.util.List;
import lombok.val;

public final class BundleLoader {

    public static void loadSingle(Context context, Class<? extends Bundle> clazz) {
        load(context, Collections.singletonList(clazz));
    }

    public static void load(Context context, List<Class<? extends Bundle>> bundles) {
        if (bundles == null || bundles.isEmpty()) {
            return;
        }

        for (Class<? extends Bundle> clazz : bundles) {
            try {
                val bundle = clazz.newInstance();
                bundle.load(context);
            } catch (IllegalAccessException | InstantiationException ignore) {}
        }
    }
}
