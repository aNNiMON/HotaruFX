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
            CLIPBOARD = "\uf0ea",
            COPYRIGHT = "\uf1f9",
            FONT_AWESOME = "\uf2b4",
            GITHUB = "\uf09b",
            GLOBE = "\uf0ac",
            PENCIL = "\uf040",
            PLAY = "\uf04b",
            REDO = "\uf01e",
            UNDO = "\uf0e2"
            ;

    static final Map<String, String> MAPPING;
    static {
        MAPPING = new HashMap<>();
        MAPPING.put("clipboard", CLIPBOARD);
        MAPPING.put("copyright", COPYRIGHT);
        MAPPING.put("earth", GLOBE);
        MAPPING.put("font-awesome", FONT_AWESOME);
        MAPPING.put("github", GITHUB);
        MAPPING.put("globe", GLOBE);
        MAPPING.put("pencil", PENCIL);
        MAPPING.put("play", PLAY);
        MAPPING.put("redo", REDO);
        MAPPING.put("undo", UNDO);
    }
}
