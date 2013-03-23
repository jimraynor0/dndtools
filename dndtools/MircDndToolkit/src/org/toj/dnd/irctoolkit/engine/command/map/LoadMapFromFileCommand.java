package org.toj.dnd.irctoolkit.engine.command.map;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.toj.dnd.irctoolkit.configs.GlobalConfigs;
import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.file.GameStore;
import org.toj.dnd.irctoolkit.map.MapGrid;

public class LoadMapFromFileCommand extends MapCommand {

    private File file;

    public LoadMapFromFileCommand(File file) {
        this.file = file;
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
        if (file != null && file.isFile() && file.exists()) {
            try {
                MapGrid map = GameStore.loadMap(file);
                context.setCurrentMap(map);
                GlobalConfigs.getConfigs().set(GlobalConfigs.CONF_LAST_MAP,
                        file.getAbsolutePath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new ToolkitCommandException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ToolkitCommandException(e);
            }
        }
    }
}
