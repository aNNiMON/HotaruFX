package com.annimon.hotarufx.ui;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

public class LibraryItem extends Button {

    private final StringProperty code = new SimpleStringProperty("");

    public LibraryItem() {
        init();
    }

    public LibraryItem(String text) {
        super(text);
        init();
    }

    public LibraryItem(String text, Node graphic) {
        super(text, graphic);
        init();
    }

    private void init() {
        setMnemonicParsing(false);
        tooltipProperty().bind(new ObjectBinding<Tooltip>() {
            { bind(code); }
            @Override
            protected Tooltip computeValue() {
                return new Tooltip(code.get());
            }
        });
    }

    public String getCode() {
        return code.get();
    }

    public StringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code.set(code
                .replace("\\n", "\n")
                .replaceAll("\\n\\s+", "\n")
                .replace('_', ' ')
        );
    }
}
