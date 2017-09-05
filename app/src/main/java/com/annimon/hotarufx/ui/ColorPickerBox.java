package com.annimon.hotarufx.ui;

import java.util.regex.Pattern;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.StringConverter;

/**
 * See https://stackoverflow.com/questions/27171885
 */
public class ColorPickerBox extends VBox {

    private final ObjectProperty<Color> currentColorProperty = new SimpleObjectProperty<>(Color.WHITE);
    private final ObjectProperty<Color> customColorProperty = new SimpleObjectProperty<>(Color.TRANSPARENT);

    private final DoubleProperty hue = new SimpleDoubleProperty(-1);
    private final DoubleProperty sat = new SimpleDoubleProperty(-1);
    private final DoubleProperty bright = new SimpleDoubleProperty(-1);
    private final DoubleProperty alpha = new SimpleDoubleProperty(100) {
        @Override
        protected void invalidated() {
            final Color c = customColorProperty.get();
            customColorProperty.set(new Color(
                    c.getRed(), c.getGreen(), c.getBlue(),
                    clamp(alpha.get() / 100.0)
            ));
        }
    };

    private final Region colorRectIndicator;

    public ColorPickerBox() {
        getStyleClass().add("color-picker-box");

        customColorProperty().addListener((ov, t, t1) -> colorChanged());

        /* Hue bar */
        final Pane hueBar = new Pane();
        hueBar.getStyleClass().add("hue-bar");
        hueBar.setBackground(new Background(new BackgroundFill(
                createHueGradient(),
                CornerRadii.EMPTY, Insets.EMPTY)));

        final Region hueBarIndicator = new Region();
        hueBarIndicator.setId("hue-bar-indicator");
        hueBarIndicator.setMouseTransparent(true);
        hueBarIndicator.setCache(true);

        /* Saturation and value rect */
        final Pane colorRect = new StackPane();
        colorRect.getStyleClass().add("color-rect");

        final Pane colorRectBg = new Pane();
        colorRectBg.backgroundProperty().bind(new ObjectBinding<Background>() {
            {
                bind(hue);
            }
            @Override
            protected Background computeValue() {
                return new Background(new BackgroundFill(
                        Color.hsb(hue.getValue(), 1.0, 1.0),
                        CornerRadii.EMPTY, Insets.EMPTY));
            }
        });

        final Pane colorRectOverlayWhite = new Pane();
        colorRectOverlayWhite.getStyleClass().add("color-rect");
        colorRectOverlayWhite.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.gray(1, 1)),
                        new Stop(1, Color.gray(1, 0))),
                CornerRadii.EMPTY, Insets.EMPTY)));

        final Pane colorRectOverlayBlack = new Pane();
        colorRectOverlayBlack.getStyleClass().add("color-rect");
        colorRectOverlayBlack.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.gray(0, 0)),
                        new Stop(1, Color.gray(0, 1))),
                CornerRadii.EMPTY, Insets.EMPTY)));

        final Pane colorRectBlackBorder = new Pane();
        colorRectBlackBorder.setMouseTransparent(true);
        colorRectBlackBorder.getStyleClass().addAll("color-rect", "color-rect-border");

        colorRectIndicator = new Region();
        colorRectIndicator.setId("color-rect-indicator");
        colorRectIndicator.setManaged(false);
        colorRectIndicator.setMouseTransparent(true);
        colorRectIndicator.setCache(true);

        /* New color rect */
        final Pane newColorRect = new Pane();
        newColorRect.getStyleClass().addAll("color-new-rect");
        newColorRect.setId("new-color");
        newColorRect.backgroundProperty().bind(new ObjectBinding<Background>() {
            {
                bind(customColorProperty);
            }
            @Override
            protected Background computeValue() {
                return new Background(new BackgroundFill(
                        customColorProperty.get(), CornerRadii.EMPTY, Insets.EMPTY));
            }
        });

        /* Color value and copy button */
        final HBox statusPane = new HBox();
        final TextField colorValue = new TextField();
        colorValue.textProperty().bindBidirectional(customColorProperty, new StringConverter<Color>() {
            final Pattern pattern = Pattern.compile("^#([0-9a-f]{3,4}|[0-9a-f]{6}|[0-9a-f]{8})$");
            @Override
            public String toString(Color color) {
                int r = (int) Math.round(color.getRed() * 255.0);
                int g = (int) Math.round(color.getGreen() * 255.0);
                int b = (int) Math.round(color.getBlue() * 255.0);
                int o = (int) Math.round(color.getOpacity() * 255.0);
                String opacity = "";
                if (o < 255) {
                    opacity = String.format("%02x", o);
                }
                return String.format("#%02x%02x%02x%s", r, g, b, opacity);
            }
            @Override
            public Color fromString(String string) {
                if (pattern.matcher(string).matches()) {
                    return Color.valueOf(string);
                }
                return customColorProperty.get();
            }
        });
        final Button copyButton = new Button("copy");
        copyButton.setOnAction(e -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(colorValue.getText());
            clipboard.setContent(content);
        });

        /* Event bindings */
        EventHandler<MouseEvent> hueBarMouseHandler = event -> {
            final double x = event.getX();
            hue.set(clamp(x / colorRect.getWidth()) * 360);
            updateHSBColor();
        };
        hueBar.setOnMouseDragged(hueBarMouseHandler);
        hueBar.setOnMousePressed(hueBarMouseHandler);

        final EventHandler<MouseEvent> rectMouseHandler = event -> {
            final double x = event.getX();
            final double y = event.getY();
            sat.set(clamp(x / colorRect.getWidth()) * 100);
            bright.set(100 - (clamp(y / colorRect.getHeight()) * 100));
            final double currentHue = hue.get();
            updateHSBColor();
            hue.setValue(currentHue);
        };
        colorRectOverlayBlack.setOnMouseDragged(rectMouseHandler);
        colorRectOverlayBlack.setOnMousePressed(rectMouseHandler);

        /* Layout bindings */
        hueBarIndicator.layoutXProperty().bind(
                hue.divide(360).multiply(hueBar.widthProperty()));
        colorRectIndicator.layoutXProperty().bind(
                sat.divide(100).multiply(colorRect.widthProperty()));
        colorRectIndicator.layoutYProperty().bind(
                Bindings.subtract(1, bright.divide(100)).multiply(colorRect.heightProperty()));
        newColorRect.opacityProperty().bind(alpha.divide(100));

        /* Adding controls */
        hueBar.getChildren().setAll(hueBarIndicator);
        colorRect.getChildren().setAll(colorRectBg, colorRectOverlayWhite, colorRectOverlayBlack,
                colorRectBlackBorder, colorRectIndicator);
        VBox.setVgrow(colorRect, Priority.SOMETIMES);
        HBox.setHgrow(colorValue, Priority.SOMETIMES);
        statusPane.getChildren().setAll(colorValue, copyButton);
        getChildren().addAll(hueBar, colorRect, newColorRect, statusPane);

        if (currentColorProperty.get() == null) {
            currentColorProperty.set(Color.TRANSPARENT);
        }
        updateValues();
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColorProperty.set(currentColor);
        updateValues();
    }

    public final ObjectProperty<Color> customColorProperty() {
        return customColorProperty;
    }

    private void updateValues() {
        final Color c = currentColorProperty.get();
        hue.set(c.getHue());
        sat.set(c.getSaturation() * 100d);
        bright.set(c.getBrightness() * 100);
        alpha.set(c.getOpacity() * 100);
        updateHSBColor();
    }

    private void colorChanged() {
        final Color c = customColorProperty.get();
        hue.set(c.getHue());
        sat.set(c.getSaturation() * 100);
        bright.set(c.getBrightness() * 100);
    }

    private void updateHSBColor() {
        customColorProperty.set(Color.hsb(
                hue.get(),
                clamp(sat.get() / 100d),
                clamp(bright.get() / 100d),
                clamp(alpha.get() / 100d)
        ));
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        colorRectIndicator.autosize();
    }

    private static double clamp(double value) {
        return (value < 0) ? 0
                           : (value > 1) ? 1 : value;
    }

    private static LinearGradient createHueGradient() {
        final Stop[] stops = new Stop[255];
        for (int x = 0; x < 255; x++) {
            final double offset = (1.0 / 255.0) * x;
            final int hue = (int)((x / 255.0) * 360);
            stops[x] = new Stop(offset, Color.hsb(hue, 1.0, 1.0));
        }
        return new LinearGradient(0f, 0f, 1f, 0f, true, CycleMethod.NO_CYCLE, stops);
    }
}
