package org.toj.dnd.irctoolkit.mapgenerator.ca;

import org.toj.dnd.irctoolkit.configs.MapGenConfigs;
import org.toj.dnd.irctoolkit.map.MapGrid;
import org.toj.dnd.irctoolkit.map.MapModel;
import org.toj.dnd.irctoolkit.map.MapObject;
import org.toj.dnd.irctoolkit.mapgenerator.DungeonGenerator;

public class CelullarAutomationCaveGenerator extends DungeonGenerator {

    private double chanceToStartAlive = 0.4d;

    private int birthLimit = 3;
    private int deathLimit = 4;
    private int iteration = 2;

    private boolean[][] dungeon;

    @Override
    public MapGrid generateDungeon() {
        MapGenConfigs config = new MapGenConfigs();
        width = config.getInt(MapGenConfigs.CA_MAP_WIDTH);
        height = config.getInt(MapGenConfigs.CA_MAP_HEIGHT);
        dungeon = new boolean[width][height];
        doGenerateDungeon();
        return paint();
    }

    private void doGenerateDungeon() {
        initialiseMap();
        for (int i = 0; i < iteration; i++) {
            doSimulationStep();
        }
    }

    public void doSimulationStep() {
        boolean[][] newMap = new boolean[width][height];
        // Loop over each row and column of the map
        for (int x = 0; x < dungeon.length; x++) {
            for (int y = 0; y < dungeon[0].length; y++) {
                int nbs = countAliveNeighbours(dungeon, x, y);
                // The new value is based on our simulation rules
                // First, if a cell is alive but has too few neighbours, kill
                // it.
                if (dungeon[x][y]) {
                    if (nbs < deathLimit) {
                        newMap[x][y] = false;
                    } else {
                        newMap[x][y] = true;
                    }
                } // Otherwise, if the cell is dead now, check if it has the
                  // right number of neighbours to be 'born'
                else {
                    if (nbs > birthLimit) {
                        newMap[x][y] = true;
                    } else {
                        newMap[x][y] = false;
                    }
                }
            }
        }
        dungeon = newMap;
    }

    public int countAliveNeighbours(boolean[][] map, int x, int y) {
        int count = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int neighbourX = x + i;
                int neighboury = y + j;
                // If we're looking at the middle point
                if (i == 0 && j == 0) {
                    // Do nothing, we don't want to add ourselves in!
                } else if (neighbourX < 0 || neighboury < 0
                        || neighbourX >= map.length
                        || neighboury >= map[0].length) {
                    // In case the index we're looking at it off the edge of the
                    // map
                    count = count + 1;
                } else if (map[neighbourX][neighboury]) {
                    // Otherwise, a normal check of the neighbour
                    count = count + 1;
                }
            }
        }
        return count;
    }

    public void initialiseMap() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (Math.random() < chanceToStartAlive) {
                    dungeon[x][y] = true;
                }
            }
        }
    }

    private MapGrid paint() {
        MapGrid map = createNewMapGrid();
        MapModel wallModel = map.getModelList().findModelById(
                DungeonGenerator.WALL_ID);
        for (int x = 0; x < dungeon.length; x++) {
            for (int y = 0; y < dungeon[0].length; y++) {
                if (dungeon[x][y]) {
                    map.drawObject(new MapObject(x, y, wallModel));
                }
            }
        }
        return map;
    }

    public static void main(String[] args) {
        CelullarAutomationCaveGenerator cave = new CelullarAutomationCaveGenerator();
        cave.doGenerateDungeon();
        for (int x = 0; x < cave.dungeon.length; x++) {
            for (int y = 0; y < cave.dungeon[0].length; y++) {
                System.out.print(cave.dungeon[x][y] ? "墙" : "　");
            }
            System.out.println();
        }
    }
}
