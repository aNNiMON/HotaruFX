package com.annimon.hotarufx.visual;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

class TimeLineTest {

    @Test
    void add() {
        final var timeline = new PropertyTimeline<String>(null);
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