package org.toj.common.arena;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import org.toj.dnd.irctoolkit.dice.Dice;

public class ArenaMap {
    protected int width;
    protected int height;
    protected Map<Point, String> map = new HashMap<Point, String>();

    public ArenaMap(int width, int height, String mapText) {
        this.width = width;
        this.height = height;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map.put(pos(i, j), "" + mapText.charAt(i + j * width));
            }
        }
    }

    public void placeRandomly(String ch) {
        int x = Dice.getDice(width - 1).roll();
        int y = Dice.getDice(height - 1).roll();

        if (validPosition(pos(x, y), map.get(pos(x, y)))) {
            map.put(pos(x, y), ch);
        } else {
            placeRandomly(ch);
        }
    }

    protected boolean validPosition(Point p, String ch) {
        if ("Ç½".equals(ch) || "ÈË".equals(ch) || "¹Ö".equals(ch) || "·ì".equals(ch)) {
            return false;
        }
        
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sb.append(map.get(pos(j, i)));
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    protected Point pos(int x, int y) {
        return new Point(x, y);
    }
}
