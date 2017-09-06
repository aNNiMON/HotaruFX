package com.annimon.hotarufx.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.text.Text;

public class LineText extends Text {

    private final BooleanProperty endLineBreak = new SimpleBooleanProperty(true);
    private final StringProperty content = new SimpleStringProperty("");

    public LineText() {
        init();
    }

    public LineText(String text) {
        super(text);
        init();
    }

    public LineText(double x, double y, String text) {
        super(x, y, text);
        init();
    }

    private void init() {
        getStyleClass().add("linetext");
        setContent(getText());
        textProperty().bind(
                Bindings.when(endLineBreak)
                        .then(content.concat("\n"))
                        .otherwise(content)
        );
    }

    public String getContent() {
        return content.get();
    }

    public StringProperty contentProperty() {
        return content;
    }

    public void setContent(String content) {
        this.content.set(content.replace("\\n", "\n"));
    }

    public boolean isEndLineBreak() {
        return endLineBreak.get();
    }

    public BooleanProperty endLineBreakProperty() {
        return endLineBreak;
    }

    public void setEndLineBreak(boolean endLineBreak) {
        this.endLineBreak.set(endLineBreak);
    }
}
