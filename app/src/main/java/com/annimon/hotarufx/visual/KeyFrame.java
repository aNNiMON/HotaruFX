package com.annimon.hotarufx.visual;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName="of")
@EqualsAndHashCode
public class KeyFrame implements Comparable<KeyFrame> {

    @Getter
    private final int frame;

    @Override
    public int compareTo(KeyFrame o) {
        return Integer.compare(frame, o.frame);
    }
}
