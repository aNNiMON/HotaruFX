package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.exceptions.TypeException;
import com.annimon.hotarufx.lib.ArrayValue;
import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.Function;
import com.annimon.hotarufx.lib.NumberValue;
import com.annimon.hotarufx.lib.Types;
import com.annimon.hotarufx.lib.Validator;
import com.annimon.hotarufx.lib.Value;
import com.annimon.hotarufx.visual.objects.ShapeNode;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.shape.Shape;
import static com.annimon.hotarufx.bundles.FunctionInfo.of;
import static com.annimon.hotarufx.bundles.FunctionType.COMMON;

public class NodeUtilsBundle implements Bundle {

    private static final Map<String, FunctionInfo> FUNCTIONS;
    static {
        FUNCTIONS = new HashMap<>();
        FUNCTIONS.put("strokeDashArray", of(COMMON, NodeUtilsBundle::strokePattern));
    }

    @Override
    public Map<String, FunctionInfo> functionsInfo() {
        return FUNCTIONS;
    }

    private static Function strokePattern(Context context) {
        return args -> {
            final var validator = Validator.with(args);
            validator.checkOrOr(1, 2);
            if (args[0].type() != Types.NODE || !(args[0].raw() instanceof ShapeNode)) {
                throw new TypeException("Shape required at first argument");
            }
            final var shape = (Shape) ((ShapeNode) args[0].raw()).getFxNode();
            if (args.length == 2) {
                final var array = validator.requireArrayAt(1);
                final var dashList = array.stream()
                        .map(Value::asDouble)
                        .toList();
                shape.getStrokeDashArray().setAll(dashList);
                return NumberValue.ZERO;
            } else {
                return ArrayValue.from(shape.getStrokeDashArray(), NumberValue::of);
            }
        };
    }
}
