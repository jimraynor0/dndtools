package org.toj.dnd.irctoolkit.filter;

import java.io.Serializable;

import org.toj.dnd.irctoolkit.map.Point;

public class ViewPoint extends Point implements Serializable {
    private static final long serialVersionUID = -2675267555217600980L;

    private String obj;
    private int range = 0;

    public ViewPoint(String obj) {
        super();
        this.obj = obj;
    }

    public ViewPoint(String obj, int range) {
        super();
        this.obj = obj;
        this.range = range;
    }

    public ViewPoint() {
        super();
    }

    public ViewPoint(int x, int y) {
        super(x, y);
    }

    public ViewPoint(int x, int y, int range) {
        super(x, y);
        this.range = range;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public double getOneAndHalfSquareSlope() {
        return range / (range + 1.5);
    }
}
