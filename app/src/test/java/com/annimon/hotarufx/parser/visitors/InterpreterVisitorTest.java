package com.annimon.hotarufx.parser.visitors;

import com.annimon.hotarufx.exceptions.VariableNotFoundException;
import com.annimon.hotarufx.lexer.HotaruLexer;
import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.NumberValue;
import com.annimon.hotarufx.lib.Value;
import com.annimon.hotarufx.parser.HotaruParser;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InterpreterVisitorTest {

    static Value eval(String input) {
        return eval(input, new Context());
    }

    static Value eval(String input, Context context) {
        return HotaruParser.parse(HotaruLexer.tokenize(input))
                .accept(new InterpreterVisitor(), context);
    }

    @Test
    void testAssignVariables() {
        Context context = new Context();

        String input = "A = 1\nB = 2\nC = B";
        Value result = eval(input, context);

        assertThat(result.toString(), is("2"));
        assertThat(context.variables(), allOf(
                hasEntry("A", NumberValue.of(1)),
                hasEntry("B", NumberValue.of(2)),
                hasEntry("C", NumberValue.of(2))
        ));
    }

    @Test
    void testUnaryOps() {
        assertThat(eval("A = -1").asInt(), is(-1));
    }

    @Test
    void testRuntimeErrors() {
        assertThrows(VariableNotFoundException.class, () -> eval("A = B"));
    }
}