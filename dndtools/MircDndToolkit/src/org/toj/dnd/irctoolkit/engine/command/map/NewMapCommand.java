package org.toj.dnd.irctoolkit.engine.command.map;

import org.toj.dnd.irctoolkit.configs.DefaultModels;
import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.map.MapGrid;

public class NewMapCommand extends MapCommand {
    // private Logger log = Logger.getLogger(this.getClass());

    private int width;
    private int height;

    public NewMapCommand(int width, int height) {
        this.width = width;
        this.height = height;
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
    public void doExecute() throws ToolkitCommandException {
        context.setCurrentMap(new MapGrid(width, height));
        ToolkitEngine.getEngine().queueCommand(
                new AddOrUpdateModelCommand(new DefaultModels()
                        .loadDefaultModels()));
    }
}
