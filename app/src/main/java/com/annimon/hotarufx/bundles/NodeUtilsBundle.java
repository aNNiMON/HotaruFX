package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.exceptions.TypeException;
import com.annimon.hotarufx.lib.ArrayValue;
import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.Function;
import com.annimon.hotarufx.lib.NumberValue;
import com.annimon.hotarufx.lib.Types;
import com.annimon.hotarufx.lib.Validator;
import com.annimon.hotarufx.lib.Value;
import com.annimon.hotarufx.visual.KeyFrame;
import com.annimon.hotarufx.visual.objects.ShapeNode;
import java.util.Map;
import com.annimon.hotarufx.visual.objects.TextNode;
import javafx.animation.Interpolator;
import javafx.scene.shape.Shape;
import static com.annimon.hotarufx.bundles.FunctionInfo.of;
import static java.util.Map.entry;

public class NodeUtilsBundle implements Bundle {

    private static final Map<String, FunctionInfo> FUNCTIONS;
    static {
        FUNCTIONS = Map.ofEntries(
                entry("strokeDashArray", of(NodeUtilsBundle::strokePattern)),
                entry("typewriter", of(NodeUtilsBundle::typewriter))
        );
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

    private static Function typewriter(Context context) {
        return args -> {
            // (textNode, text, startAt, step = 50 ms) -> endFrame
            final var validator = Validator.with(args);
            validator.checkOrOr(3, 4);
            if (args[0].type() != Types.NODE || !(args[0].raw() instanceof TextNode textNode)) {
                throw new TypeException("TextNode required at first argument");
            }

            final String text = args[1].asString();
            final double startAt = args[2].asDouble();
            final double step;
            if (args.length == 4) {
                step = args[3].asDouble();
            } else {
                final var frameRate = context.composition().getTimeline().getFrameRate();
                step = 50 * frameRate / 1000d;
            }

            double frame = startAt;
            textNode.textProperty()
                    .add(KeyFrame.of((int) frame), "", Interpolator.DISCRETE);
            final var sb = new StringBuilder();
            for (char ch : text.toCharArray()) {
                frame += step;
                sb.append(ch);
                textNode.textProperty()
                        .add(KeyFrame.of((int) frame), sb.toString(), Interpolator.DISCRETE);
            }
            return NumberValue.of(frame);
        };
    }
}
