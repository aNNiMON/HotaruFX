package com.annimon.hotarufx.visual.objects;

import com.annimon.hotarufx.visual.PropertyBindings;
import com.annimon.hotarufx.visual.PropertyTimeline;
import com.annimon.hotarufx.visual.PropertyTimelineHolder;
import com.annimon.hotarufx.visual.TimeLine;
import com.annimon.hotarufx.visual.visitors.NodeVisitor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import java.util.ArrayList;
import java.util.List;
import static com.annimon.hotarufx.visual.PropertyType.PAINT;

public class GuideGridNode extends ObjectNode {

    public final Group group;
    public final List<Line> lines;

    private final PropertyTimelineHolder<Paint> stroke;
    private final ObjectProperty<Paint> strokeProperty = new SimpleObjectProperty<>(Color.BLACK);

    public GuideGridNode(int stepX, int stepY, int width, int height) {
        this(new Group(), stepX, stepY, width, height);
    }

    private GuideGridNode(Group group, int stepX, int stepY, int width, int height) {
        super(group);
        this.group = group;
        this.lines = new ArrayList<>();
        stroke = PropertyTimelineHolder.empty();

        final int halfW = width / 2;
        final int halfH = height / 2;
        lines.add(new Line(-halfW, 0, halfW, 0));
        for (double yy = stepY; yy < halfH; yy += stepY) {
            lines.add(new Line(-halfW, yy, halfW, yy));
            lines.add(new Line(-halfW, -yy, halfW, -yy));
        }
        lines.add(new Line(0, -halfH, 0, halfH));
        for (double xx = stepX; xx < halfW; xx += stepX) {
            lines.add(new Line(xx, -halfH, xx, halfH));
            lines.add(new Line(-xx, -halfH, -xx, halfH));
        }
        group.getChildren().addAll(lines);
        for (Line line : lines) {
            line.strokeProperty().bind(strokeProperty);
        }
    }

    public PropertyTimeline<Paint> strokeProperty() {
        return stroke.setIfEmptyThenGet(() -> strokeProperty);
    }

    @Override
    public void buildTimeline(TimeLine timeline) {
        super.buildTimeline(timeline);
        stroke.applyIfPresent(timeline);
    }

    @Override
    public PropertyBindings propertyBindings(PropertyBindings bindings) {
        return super.propertyBindings(bindings)
                .add("stroke", PAINT, this::strokeProperty);
    }

    @Override
    public <R, T> R accept(NodeVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
