package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.NumberValue;
import com.annimon.hotarufx.visual.Composition;
import lombok.val;

public class CompositionBundle implements Bundle {

    @Override
    public void load(Context context) {
        context.functions().put("composition", args -> {
            final int width, height;
            final double frameRate;
            switch (args.length) {
                case 0:
                    width = 1280;
                    height = 720;
                    frameRate = 30d;
                    break;
                case 1:
                    width = 1280;
                    height = 720;
                    frameRate = args[0].asNumber();
                    break;
                case 2:
                    width = args[0].asInt();
                    height = args[1].asInt();
                    frameRate = 30d;
                    break;
                case 3:
                default:
                    width = args[0].asInt();
                    height = args[1].asInt();
                    frameRate = args[2].asNumber();
                    break;
            }
            val composition = new Composition(width, height, frameRate);
            val scene = composition.getScene();
            context.composition(composition);
            context.variables().put("Width", NumberValue.of(scene.getVirtualWidth()));
            context.variables().put("Height", NumberValue.of(scene.getVirtualHeight()));
            context.variables().put("HalfWidth", NumberValue.of(scene.getVirtualWidth() / 2));
            context.variables().put("HalfHeight", NumberValue.of(scene.getVirtualHeight() / 2));
            return NumberValue.ZERO;
        });
    }
}
