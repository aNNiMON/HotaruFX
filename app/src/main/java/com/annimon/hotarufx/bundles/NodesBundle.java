package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.Function;
import com.annimon.hotarufx.lib.NodeValue;
import com.annimon.hotarufx.lib.Types;
import com.annimon.hotarufx.lib.Validator;
import com.annimon.hotarufx.lib.Value;
import com.annimon.hotarufx.visual.objects.CircleNode;
import com.annimon.hotarufx.visual.objects.GroupNode;
import com.annimon.hotarufx.visual.objects.LineNode;
import com.annimon.hotarufx.visual.objects.ObjectNode;
import com.annimon.hotarufx.visual.objects.PolygonNode;
import com.annimon.hotarufx.visual.objects.PolylineNode;
import com.annimon.hotarufx.visual.objects.RectangleNode;
import com.annimon.hotarufx.visual.objects.SVGPathNode;
import com.annimon.hotarufx.visual.objects.TextNode;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.val;

public class NodesBundle implements Bundle {

    @Override
    public void load(Context context) {
        context.functions().put("circle", node(CircleNode::new));
        context.functions().put("group", group());
        context.functions().put("line", node(LineNode::new));
        context.functions().put("polygon", poly(PolygonNode::new));
        context.functions().put("polyline", poly(PolylineNode::new));
        context.functions().put("rectangle", node(RectangleNode::new));
        context.functions().put("svgPath", node(SVGPathNode::new));
        context.functions().put("text", node(TextNode::new));
    }

    private Function node(Supplier<? extends ObjectNode> supplier) {
        return args -> {
            val map = Validator.with(args).requireMapAt(0);
            val node = new NodeValue(supplier.get());
            node.fill(map);
            return node;
        };
    }

    private Function poly(java.util.function.Function<List<Double>, ObjectNode> ctor) {
        return args -> {
            val validator = Validator.with(args);
            val map = validator.requireMapAt(1);
            val points = validator.requireArrayAt(0).stream()
                    .map(Value::asNumber)
                    .collect(Collectors.toList());
            val node = new NodeValue(ctor.apply(points));
            node.fill(map);
            return node;
        };
    }

    private Function group() {
        return args -> {
            val nodes = Arrays.stream(args)
                    .filter(v -> v.type() == Types.NODE)
                    .map(v -> ((NodeValue) v).getNode())
                    .collect(Collectors.toList());
            return new NodeValue(new GroupNode(nodes));
        };
    }
}
