package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.Function;
import com.annimon.hotarufx.lib.NumberValue;
import com.annimon.hotarufx.lib.Validator;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import static com.annimon.hotarufx.bundles.FunctionInfo.of;
import static com.annimon.hotarufx.bundles.FunctionType.COMMON;

public class AssertionsBundle implements Bundle {

    private static final Map<String, FunctionInfo> FUNCTIONS;
    static {
        FUNCTIONS = Map.ofEntries(
                Map.entry("assertHasVariable", of(COMMON, AssertionsBundle::assertHasVariable)),
                Map.entry("assertHasFunction", of(COMMON, AssertionsBundle::assertHasFunction)),
                Map.entry("assertTrue", of(COMMON, AssertionsBundle::assertTrue)),
                Map.entry("assertEquals", of(COMMON, AssertionsBundle::assertEquals))
        );
    }

    @Override
    public Map<String, FunctionInfo> functionsInfo() {
        return FUNCTIONS;
    }

    private static Function assertHasVariable(Context context) {
        return args -> {
            Validator.with(args).check(1);
            final var name = args[0].asString();
            Assertions.assertTrue(context.variables().containsKey(name));
            return NumberValue.ZERO;
        };
    }

    private static Function assertHasFunction(Context context) {
        return args -> {
            Validator.with(args).check(1);
            final var name = args[0].asString();
            Assertions.assertTrue(context.functions().containsKey(name));
            return NumberValue.ZERO;
        };
    }

    private static Function assertTrue(Context context) {
        return args -> {
            Validator.with(args).check(1);
            Assertions.assertTrue(args[0].asBoolean());
            return NumberValue.ZERO;
        };
    }

    private static Function assertEquals(Context context) {
        return args -> {
            Validator.with(args).check(2);
            final var expectedValue = args[0];
            final var actualValue = args[1];
            Assertions.assertEquals(expectedValue, actualValue);
            return NumberValue.ZERO;
        };
    }
}
