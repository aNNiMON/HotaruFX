package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.exceptions.TypeException;
import com.annimon.hotarufx.lib.*;
import com.annimon.hotarufx.visual.KeyFrame;
import com.annimon.hotarufx.visual.objects.ShapeNode;
import java.util.Map;
import com.annimon.hotarufx.visual.objects.TextNode;
import javafx.animation.Interpolator;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import static com.annimon.hotarufx.bundles.FunctionInfo.of;
import static java.util.Map.entry;

public class NodeUtilsBundle implements Bundle {

    private static final Map<String, FunctionInfo> FUNCTIONS;
    static {
        FUNCTIONS = Map.ofEntries(
                entry("font", of(NodeUtilsBundle::newFont)),
                entry("strokeDashArray", of(NodeUtilsBundle::strokePattern)),
                entry("typewriter", of(NodeUtilsBundle::typewriter))
        );
    }

    @Override
    public Map<String, FunctionInfo> functionsInfo() {
        return FUNCTIONS;
    }

    private static Function newFont(Context context) {
        return args -> {
            final var validator = Validator.with(args);
            validator.check(1);
            if (args[0].type() == Types.MAP) {
                return new FontValue(FontValue.toFont((MapValue) args[0]));
            }
            return new FontValue(Font.font(args[0].asDouble()));
        };
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
            // (textNode, text, startAt, step = 50 ms, reverse = false) -> endFrame
            final var validator = Validator.with(args);
            validator.checkRange(3, 5);
            if (args[0].type() != Types.NODE || !(args[0].raw() instanceof TextNode textNode)) {
                throw new TypeException("TextNode required at first argument");
            }

            final String text = args[1].asString();
            final double startAt = args[2].asDouble();
            final double step;
            if (args.length >= 4) {
                step = args[3].asDouble();
            } else {
                final var frameRate = context.composition().getTimeline().getFrameRate();
                step = 50 * frameRate / 1000d;
            }
            final boolean reverse = args.length == 5 && (args[4].asBoolean());

            double frame = startAt;
            textNode.textProperty()
                    .add(KeyFrame.of((int) frame), reverse ? text : "", Interpolator.DISCRETE);
            final int last = text.length() - 1;
            for (int i = 0; i <= last; i++) {
                frame += step;
                final String s = text.substring(0, reverse ? (last - i) : i + 1);
                textNode.textProperty()
                        .add(KeyFrame.of((int) frame), s, Interpolator.DISCRETE);
            }
            return NumberValue.of(frame);
        };
    }
}
