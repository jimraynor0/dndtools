package org.toj.dnd.irctoolkit.engine.command.ui;

import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.map.MapGrid;
import org.toj.dnd.irctoolkit.mapgenerator.DungeonGenerator;

public class GenerateRandomMapCommand extends MapCommand {
    private DungeonGenerator dungeonGenerator;

    public GenerateRandomMapCommand(DungeonGenerator dungeonGenerator) {
        this.dungeonGenerator = dungeonGenerator;
    }

    @Override
    protected boolean mapChanged() {
        return true;
    }

    @Override
    protected boolean modelChanged() {
        return true;
    }

    @Override
    protected boolean filterChanged() {
        return true;
    }

    @Override
    public void doExecute() throws ToolkitCommandException {
        MapGrid map = dungeonGenerator.generateDungeon();
        if (map != null) {
            context.setCurrentMap(map);
        }
    }
}
