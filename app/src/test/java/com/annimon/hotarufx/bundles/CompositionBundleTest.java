package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.NumberValue;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class CompositionBundleTest {

    @Test
    void testBundle() {
        final var context = new Context();
        BundleLoader.loadSingle(context, CompositionBundle.class);

        assertThat(context.functions(), hasKey("composition"));
        assertThat(context.composition(), nullValue());
        assertThat(context.variables(), allOf(
                not(hasKey("Width")),
                not(hasKey("Height")),
                not(hasKey("HalfWidth")),
                not(hasKey("HalfHeight"))
        ));

        context.functions().get("composition").execute();

        assertThat(context.composition(), notNullValue());
        assertThat(context.variables(), allOf(
                hasEntry("Width", NumberValue.of(1920)),
                hasEntry("Height", NumberValue.of(1080)),
                hasEntry("HalfWidth", NumberValue.of(960)),
                hasEntry("HalfHeight", NumberValue.of(540))
        ));
    }
}