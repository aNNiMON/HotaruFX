package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Function;
import com.annimon.hotarufx.lib.NodeValue;
import com.annimon.hotarufx.lib.Types;
import com.annimon.hotarufx.lib.Validator;
import com.annimon.hotarufx.lib.Value;
import com.annimon.hotarufx.visual.objects.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import static com.annimon.hotarufx.bundles.FunctionInfo.of;
import static com.annimon.hotarufx.bundles.FunctionType.NODE;

public class NodesBundle implements Bundle {

    private static final Map<String, FunctionInfo> FUNCTIONS;
    static {
        FUNCTIONS = new HashMap<>();
        FUNCTIONS.put("arc", of(NODE, node(ArcNode::new)));
        FUNCTIONS.put("circle", of(NODE, node(CircleNode::new)));
        FUNCTIONS.put("ellipse", of(NODE, node(EllipseNode::new)));
        FUNCTIONS.put("group", of(NODE, group()));
        FUNCTIONS.put("image", of(NODE, image()));
        FUNCTIONS.put("line", of(NODE, node(LineNode::new)));
        FUNCTIONS.put("polygon", of(NODE, poly(PolygonNode::new)));
        FUNCTIONS.put("polyline", of(NODE, poly(PolylineNode::new)));
        FUNCTIONS.put("rectangle", of(NODE, node(RectangleNode::new)));
        FUNCTIONS.put("svgPath", of(NODE, node(SVGPathNode::new)));
        FUNCTIONS.put("text", of(NODE, node(TextNode::new)));
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
