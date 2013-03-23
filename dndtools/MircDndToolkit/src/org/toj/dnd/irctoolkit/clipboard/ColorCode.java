package org.toj.dnd.irctoolkit.clipboard;

import org.toj.dnd.irctoolkit.token.Color;

public class ColorCode {
    private static final Color[] COLORS = { Color.WHITE, Color.BLACK,
            Color.NAVY, Color.GREEN, Color.RED, Color.MAROON, Color.PURPLE,
            Color.ORANGE, Color.YELLOW, Color.AQUA, Color.TEAL, Color.LIME,
            Color.BLUE, Color.FUCHSIA, Color.GRAY, Color.SILVER };

    public static Color toColor(int code) {
        return COLORS[code];
    }

    public static int toCode(Color color) {
        for (int i = 0; i < COLORS.length; i++) {
            if (color == COLORS[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int getColorCount() {
        return COLORS.length;
    }
}
