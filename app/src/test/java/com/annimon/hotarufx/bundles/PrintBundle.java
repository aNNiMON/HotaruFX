package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.Function;
import com.annimon.hotarufx.lib.NumberValue;
import java.util.HashMap;
import java.util.Map;
import lombok.val;
import static com.annimon.hotarufx.bundles.FunctionInfo.of;
import static com.annimon.hotarufx.bundles.FunctionType.COMMON;

public class PrintBundle implements Bundle {

    private static final Map<String, FunctionInfo> FUNCTIONS;
    static {
        FUNCTIONS = new HashMap<>();
        FUNCTIONS.put("print", of(COMMON, PrintBundle::print));
        FUNCTIONS.put("println", of(COMMON, PrintBundle::println));
        FUNCTIONS.put("dump", of(COMMON, PrintBundle::dump));
    }

    @Override
    public Map<String, FunctionInfo> functionsInfo() {
        return FUNCTIONS;
    }

    private static Function print(Context context) {
        return args -> {
            if (args.length > 0) {
                System.out.print(args[0]);
            }
            return NumberValue.ZERO;
        };
    }

    private static Function println(Context context) {
        return args -> {
            if (args.length > 0) {
                System.out.println(args[0]);
            } else {
                System.out.println();
            }
            return NumberValue.ZERO;
        };
    }

    private static Function dump(Context context) {
        return args -> {
            val maxVariableLength = context.variables()
                    .keySet().stream()
                    .mapToInt(String::length)
                    .max()
                    .orElse(20);
            System.out.println("\n\tVARIABLES");
            val format = "%"+maxVariableLength+"s   %s%n";
            context.variables().forEach((k, v) -> {
                System.out.printf(format, k, v);
            });

            System.out.println("\n\tFUNCTIONS");
            context.functions().keySet().forEach(System.out::println);
            return NumberValue.ZERO;
        };
    }
}
