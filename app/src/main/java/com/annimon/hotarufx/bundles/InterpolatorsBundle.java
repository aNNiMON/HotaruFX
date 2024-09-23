package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.InterpolatorValue;
import java.util.Map;
import java.util.Set;
import com.annimon.hotarufx.lib.Value;
import javafx.animation.Interpolator;
import static java.util.Map.entry;

public class InterpolatorsBundle implements Bundle {

    private static final Map<String, Value> VALUES;
    static {
        VALUES = Map.ofEntries(
                entry("linear", new InterpolatorValue(Interpolator.LINEAR)),
                entry("hold", new InterpolatorValue(Interpolator.DISCRETE)),
                entry("discrete", new InterpolatorValue(Interpolator.DISCRETE)),
                entry("easeIn", new InterpolatorValue(Interpolator.EASE_IN)),
                entry("easeOut", new InterpolatorValue(Interpolator.EASE_OUT)),
                entry("ease", new InterpolatorValue(Interpolator.EASE_BOTH)),
                entry("easeBoth", new InterpolatorValue(Interpolator.EASE_BOTH))
        );
    }

    @Override
    public Map<String, FunctionInfo> functionsInfo() {
        return Map.of();
    }

    @Override
    public Map<IdentifierType, Set<String>> identifiers() {
        return Map.of(IdentifierType.INTERPOLATION, VALUES.keySet());
    }

    @Override
    public void load(Context context) {
        Bundle.super.load(context);
        context.variables().putAll(VALUES);
    }
}
