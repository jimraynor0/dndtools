package org.toj.dnd.irctoolkit.token;

public class Model {
    private Color foreground;
    private Color background;

    public Model() {
        super();
    }

    public Model(Color foreground, Color background, String ch) {
        super();
        this.foreground = foreground;
        this.background = background;
        this.ch = ch;
    }

    private String ch;

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }
}
