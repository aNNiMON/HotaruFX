package com.annimon.hotarufx.parser;

import com.annimon.hotarufx.exceptions.ParseException;
import com.annimon.hotarufx.lexer.HotaruLexer;
import com.annimon.hotarufx.parser.ast.AssignNode;
import com.annimon.hotarufx.parser.ast.BlockNode;
import com.annimon.hotarufx.parser.ast.FunctionNode;
import com.annimon.hotarufx.parser.ast.MapNode;
import com.annimon.hotarufx.parser.ast.Node;
import com.annimon.hotarufx.parser.ast.ValueNode;
import com.annimon.hotarufx.parser.ast.VariableNode;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HotaruParserTest {

    static Node p(String input) {
        return HotaruParser.parse(HotaruLexer.tokenize(input));
    }

    @Test
    void testParseNodeAssignment() {
        String input = "A = node({x: -1, text:'hello'})";
        Node node = p(input);
        assertThat(node, instanceOf(BlockNode.class));

        BlockNode block = (BlockNode) node;
        assertThat(block.statements.size(), is(1));
        assertThat(block.start().getPosition(), is(1));
        assertThat(block.end().getPosition(), is(input.length()));

        Node firstNode = block.statements.get(0);
        assertThat(firstNode, instanceOf(AssignNode.class));

        AssignNode assignNode = (AssignNode) firstNode;
        assertThat(assignNode.target, instanceOf(VariableNode.class));
        assertThat(assignNode.value, instanceOf(FunctionNode.class));

        VariableNode target = (VariableNode) assignNode.target;
        assertThat(target.name, is("A"));

        FunctionNode value = (FunctionNode) assignNode.value;
        assertThat(value.functionNode, instanceOf(ValueNode.class));
        assertThat(((ValueNode)value.functionNode).value.asString(), is("node"));
        assertThat(value.arguments.size(), is(1));

        Node argument = value.arguments.get(0);
        assertThat(argument, instanceOf(MapNode.class));

        MapNode mapNode = (MapNode) argument;
        assertThat(mapNode.elements.size(), is(2));
        assertThat(mapNode.elements, allOf(
                hasKey("x"), hasKey("text")
        ));
    }

    @Test
    void testParseErrors() {
        assertThrows(ParseException.class, () -> p("A ="));
        assertThrows(ParseException.class, () -> p("1 = A"));
        assertThrows(ParseException.class, () -> p("{A = B}"));
    }
}