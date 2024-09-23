package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.Function;
import com.annimon.hotarufx.lib.NodeValue;
import com.annimon.hotarufx.lib.NumberValue;
import com.annimon.hotarufx.lib.Types;
import com.annimon.hotarufx.visual.Composition;
import com.annimon.hotarufx.visual.PropertyType;
import com.annimon.hotarufx.visual.visitors.RenderVisitor;
import java.util.Arrays;
import java.util.Map;
import javafx.scene.paint.Paint;
import static com.annimon.hotarufx.bundles.FunctionInfo.of;
import static java.util.Map.entry;

public class CompositionBundle implements Bundle {

    private static final Map<String, FunctionInfo> FUNCTIONS;
    static {
        FUNCTIONS = Map.ofEntries(
                entry("composition", of(CompositionBundle::composition)),
                entry("render", of(CompositionBundle::render))
        );
    }

    @Override
    public Map<String, FunctionInfo> functionsInfo() {
        return FUNCTIONS;
    }

    private static Function composition(Context context) {
        return args -> {
            final Composition composition = switch (args.length) {
                case 0 -> new Composition();
                case 1 -> {
                    double frameRate = args[0].asDouble();
                    yield new Composition(frameRate);
                }
                case 2 -> {
                    int width = args[0].asInt();
                    int height = args[1].asInt();
                    yield new Composition(width, height);
                }
                case 3 -> {
                    int width = args[0].asInt();
                    int height = args[1].asInt();
                    double frameRate = args[2].asDouble();
                    yield new Composition(width, height, frameRate);
                }
                default -> {
                    int width = args[0].asInt();
                    int height = args[1].asInt();
                    double frameRate = args[2].asDouble();
                    final var background = PropertyType.PAINT.<Paint>getFromHFX().apply(args[3]);
                    yield new Composition(width, height, frameRate, background);
                }
            };
            final var scene = composition.getScene();
            context.composition(composition);
            context.variables().putAll(Map.ofEntries(
                    entry("Width", NumberValue.of(scene.getVirtualWidth())),
                    entry("Height", NumberValue.of(scene.getVirtualHeight())),
                    entry("HalfWidth", NumberValue.of(scene.getVirtualWidth() / 2)),
                    entry("HalfHeight", NumberValue.of(scene.getVirtualHeight() / 2))
            ));
            return NumberValue.ZERO;
        };
    }

    private static Function render(Context context) {
        return args -> {
            final var renderVisitor = new RenderVisitor(context.composition().getTimeline());
            final var scene = context.composition().getScene();
            Arrays.stream(args)
                    .filter(v -> v.type() == Types.NODE)
                    .map(v -> ((NodeValue) v).getNode())
                    .forEach(node -> node.accept(renderVisitor, scene));
            return NumberValue.ZERO;
        };
    }
}
