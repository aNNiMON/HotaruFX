package com.annimon.hotarufx.visual;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class CompositionTest {

    @Test
    void testVirtualSize() {
        Composition composition;
        composition = new Composition(1280, 720, Color.WHITE, new Group());
        assertThat(composition.getVirtualWidth(), is(1920));

        composition = new Composition(1280, 1280, Color.WHITE, new Group());
        assertThat(composition.getVirtualWidth(), is(1080));
    }
}