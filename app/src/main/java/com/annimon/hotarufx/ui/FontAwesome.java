package com.annimon.hotarufx.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.text.Font;
import javafx.util.Pair;

public enum FontAwesome {

    CLIPBOARD("\uf0ea", "clipboard", "paste"),
    CLONE("\uf24d", "clone"),
    COPY("\uf0c5", "copy"),
    COPYRIGHT("\uf1f9", "copyright"),
    FONT_AWESOME("\uf2b4", "font-awesome"),
    GITHUB("\uf09b", "github"),
    GLOBE("\uf0ac", "earth", "globe"),
    PENCIL("\uf040", "pencil"),
    PLAY("\uf04b", "play"),
    REDO("\uf01e", "redo"),
    SCISSORS("\uf0c4", "scissors", "cut"),
    UNDO("\uf0e2", "undo");

    private final String symbol;
    private final List<String> names;

    FontAwesome(String symbol, String name, String... aliases) {
        this.symbol = symbol;
        names = new ArrayList<>();
        names.add(name);
        if (aliases.length > 0) {
            names.addAll(Arrays.asList(aliases));
        }
    }

    public static String getIcon(String name) {
        return MAPPING.get(name);
    }

    static final Font FONT;
    static {
        URL resource = FontAwesome.class.getResource("/fontawesome.ttf");
        FONT = Font.loadFont(resource.toExternalForm(), 16);
    }

    static final Map<String, String> MAPPING;
    static {
        MAPPING = Arrays.stream(FontAwesome.values())
                .flatMap(f -> f.names.stream().map(name -> new Pair<>(name, f.symbol)))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }
}
