package org.toj.dnd.irctoolkit.mapgenerator;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import org.toj.dnd.irctoolkit.configs.DefaultModels;
import org.toj.dnd.irctoolkit.map.MapGrid;
import org.toj.dnd.irctoolkit.util.RandomUtil;

public class Dungeon {

    // public static final String WALL = "¡ö";
    // public static final String DOOR = "¡î";
    // public static final String SPACE = "¡¡";

    private static final String WALL_ID = "default.wall";
    private static final String DOOR_ID = "default.doorclosed";

    private int width;
    private int height;
    private LinkedList<Room> rooms;
    private LinkedList<Door> doors;
    private LinkedList<Corridor> corridors;

    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.doors = new LinkedList<Door>();
        this.rooms = new LinkedList<Room>();
        this.corridors = new LinkedList<Corridor>();
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public LinkedList<Room> getRooms() {
        return rooms;
    }

    public LinkedList<Corridor> getCorridors() {
        return corridors;
    }

    public boolean canHave(Room room) {
        if (room.getPosX() <= 0 || room.getPosY() <= 0
                || room.getPosX() + room.getWidth() >= this.width - 1
                || room.getPosY() + room.getHeight() >= this.height - 1) {
            return false;
        }
        for (Room i : rooms) {
            for (Point p : i.getCorners()) {
                if (room.wallContains(p)) {
                    return false;
                }
            }
            for (Point p : room.getCorners()) {
                if (i.wallContains(p)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Room pickRandomRoom() {
        return this.rooms.get(RandomUtil.random(0, rooms.size()));
    }

    public MapGrid asMapGrid() {
        MapGrid map = new MapGrid(width, height);
        map.getModelList().addAll(new DefaultModels().loadDefaultModels());
        for (Room room : rooms) {
            paint(room, map);
        }
        for (Door door : doors) {
            paint(door, map);
        }
        return map;
    }

    //
    // public String toString() {
    // String[][] grid = new String[width][height];
    // for (Room room : rooms) {
    // paint(room, grid);
    // }
    // for (Point door : doors) {
    // paint(door, grid);
    // }
    //
    // StringBuilder sb = new StringBuilder();
    // for (int x = 0; x < width; x++) {
    // for (int y = 0; y < height; y++) {
    // sb.append(grid[x][y] == null ? SPACE : grid[x][y]);
    // }
    // sb.append("\r\n");
    // }
    // return sb.toString();
    // }

    private void paint(Door door, MapGrid map) {
        for (Point p : door.getSquares()) {
            map.drawObjectsOntoGrid(new int[] { p.x }, new int[] { p.y }, map
                    .getModelList().findModelById(DOOR_ID));
        }
    }

    private void paint(Room room, MapGrid map) {
        List<Point> corners = room.getWallCorners();
        Point topLeft = corners.get(0);
        Point topRight = corners.get(1);
        Point bottomLeft = corners.get(2);
        Point bottomRight = corners.get(3);
        for (int i = topLeft.x; i <= topRight.x; i++) {
            map.drawObjectsOntoGrid(new int[] { i }, new int[] { topLeft.y },
                    map.getModelList().findModelById(WALL_ID));
        }
        for (int i = bottomLeft.x; i <= bottomRight.x; i++) {
            map.drawObjectsOntoGrid(new int[] { i },
                    new int[] { bottomLeft.y }, map.getModelList()
                            .findModelById(WALL_ID));
        }
        for (int i = topLeft.y; i <= bottomLeft.y; i++) {
            map.drawObjectsOntoGrid(new int[] { topLeft.x }, new int[] { i },
                    map.getModelList().findModelById(WALL_ID));
        }
        for (int i = topRight.y; i <= bottomRight.y; i++) {
            map.drawObjectsOntoGrid(new int[] { topRight.x }, new int[] { i },
                    map.getModelList().findModelById(WALL_ID));
        }
    }

    public LinkedList<Door> getDoors() {
        return doors;
    }
}
