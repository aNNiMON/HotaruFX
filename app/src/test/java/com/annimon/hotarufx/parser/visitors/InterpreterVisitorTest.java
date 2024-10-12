package com.annimon.hotarufx.parser.visitors;

import com.annimon.hotarufx.bundles.BundleLoader;
import com.annimon.hotarufx.bundles.CompositionBundle;
import com.annimon.hotarufx.exceptions.FunctionNotFoundException;
import com.annimon.hotarufx.exceptions.HotaruRuntimeException;
import com.annimon.hotarufx.exceptions.VariableNotFoundException;
import com.annimon.hotarufx.lexer.HotaruLexer;
import com.annimon.hotarufx.lib.ArrayValue;
import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.MapValue;
import com.annimon.hotarufx.lib.NumberValue;
import com.annimon.hotarufx.lib.StringValue;
import com.annimon.hotarufx.lib.Value;
import com.annimon.hotarufx.parser.HotaruParser;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
    void testBinaryOps() {
        assertThat(eval("A = 1 + 2").asInt(), is(3));
        assertThat(eval("A = 1 + -3").asInt(), is(-2));
        assertThat(eval("A = -1 + -3").asInt(), is(-4));
        assertThat(eval("A = 4 - 3").asInt(), is(1));
        assertThat(eval("A = -1 - -3").asInt(), is(2));
    }

    @Test
    void testUnaryOps() {
        assertThat(eval("A = -1").asInt(), is(-1));
    }

    @Test
    void testArray() {
        Value value = eval("A = [0, 1, 'hello']");
        assertThat(value, instanceOf(ArrayValue.class));

        ArrayValue arrayValue = (ArrayValue) value;
        assertThat(arrayValue, contains(
                NumberValue.of(0),
                NumberValue.of(1),
                new StringValue("hello")
        ));
    }

    @Test
    void testMap() {
        Value value = eval("A = {x: 0, y: 1, text: 'hello'}");
        assertThat(value, instanceOf(MapValue.class));

        Map<String, Value> map = ((MapValue) value).getMap();
        assertThat(map, allOf(
                hasEntry("x", NumberValue.of(0)),
                hasEntry("y", NumberValue.of(1))
        ));
        assertThat(map, hasEntry("text", new StringValue("hello")));
    }

    @Test
    void testMapAccess() {
        Context context = new Context();
        eval("""
                A = {x: 0, y: 22, z: 0, text: 'hello'}
                A.x = 20
                A.z = A.y
                A.newKey = 'newValue'""", context);
        Value value = context.variables().get("A");

        assertThat(value, instanceOf(MapValue.class));

        Map<String, Value> map = ((MapValue) value).getMap();
        assertThat(map, allOf(
                hasEntry("x", NumberValue.of(20)),
                hasEntry("y", NumberValue.of(22)),
                hasEntry("z", NumberValue.of(22))
        ));
        assertThat(map, allOf(
                hasEntry("text", new StringValue("hello")),
                hasEntry("newKey", new StringValue("newValue"))
        ));
    }

    @Test
    void testFunction() {
        Context context = new Context();
        context.functions().put("ten", args -> NumberValue.of(10));
        context.functions().put("adder", args -> NumberValue.of(
                Arrays.stream(args).mapToInt(Value::asInt).sum() ));
        Value value;

        value = eval("y = ten(10)", context);
        assertThat(value, is(NumberValue.of(10)));

        value = eval("x = adder(1, 2, 3, 4, 5)", context);
        assertThat(value, is(NumberValue.of(15)));

        value = eval("adder(ten(), ten())", context);
        assertThat(value, is(NumberValue.of(20)));
    }

    @Test
    void testUnits() {
        final var context = new Context();
        BundleLoader.loadSingle(context, CompositionBundle.class);
        context.functions().put("rate", context.functions().get("composition"));
        Value value;

        value = eval("rate(30) t = 1 sec", context);
        assertThat(value.asDouble(), closeTo(30, 0.0001));

        value = eval("rate(30) t = 5 sec", context);
        assertThat(value.asDouble(), closeTo(150, 0.0001));

        value = eval("rate(30) t = 0.5 sec", context);
        assertThat(value.asDouble(), closeTo(15, 0.0001));

        value = eval("rate(30) t = 1000 ms", context);
        assertThat(value.asDouble(), closeTo(30, 0.0001));

        value = eval("rate(30) t = 333 ms", context);
        assertThat(value.asDouble(), closeTo(10, 0.01));

        value = eval("rate(25) t = 1 sec", context);
        assertThat(value.asDouble(), closeTo(25, 0.0001));

        value = eval("rate(25) t = 1000 ms", context);
        assertThat(value.asDouble(), closeTo(25, 0.0001));
    }

    @Test
    void testRuntimeErrors() {
        assertThrows(VariableNotFoundException.class, () -> eval("A = B"));
        assertThrows(FunctionNotFoundException.class, () -> eval("test()"));
        assertThrows(HotaruRuntimeException.class, () -> eval("A = '2' + 70"));
    }
}