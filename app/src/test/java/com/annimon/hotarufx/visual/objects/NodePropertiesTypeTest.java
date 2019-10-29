package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.Property;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;
import javafx.beans.value.WritableValue;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class NodePropertiesTypeTest {

    static Stream<Arguments> nodeProvider() {
        return Stream.of(
                new ArcNode(),
                new CircleNode(),
                new EllipseNode(),
                new GroupNode(Collections.singletonList(new TextNode())),
                new LineNode(),
                new PolygonNode(Arrays.asList(0d, 0d, 20d, 20d, 30d, 30d)),
                new PolylineNode(Arrays.asList(0d, 0d, 20d, 20d, 30d, 30d, 0d, 0d)),
                new RectangleNode(),
                new SVGPathNode(),
                new TextNode()
        ).flatMap(node -> node.propertyBindings()
                        .entrySet()
                        .stream()
                        .map(entry -> Arguments.of(
                                node,
                                entry.getKey(),
                                entry.getValue(),
                                node.getClass().getSimpleName()
                        ))
                );
    }

    @DisplayName("Node properties type checking")
    @ParameterizedTest(name = "{3}: {1}")
    @MethodSource("nodeProvider")
    @SuppressWarnings("unchecked")
    void testNode(ObjectNode node, String name, Property property, String nodeName) {
        try {
            final var value = property.getProperty().get().getProperty();
            switch (property.getType()) {
                case BOOLEAN:
                    final var booleanValue = (WritableValue<Boolean>) value;
                    booleanValue.setValue(true);
                    assertTrue(booleanValue.getValue());
                    break;
                case NUMBER:
                    final var numberValue = (WritableValue<Number>) value;
                    numberValue.setValue(2);
                    assertThat(numberValue.getValue().intValue(), is(2));
                    break;
                case STRING:
                    final var stringValue = (WritableValue<String>) value;
                    stringValue.setValue("0");
                    assertThat(stringValue.getValue(), is("0"));
                    break;
                case NODE:
                case CLIP_NODE:
                    final var nodeValue = (WritableValue<Node>) value;
                    nodeValue.setValue(new Text("test"));
                    assertThat(((Text) nodeValue.getValue()).getText(), is("test"));
                    break;
                case PAINT:
                    final var paintValue = (WritableValue<Paint>) value;
                    paintValue.setValue(Color.BLUE);
                    assertThat(paintValue.getValue(), is(Color.BLUE));
                    break;
                case FONT:
                    final var fontValue = (WritableValue<Font>) value;
                    fontValue.setValue(Font.getDefault());
                    assertThat(fontValue.getValue().getFamily(), is(Font.getDefault().getFamily()));
                    break;
            }
        } catch (Exception e) {
            fail(node.getClass().getSimpleName() + ", " + name, e);
        }
    }

}