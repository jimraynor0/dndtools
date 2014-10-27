package org.toj.dnd.irctoolkit.clipboard;

public class ParsedColor {
    private int foreground;

    private int background;

    private int length;

    @Override
    public String toString() {
        return new StringBuilder("[").append(foreground).append(", ")
                .append(background).append(", ").append(length).append("]")
                .toString();
    }

    public int getForeground() {
        return foreground;
    }

    public void setForeground(int foreground) {
        this.foreground = foreground;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}