package org.toj.dnd.irctoolkit.util;

import java.util.Arrays;
import java.util.List;

import org.toj.dnd.irctoolkit.map.Point;

public class AxisUtil {
    private static final List<String> SUFFIXES = Arrays.asList("0", "1", "2",
            "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z");

    public static int supportsAxisLength() {
        return SUFFIXES.size();
    }

    public static int toNumber(String label) {
        return SUFFIXES.indexOf(label.substring(1));
    }

    public static String toString(String prefix, int i) {
        return prefix + SUFFIXES.get(i);
    }

    public static boolean is2DAxis(String str) {
        return str.matches("x.y.");
    }

    public static Point parse2DAxis(String str) {
        Point p = new Point();
        p.setX(toNumber(str.substring(0, 2)));
        p.setY(toNumber(str.substring(2)));
        return p;
    }
}
