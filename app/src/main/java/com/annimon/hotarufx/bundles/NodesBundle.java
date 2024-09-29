package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.exceptions.HotaruRuntimeException;
import com.annimon.hotarufx.lib.*;
import com.annimon.hotarufx.visual.objects.*;
import java.util.*;
import java.util.function.Supplier;
import static com.annimon.hotarufx.bundles.FunctionInfo.of;
import static java.util.Map.entry;

public class NodesBundle implements Bundle {

    private static final Map<String, FunctionInfo> FUNCTIONS;
    static {
        FUNCTIONS = Map.ofEntries(
                entry("arc", of(node(ArcNode::new))),
                entry("circle", of(node(CircleNode::new))),
                entry("ellipse", of(node(EllipseNode::new))),
                entry("group", of(group(GroupNode::new))),
                entry("guideGrid", of(guideGrid())),
                entry("image", of(image())),
                entry("line", of(node(LineNode::new))),
                entry("polygon", of(poly(PolygonNode::new))),
                entry("polyline", of(poly(PolylineNode::new))),
                entry("rectangle", of(node(RectangleNode::new))),
                entry("svgPath", of(node(SVGPathNode::new))),
                entry("text", of(node(TextNode::new))),
                entry("textFlow", of(group(TextFlowNode::new)))
        );
    }

    @Override
    public Map<String, FunctionInfo> functionsInfo() {
        return FUNCTIONS;
    }

    @Override
    public Map<IdentifierType, Set<String>> identifiers() {
        return Map.of(IdentifierType.NODE, FUNCTIONS.keySet());
    }

    private static Function node(Supplier<? extends ObjectNode> supplier) {
        return args -> {
            final var map = Validator.with(args).requireMapAt(0);
            final var node = new NodeValue(supplier.get());
            node.fill(map);
            return node;
        };
    }

    private static Function poly(java.util.function.Function<List<Double>, ObjectNode> ctor) {
        return args -> {
            final var validator = Validator.with(args);
            final var map = validator.requireMapAt(1);
            final var points = validator.requireArrayAt(0).stream()
                    .map(Value::asDouble)
                    .toList();
            final var node = new NodeValue(ctor.apply(points));
            node.fill(map);
            return node;
        };
    }

    private static Function image() {
        return args -> {
            final var validator = Validator.with(args);
            final var map = validator.requireMapAt(1);
            final var url = args[0].asString();
            final var node = new NodeValue(new ImageNode(url));
            node.fill(map);
            return node;
        };
    }

    private static Function group(java.util.function.Function<List<ObjectNode>, ObjectNode> ctor) {
        return args -> {
            final var nodes = Arrays.stream(args)
                    .filter(v -> v.type() == Types.NODE)
                    .map(v -> ((NodeValue) v).getNode())
                    .toList();
            return new NodeValue(ctor.apply(nodes));
        };
    }

    private static Function guideGrid() {
        return args -> {
            final var validator = Validator.with(args);
            final var map = validator.requireMapAt(0);

            final var config = map.getMap();
            final int stepX = config.getOrDefault("stepX", NumberValue.of(100)).asInt();
            final int stepY = config.getOrDefault("stepY", NumberValue.of(100)).asInt();
            final int width = config.getOrDefault("width", NumberValue.of(1920)).asInt();
            final int height = config.getOrDefault("height", NumberValue.of(1080)).asInt();
            if (stepX < 5 || stepX > 1920)
                throw new HotaruRuntimeException("stepX should be within 5...1920 px");
            if (stepY < 5 || stepY > 1080)
                throw new HotaruRuntimeException("stepY should be within 5...1080 px");
            if (width < stepX || width > 10000)
                throw new HotaruRuntimeException("width should be within stepX(%d)...10000 px".formatted(stepX));
            if (height < stepY || height > 10000)
                throw new HotaruRuntimeException("height should be within stepY(%d)...10000 px".formatted(stepY));

            final var node = new NodeValue(new GuideGridNode(stepX, stepY, width, height));
            node.fill(map);
            return node;
        };
    }
}
