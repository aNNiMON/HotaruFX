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
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Paint;
import lombok.val;
import static com.annimon.hotarufx.bundles.FunctionInfo.of;
import static com.annimon.hotarufx.bundles.FunctionType.COMMON;

public class CompositionBundle implements Bundle {

    private static final Map<String, FunctionInfo> FUNCTIONS;
    static {
        FUNCTIONS = new HashMap<>();
        FUNCTIONS.put("composition", of(COMMON, CompositionBundle::composition));
        FUNCTIONS.put("render", of(COMMON, CompositionBundle::render));
    }

    @Override
    public Map<String, FunctionInfo> functionsInfo() {
        return FUNCTIONS;
    }

    private static Function composition(Context context) {
        return args -> {
            final Composition composition;
            switch (args.length) {
                case 0:
                    composition = new Composition();
                    break;
                case 1:
                    double frameRate = args[0].asDouble();
                    composition = new Composition(frameRate);
                    break;
                case 2:
                    int width = args[0].asInt();
                    int height = args[1].asInt();
                    composition = new Composition(width, height);
                    break;
                case 3:
                    width = args[0].asInt();
                    height = args[1].asInt();
                    frameRate = args[2].asDouble();
                    composition = new Composition(width, height, frameRate);
                    break;
                case 4:
                default:
                    width = args[0].asInt();
                    height = args[1].asInt();
                    frameRate = args[2].asDouble();
                    val background = PropertyType.PAINT.<Paint>getFromHFX().apply(args[3]);
                    composition = new Composition(width, height, frameRate, background);
                    break;
            }
            val scene = composition.getScene();
            context.composition(composition);
            context.variables().put("Width", NumberValue.of(scene.getVirtualWidth()));
            context.variables().put("Height", NumberValue.of(scene.getVirtualHeight()));
            context.variables().put("HalfWidth", NumberValue.of(scene.getVirtualWidth() / 2));
            context.variables().put("HalfHeight", NumberValue.of(scene.getVirtualHeight() / 2));
            return NumberValue.ZERO;
        };
    }

    private static Function render(Context context) {
        return args -> {
            val renderVisitor = new RenderVisitor(context.composition().getTimeline());
            val scene = context.composition().getScene();
            Arrays.stream(args)
                    .filter(v -> v.type() == Types.NODE)
                    .map(v -> ((NodeValue) v).getNode())
                    .forEach(node -> node.accept(renderVisitor, scene));
            return NumberValue.ZERO;
        };
    }
}
