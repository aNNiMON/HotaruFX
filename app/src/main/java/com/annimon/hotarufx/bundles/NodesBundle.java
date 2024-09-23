package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Function;
import com.annimon.hotarufx.lib.NodeValue;
import com.annimon.hotarufx.lib.Types;
import com.annimon.hotarufx.lib.Validator;
import com.annimon.hotarufx.lib.Value;
import com.annimon.hotarufx.visual.objects.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import static com.annimon.hotarufx.bundles.FunctionInfo.of;
import static com.annimon.hotarufx.bundles.FunctionType.NODE;
import static java.util.Map.entry;

public class NodesBundle implements Bundle {

    private static final Map<String, FunctionInfo> FUNCTIONS;
    static {
        FUNCTIONS = Map.ofEntries(
                entry("arc", of(NODE, node(ArcNode::new))),
                entry("circle", of(NODE, node(CircleNode::new))),
                entry("ellipse", of(NODE, node(EllipseNode::new))),
                entry("group", of(NODE, group())),
                entry("image", of(NODE, image())),
                entry("line", of(NODE, node(LineNode::new))),
                entry("polygon", of(NODE, poly(PolygonNode::new))),
                entry("polyline", of(NODE, poly(PolylineNode::new))),
                entry("rectangle", of(NODE, node(RectangleNode::new))),
                entry("svgPath", of(NODE, node(SVGPathNode::new))),
                entry("text", of(NODE, node(TextNode::new)))
        );
    }

    @Override
    public Map<String, FunctionInfo> functionsInfo() {
        return FUNCTIONS;
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

    private static Function group() {
        return args -> {
            final var nodes = Arrays.stream(args)
                    .filter(v -> v.type() == Types.NODE)
                    .map(v -> ((NodeValue) v).getNode())
                    .toList();
            return new NodeValue(new GroupNode(nodes));
        };
    }
}
