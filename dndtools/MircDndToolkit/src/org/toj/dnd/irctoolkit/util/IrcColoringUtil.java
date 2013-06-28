package org.toj.dnd.irctoolkit.util;

public class IrcColoringUtil {
    public static String paint(String text, int colorCode) {
        return new StringBuilder().append((char) 3)
                .append(colorCode < 10 ? "0" + colorCode : colorCode)
                .append(text).append((char) 15).toString();
    }
}
