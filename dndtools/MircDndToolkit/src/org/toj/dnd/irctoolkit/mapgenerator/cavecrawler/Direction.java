package org.toj.dnd.irctoolkit.mapgenerator.cavecrawler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;

enum Direction {
    NORTH(0, -1),
    NORTH_EAST(1, -1),
    EAST(1, 0),
    SOUTH_EAST(1, 1),
    SOUTH(0, 1),
    SOUTH_WEST(-1, 1),
    WEST(-1, 0),
    NORTH_WEST(-1, -1);

    static Direction randomNextDirection(Direction last) {
        List<Direction> directions = new ArrayList<Direction>(
                Arrays.asList(Direction.values()));
        List<Integer> weighedPosibilities = Arrays.asList(-1, 0, 0, 1);
        int index = directions.indexOf(last) + weighedPosibilities.get(RandomUtils
                .nextInt(weighedPosibilities.size()));
        if (index == -1) {
            index = 7;
        }
        if (index == 8) {
            index = 0;
        }
        return directions.get(index);
    }

    static Direction randomPerpendicularDirection(Direction last) {
        List<Direction> directions = new ArrayList<Direction>(
                Arrays.asList(Direction.values()));
        int index = directions.indexOf(last);
        if (RandomUtils.nextBoolean()) {
            index += 2;
            if (index >= 8) {
                index -= 8;
            }
        } else {
            index -= 2;
            if (index < 0) {
                index += 8;
            }
        }
        return directions.get(index);
    }

    private final int xInc;
    private final int yInc;

    Direction(int xInc, int yInc) {
        this.xInc = xInc;
        this.yInc = yInc;
    }

    VectorPoint move(VectorPoint o) {
        VectorPoint n = new VectorPoint();
        n.x = o.x + xInc;
        n.y = o.y + yInc;
        o.direction = this;
        return n;
    }
}
