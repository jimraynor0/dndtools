package org.toj.dnd.irctoolkit.engine.command.map;

import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.map.MapGrid;
import org.toj.dnd.irctoolkit.mapgenerator.DoorFirstDungeonGenerator;

public class GenerateRandomMapCommand extends MapCommand {

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
        MapGrid map = new DoorFirstDungeonGenerator().generateDungeon().asMapGrid();
        if (map != null) {
            context.setCurrentMap(map);
        }
    }
}
