package org.toj.dnd.irctoolkit.mapgenerator.dla;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.toj.dnd.irctoolkit.map.MapGrid;
import org.toj.dnd.irctoolkit.mapgenerator.DungeonGenerator;

public class DiffusionLimitedAggregationCaveGenerator implements
        DungeonGenerator {

    private int width = 20;
    private int height = 20;
    private int walkerCount = width * height / 2;

    private List<Point> emptyTiles = new ArrayList<Point>(walkerCount + 1);

    @Override
    public MapGrid generateDungeon() {
        initMap();
        while (walkerCount > 0) {
            Walker walker = new Walker();
            walker.walkOn();
            emptyTiles.add(walker.getEndPoint());
        }
        return null;
    }

    private void initMap() {

    }

    private class Walker {
    
        public void walkOn() {
            // TODO Auto-generated method stub
            
        }
    
        public Point getEndPoint() {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
