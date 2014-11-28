package org.toj.dnd.irctoolkit.mapgenerator;

import org.toj.dnd.irctoolkit.configs.DefaultModels;
import org.toj.dnd.irctoolkit.map.MapGrid;

public abstract class DungeonGenerator {
    public static final String WALL_ID = "default.wall";
    public static final String DOOR_ID = "default.doorclosed";

    protected int width = 20;
    protected int height = 20;

    protected MapGrid createNewMapGrid() {
        MapGrid map = new MapGrid(width, height);
        map.getModelList().addAll(new DefaultModels().loadDefaultModels());
        return map;
    }

    public abstract MapGrid generateDungeon();

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
