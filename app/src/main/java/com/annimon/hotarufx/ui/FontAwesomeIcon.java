package com.annimon.hotarufx.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.text.Text;

public class FontAwesomeIcon extends Text {

    private final StringProperty icon = new SimpleStringProperty();

    public FontAwesomeIcon() {
        init();
    }

    public FontAwesomeIcon(String text) {
        super(text);
        init();
    }

    public FontAwesomeIcon(double x, double y, String text) {
        super(x, y, text);
        init();
    }

    private void init() {
        setFont(FontAwesome.FONT);
        getStyleClass().add("fa-icon");
        icon.addListener((observable, oldValue, newValue) -> {
            String faIcon = FontAwesome.MAPPING.get(newValue);
            if (faIcon != null) {
                setText(faIcon);
            }
        });
        setIcon(getText());
    }

    public String getIcon() {
        return icon.get();
    }

    public void setIcon(String icon) {
        this.icon.set(icon);
    }

    public StringProperty iconProperty() {
        return icon;
    }
}
