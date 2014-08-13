package org.toj.common.arena;

import java.awt.Point;

public class SinkingBoatMap extends ArenaMap {

    public SinkingBoatMap(int width, int height, String mapText) {
        super(width, height, mapText);
    }

    protected boolean validPosition(Point p, String ch) {
        if (!super.validPosition(p, ch)) {
            return false;
        }
        for (int i = (p.x - 1); i <= (p.x + 1); i++) {
            for (int j = p.y - 1; j <= p.y + 1; j++) {
                if ("¸¡".equals(map.get(pos(i, j))) || "´¬".equals(map.get(pos(i, j)))) {
                    return true;
                }
            }
        }
        
        return false;
    }
}
