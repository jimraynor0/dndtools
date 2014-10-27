package org.toj.dnd.irctoolkit.mapgenerator;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Door {
    private List<Point> squares = new ArrayList<Point>();

    public List<Point> getSquares() {
        return squares;
    }

    public void setSquares(List<Point> doorSquares) {
        this.squares = doorSquares;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Door = [");
        for (Point p : squares) {
            if (p != squares.get(0)) {
                sb.append(" - ");
            }
            sb.append(p.x).append(",").append(p.y);
        }
        return sb.append("]").toString();
    }

    public int getWidth() {
        return squares.size();
    }

    public Point getStart() {
        return squares.get(0);
    }

    public Point getEnd() {
        return squares.get(squares.size() - 1);
    }
}
