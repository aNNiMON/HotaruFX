package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.InterpolatorValue;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.Interpolator;

public class InterpolatorsBundle implements Bundle {

    private static final Map<String, FunctionInfo> FUNCTIONS;
    static {
        FUNCTIONS = new HashMap<>();
    }

    @Override
    public Map<String, FunctionInfo> functionsInfo() {
        return FUNCTIONS;
    }

    @Override
    public void load(Context context) {
        Bundle.super.load(context);
        context.variables().put("linear", new InterpolatorValue(Interpolator.LINEAR));
        context.variables().put("hold", new InterpolatorValue(Interpolator.DISCRETE));
        context.variables().put("discrete", new InterpolatorValue(Interpolator.DISCRETE));
        context.variables().put("easeIn", new InterpolatorValue(Interpolator.EASE_IN));
        context.variables().put("easeOut", new InterpolatorValue(Interpolator.EASE_OUT));
        context.variables().put("ease", new InterpolatorValue(Interpolator.EASE_BOTH));
        context.variables().put("easeBoth", new InterpolatorValue(Interpolator.EASE_BOTH));
    }
}
