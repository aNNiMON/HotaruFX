package com.annimon.hotarufx.ui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.text.Font;

public class FontAwesome {

    static final Font FONT;
    static {
        URL resource = FontAwesome.class.getResource("/fontawesome.ttf");
        FONT = Font.loadFont(resource.toExternalForm(), 16);
    }

    public static final String
            UNDO = "\uf0e2",
            REDO = "\uf01e",
            CLIPBOARD = "\uf0ea"
            ;

    static final Map<String, String> MAPPING;
    static {
        MAPPING = new HashMap<>();
        MAPPING.put("undo", UNDO);
        MAPPING.put("redo", REDO);
        MAPPING.put("clipboard", CLIPBOARD);
    }
}
