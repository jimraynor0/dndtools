package org.toj.dnd.irctoolkit.mapgenerator.cavecrawler;

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

public class CrawlerCaveGenerator extends DungeonGenerator {
    private int walkerCount = 0;

    private List<VectorPoint> emptyTiles;

    private int branchNumber;
    private int branchLength;

    @Override
    public MapGrid generateDungeon() {
        MapGenConfigs config = new MapGenConfigs();
        branchNumber = config.getInt(MapGenConfigs.CRW_MAP_BRANCH_NUMBER);
        branchLength = config.getInt(MapGenConfigs.CRW_MAP_BRANCH_LENGTH);
        initMap();
        while (walkerCount < branchNumber) {
            Walker walker = createWalker();
            walker.walkOn();
            walkerCount++;
        }

        return createMap();
    }

    private void initMap() {
        emptyTiles = new ArrayList<VectorPoint>(branchNumber * branchLength);
        emptyTiles.add(new VectorPoint(0, 0, Direction.EAST));
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
        for (VectorPoint p : emptyTiles) {
            map.eraseObjects(Arrays.asList(p.x), Arrays.asList(p.y),
                    Arrays.asList(wallModel));
        }
        return map;
    }

    public static void main(String[] args) {
        new CrawlerCaveGenerator().generateDungeon();
    }
}
