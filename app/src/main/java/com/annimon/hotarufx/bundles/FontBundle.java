package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.FontValue;
import com.annimon.hotarufx.lib.Function;
import com.annimon.hotarufx.lib.MapValue;
import com.annimon.hotarufx.lib.Types;
import com.annimon.hotarufx.lib.Validator;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.text.Font;
import static com.annimon.hotarufx.bundles.FunctionInfo.of;
import static com.annimon.hotarufx.bundles.FunctionType.COMMON;

public class FontBundle implements Bundle {

    private static final Map<String, FunctionInfo> FUNCTIONS;
    static {
        FUNCTIONS = new HashMap<>();
        FUNCTIONS.put("font", of(COMMON, FontBundle::newFont));
    }

    @Override
    public Map<String, FunctionInfo> functionsInfo() {
        return FUNCTIONS;
    }

    private static Function newFont(Context context) {
        return args -> {
            final var validator = Validator.with(args);
            validator.check(1);
            if (args[0].type() == Types.MAP) {
                return new FontValue(FontValue.toFont((MapValue) args[0]));
            }
            return new FontValue(Font.font(args[0].asDouble()));
        };
    }
}
