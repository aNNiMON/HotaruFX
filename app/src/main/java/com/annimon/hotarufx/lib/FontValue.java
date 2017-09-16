package com.annimon.hotarufx.lib;

import com.annimon.hotarufx.exceptions.TypeException;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import lombok.val;

public class FontValue extends MapValue {

    public static Font toFont(MapValue mapValue) {
        val map = mapValue.getMap();
        val family = map.getOrDefault("family", new StringValue(Font.getDefault().getFamily())).asString();
        val weight = map.getOrDefault("weight", NumberValue.of(FontWeight.NORMAL.getWeight())).asInt();
        val isItalic = map.getOrDefault("italic", NumberValue.ZERO).asBoolean();
        val posture = isItalic ? FontPosture.ITALIC : FontPosture.REGULAR;
        val size = map.getOrDefault("size", NumberValue.MINUS_ONE).asDouble();
        return Font.font(family, FontWeight.findByWeight(weight), posture, size);
    }

    private final Font font;

    public FontValue(Font font) {
        super(4);
        this.font = font;
        init();
    }

    private void init() {
        val map = super.getMap();
        map.put("family", new StringValue(font.getFamily()));
        map.put("isItalic", NumberValue.fromBoolean(font.getStyle().toLowerCase().contains("italic")));
        val weight = FontWeight.findByName(font.getStyle());
        map.put("weight", NumberValue.of(weight != null
                ? (weight.getWeight())
                : FontWeight.NORMAL.getWeight()));
        map.put("size", NumberValue.of(font.getSize()));
    }

    @Override
    public int type() {
        return Types.MAP;
    }

    public Font getFont() {
        return font;
    }

    @Override
    public Object raw() {
        return font;
    }

    @Override
    public Number asNumber() {
        throw new TypeException("Cannot cast font to number");
    }

    @Override
    public String asString() {
        throw new TypeException("Cannot cast font to string");
    }

    @Override
    public int compareTo(Value o) {
        return 0;
    }
}
