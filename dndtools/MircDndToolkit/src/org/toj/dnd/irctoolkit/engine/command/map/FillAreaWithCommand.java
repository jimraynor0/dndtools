package org.toj.dnd.irctoolkit.engine.command.map;

import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.map.MapModel;

public class FillAreaWithCommand extends MapCommand {

    private int[] posXs;
    private int[] posYs;
    private MapModel model;

    public FillAreaWithCommand(int[] posXs, int[] posYs, MapModel model) {
        this.posXs = posXs;
        this.posYs = posYs;
        this.model = model;
    }

    @Override
    protected boolean mapChanged() {
        return true;
    }

    @Override
    protected boolean modelChanged() {
        return false;
    }

    @Override
    public void doExecute() throws ToolkitCommandException {
        context.getCurrentMap().drawObjectsOntoGrid(posXs, posYs, model);
    }
}
