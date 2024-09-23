package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.InterpolatorValue;
import java.util.Map;
import javafx.animation.Interpolator;
import static java.util.Map.entry;

public class InterpolatorsBundle implements Bundle {

    private static final Map<String, FunctionInfo> FUNCTIONS;
    static {
        FUNCTIONS = Map.of();
    }

    @Override
    public Map<String, FunctionInfo> functionsInfo() {
        return FUNCTIONS;
    }

    @Override
    public void load(Context context) {
        Bundle.super.load(context);
        context.variables().putAll(Map.ofEntries(
                entry("linear", new InterpolatorValue(Interpolator.LINEAR)),
                entry("hold", new InterpolatorValue(Interpolator.DISCRETE)),
                entry("discrete", new InterpolatorValue(Interpolator.DISCRETE)),
                entry("easeIn", new InterpolatorValue(Interpolator.EASE_IN)),
                entry("easeOut", new InterpolatorValue(Interpolator.EASE_OUT)),
                entry("ease", new InterpolatorValue(Interpolator.EASE_BOTH)),
                entry("easeBoth", new InterpolatorValue(Interpolator.EASE_BOTH))
        ));
    }
}
