package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.Function;
import com.annimon.hotarufx.lib.NodeValue;
import com.annimon.hotarufx.lib.Types;
import com.annimon.hotarufx.lib.Validator;
import com.annimon.hotarufx.visual.objects.CircleNode;
import com.annimon.hotarufx.visual.objects.GroupNode;
import com.annimon.hotarufx.visual.objects.ObjectNode;
import com.annimon.hotarufx.visual.objects.RectangleNode;
import com.annimon.hotarufx.visual.objects.SVGPathNode;
import com.annimon.hotarufx.visual.objects.TextNode;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.val;

public class NodesBundle implements Bundle {

    @Override
    public void load(Context context) {
        context.functions().put("circle", node(CircleNode::new));
        context.functions().put("group", group());
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
