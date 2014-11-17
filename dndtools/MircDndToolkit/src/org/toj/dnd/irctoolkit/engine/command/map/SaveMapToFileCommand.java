package org.toj.dnd.irctoolkit.engine.command.map;

import java.io.File;
import java.io.IOException;

import org.toj.dnd.irctoolkit.configs.GlobalConfigs;
import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.io.file.GameStore;

public class SaveMapToFileCommand extends MapCommand {

    private File file;

    public SaveMapToFileCommand(File file) {
        this.file = file;
    }

    @Override
    protected boolean mapChanged() {
        return false;
    }

    @Override
    protected boolean modelChanged() {
        return false;
    }

    @Override
    public void doExecute() throws ToolkitCommandException {
        if (file != null) {
            try {
                GameStore.save(context.getCurrentMap(), file);
                GlobalConfigs.getConfigs().set(GlobalConfigs.CONF_LAST_MAP,
                        file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                throw new ToolkitCommandException(e);
            }
        }
    }
}
