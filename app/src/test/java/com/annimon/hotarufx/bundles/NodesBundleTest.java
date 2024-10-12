package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.MapValue;
import com.annimon.hotarufx.lib.NodeValue;
import com.annimon.hotarufx.lib.NumberValue;
import com.annimon.hotarufx.lib.StringValue;
import com.annimon.hotarufx.lib.Value;
import com.annimon.hotarufx.visual.objects.CircleNode;
import java.util.HashMap;
import java.util.List;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class NodesBundleTest {

    @Test
    void testBundle() {
        final var context = new Context();
        BundleLoader.load(context, List.of(
                CompositionBundle.class,
                NodesBundle.class
        ));

        assertThat(context.functions(), hasKey("circle"));

        final var map = new HashMap<String, Value>();
        map.put("translateZ", NumberValue.of(-40));
        map.put("cx", NumberValue.of(-10));
        map.put("radius", NumberValue.of(50));
        map.put("fill", new StringValue("#00AA00"));
        final var value = context.functions().get("circle").execute(new MapValue(map));

        assertThat(value, instanceOf(NodeValue.class));

        final var nodeValue = (NodeValue) value;
        assertThat(nodeValue.getNode(), notNullValue());
        assertThat(nodeValue.getNode(), instanceOf(CircleNode.class));

        final var circle = (CircleNode) nodeValue.getNode();
        assertThat(circle.circle.getCenterX(), closeTo(-10, 0.001));
        assertThat(circle.circle.getRadius(), closeTo(50, 0.001));
        assertThat(circle.circle.getFill(), is(Color.web("#00AA00")));
        assertThat(circle.circle.getTranslateZ(), closeTo(-40, 0.001));
    }
}