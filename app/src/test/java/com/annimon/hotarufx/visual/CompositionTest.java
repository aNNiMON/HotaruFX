package com.annimon.hotarufx.visual;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class CompositionTest {

    @Test
    void testVirtualSize() {
        Composition composition;
        composition = new Composition(1280, 720, 30);
        assertThat(composition.getVirtualWidth(), is(1920));

        composition = new Composition(1280, 1280, 30);
        assertThat(composition.getVirtualWidth(), is(1080));
    }
}