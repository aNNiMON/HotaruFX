package com.annimon.hotarufx.visual;

import lombok.val;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.*;

class TimeLineTest {

    @Test
    void add() {
        val timeline = new TimeLine();
        timeline.add(KeyFrame.of(20), null);
        timeline.add(KeyFrame.of(10), null);
        timeline.add(KeyFrame.of(0), null);
        timeline.add(KeyFrame.of(1), null);

        assertThat(timeline.getKeyFrames().keySet(), contains(
                KeyFrame.of(0), KeyFrame.of(1),
                KeyFrame.of(10), KeyFrame.of(20)
        ));
    }
}