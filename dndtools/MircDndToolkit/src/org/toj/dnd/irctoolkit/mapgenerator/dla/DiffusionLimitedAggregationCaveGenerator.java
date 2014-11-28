package org.toj.dnd.irctoolkit.mapgenerator.dla;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.toj.dnd.irctoolkit.configs.MapGenConfigs;
import org.toj.dnd.irctoolkit.map.MapGrid;
import org.toj.dnd.irctoolkit.map.MapModel;
import org.toj.dnd.irctoolkit.mapgenerator.DungeonGenerator;

public class DiffusionLimitedAggregationCaveGenerator extends DungeonGenerator {
    private int walkerCount;

    private List<Point> emptyTiles = new ArrayList<Point>(walkerCount + 1);

    @Override
    public MapGrid generateDungeon() {
        MapGenConfigs config = new MapGenConfigs();
        width = config.getInt(MapGenConfigs.DLA_MAP_WIDTH);
        height = config.getInt(MapGenConfigs.DLA_MAP_HEIGHT);
        walkerCount = config.getInt(MapGenConfigs.DLA_EMPTY_TILE_TOTAL);
        initMap();
        while (walkerCount > 0) {
            Walker walker = createWalker();
            walker.walkOn();
            emptyTiles.add(walker.getEndPoint());
            walkerCount--;
        }

        return createMap();
    }

    private MapGrid createMap() {
        MapGrid map = createNewMapGrid();
        int[] posXs = new int[width];
        for (int i = 0; i < posXs.length; i++) {
            posXs[i] = i;
        }
        int[] posYs = new int[height];
        for (int i = 0; i < posYs.length; i++) {
            posYs[i] = i;
        }
        MapModel wallModel = map.getModelList().findModelById(
                DungeonGenerator.WALL_ID);
        map.drawObjectsOntoGrid(posXs, posYs, wallModel);
        for (Point p : emptyTiles) {
            map.eraseObjects(Arrays.asList(p.x), Arrays.asList(p.y),
                    Arrays.asList(wallModel));
        }
        return map;
    }

    private void initMap() {
        Set<Point> initialPoints = new HashSet<Point>(width * 2 + height * 2);
        for (int i = 0; i < width; i++) {
            initialPoints.add(new Point(i, 0));
            initialPoints.add(new Point(i, height - 1));
        }
        for (int i = 0; i < height; i++) {
            initialPoints.add(new Point(0, i));
            initialPoints.add(new Point(width - 1, i));
        }
        List<Point> initialPointsList = new ArrayList<Point>(initialPoints);
        emptyTiles.add(initialPointsList.get(random(initialPointsList.size())));
    }

    private Walker createWalker() {
        Point spawningPoint = null;
        do {
            spawningPoint = new Point(random(width), random(height));
        } while (emptyTiles.contains(spawningPoint));

        return new Walker(spawningPoint);
    }

    private int random(int maxNotInclusive) {
        return (int) (Math.random() * maxNotInclusive);
    }

    private class Walker {
        private Point pos;

        public Walker(Point spawningPoint) {
            pos = spawningPoint;
        }

        public void walkOn() {
            while (true) {
                Point nextStep = findNextStep();
                if (emptyTiles.contains(nextStep)) {
                    return;
                }
                pos = nextStep;
            }
        }

        private Point findNextStep() {
            List<Point> availableSteps = new ArrayList<Point>(4);
            if (pos.x > 0) {
                availableSteps.add(new Point(pos.x - 1, pos.y));
            }
            if (pos.x < width - 1) {
                availableSteps.add(new Point(pos.x + 1, pos.y));
            }
            if (pos.y > 0) {
                availableSteps.add(new Point(pos.x, pos.y - 1));
            }
            if (pos.y < height - 1) {
                availableSteps.add(new Point(pos.x, pos.y + 1));
            }
            return availableSteps.get(random(availableSteps.size()));
        }

        public Point getEndPoint() {
            return pos;
        }
    }

    public static void main(String[] args) {
        new DiffusionLimitedAggregationCaveGenerator().generateDungeon();
    }
}
