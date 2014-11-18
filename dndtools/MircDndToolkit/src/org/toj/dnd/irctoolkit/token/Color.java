package org.toj.dnd.irctoolkit.token;

import java.io.Serializable;

public enum Color implements Serializable {
    WHITE(0, java.awt.Color.WHITE, "WHITE"),
    BLACK(1, java.awt.Color.BLACK, "BLACK"),
    NAVY(2, new java.awt.Color(0, 0, 127), "NAVY"),
    GREEN(3, new java.awt.Color(0, 127, 0), "GREEN"),
    RED(4, java.awt.Color.RED, "RED"),
    MAROON(5, new java.awt.Color(127, 0, 0), "MAROON"),
    PURPLE(6, new java.awt.Color(127, 0, 127), "PURPLE"),
    ORANGE(7, new java.awt.Color(255, 127, 0), "ORANGE"),
    YELLOW(8, java.awt.Color.YELLOW, "YELLOW"),
    LIME(9, java.awt.Color.GREEN, "LIME"),
    TEAL(10, new java.awt.Color(0, 127, 127), "TEAL"),
    AQUA(11, new java.awt.Color(0, 255, 255), "AQUA"),
    BLUE(12, java.awt.Color.BLUE, "BLUE"),
    FUCHSIA(13, new java.awt.Color(255, 0, 255), "FUCHSIA"),
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
