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
import java.util.stream.Collectors;
import lombok.val;
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
            val map = Validator.with(args).requireMapAt(0);
            val node = new NodeValue(supplier.get());
            node.fill(map);
            return node;
        };
    }

    private static Function poly(java.util.function.Function<List<Double>, ObjectNode> ctor) {
        return args -> {
            val validator = Validator.with(args);
            val map = validator.requireMapAt(1);
            val points = validator.requireArrayAt(0).stream()
                    .map(Value::asDouble)
                    .collect(Collectors.toList());
            val node = new NodeValue(ctor.apply(points));
            node.fill(map);
            return node;
        };
    }

    private static Function image() {
        return args -> {
            val validator = Validator.with(args);
            val map = validator.requireMapAt(1);
            val url = args[0].asString();
            val node = new NodeValue(new ImageNode(url));
            node.fill(map);
            return node;
        };
    }

    private static Function group() {
        return args -> {
            val nodes = Arrays.stream(args)
                    .filter(v -> v.type() == Types.NODE)
                    .map(v -> ((NodeValue) v).getNode())
                    .collect(Collectors.toList());
            return new NodeValue(new GroupNode(nodes));
        };
    }
}
