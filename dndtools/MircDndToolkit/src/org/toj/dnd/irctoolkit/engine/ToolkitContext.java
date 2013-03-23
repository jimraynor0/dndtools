package org.toj.dnd.irctoolkit.engine;

import java.util.List;

import org.toj.dnd.irctoolkit.filter.MapFilter;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.history.HistoryManager;
import org.toj.dnd.irctoolkit.map.MapGrid;
import org.toj.dnd.irctoolkit.map.MapModelList;

public class ToolkitContext implements ReadonlyContext {

    private static final ToolkitContext INSTANCE = new ToolkitContext();

    public static ToolkitContext getContext() {
        return INSTANCE;
    }

    private Game game;
    private MapGrid currentMap;
    private HistoryManager history = new HistoryManager();

    private ToolkitContext() {
        super();
        this.currentMap = new MapGrid(20, 20);
    }

    @Override
    public MapGrid getCurrentMap() {
        return currentMap;
    }

    @Override
    public MapModelList getModelList() {
        return currentMap.getModelList();
    }

    @Override
    public List<MapFilter> getFilterList() {
        return currentMap.getFilterList();
    }

    public void setCurrentMap(MapGrid mapGrid) {
        this.currentMap = mapGrid;
    }

    public HistoryManager getHistory() {
        return history;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
