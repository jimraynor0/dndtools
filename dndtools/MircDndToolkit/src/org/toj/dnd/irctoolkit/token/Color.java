package org.toj.dnd.irctoolkit.token;

import java.io.Serializable;

public enum Color implements Serializable {
    WHITE(0, java.awt.Color.WHITE, "WHITE"),
    BLACK(1, java.awt.Color.BLACK, "BLACK"),
    NAVY(2, java.awt.Color.BLUE.darker(), "NAVY"),
    GREEN(3, java.awt.Color.GREEN, "GREEN"),
    RED(4, java.awt.Color.RED, "RED"),
    MAROON(5, java.awt.Color.RED.darker(), "MAROON"),
    PURPLE(6, java.awt.Color.PINK.darker(), "PURPLE"),
    ORANGE(7, java.awt.Color.ORANGE, "ORANGE"),
    YELLOW(8, java.awt.Color.YELLOW, "YELLOW"),
    LIME(9, java.awt.Color.CYAN.brighter(), "LIME"),
    TEAL(10, java.awt.Color.CYAN.darker(), "TEAL"),
    AQUA(11, java.awt.Color.GREEN.brighter(), "AQUA"),
    BLUE(12, java.awt.Color.BLUE, "BLUE"),
    FUCHSIA(13, java.awt.Color.PINK.darker().darker(), "FUCHSIA"),
    GRAY(14, java.awt.Color.GRAY, "GRAY"),
    SILVER(15, java.awt.Color.GRAY.brighter(), "SILVER");

    private int code;
    private java.awt.Color color;
    private String name;

    private Color(int code, java.awt.Color color, String name) {
        this.code = code;
        this.color = color;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public java.awt.Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public static Color forName(String name) {
        for (Color color : Color.values()) {
            if (color.getName().equalsIgnoreCase(name)) {
                return color;
            }
        }
        return null;
    }
}
