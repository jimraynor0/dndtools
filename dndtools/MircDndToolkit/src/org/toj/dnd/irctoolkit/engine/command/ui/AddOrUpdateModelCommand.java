package org.toj.dnd.irctoolkit.engine.command.ui;

import java.util.Arrays;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.map.MapModel;

public class AddOrUpdateModelCommand extends MapCommand {

    private List<MapModel> models;
    private int index = -1;

    public AddOrUpdateModelCommand(List<MapModel> models) {
        this.models = models;
    }

    public AddOrUpdateModelCommand(MapModel model, int index) {
        this.models = Arrays.asList(model);
        this.index = index;
    }

    @Override
    protected void doExecute() throws ToolkitCommandException {
        if (index == -1) {
            for (MapModel model : models) {
                // skip existing ones
                boolean exists = false;
                for (MapModel existing : context.getModelList()) {
                    if (existing.getId().equals(model.getId())) {
                        exists = true;
                    }
                }
                if (!exists) {
                    context.getModelList().add(model);
                }
            }
        } else {
            context.getModelList().set(index, models.get(0));
        }
    }

    @Override
    protected boolean modelChanged() {
        return true;
    }
}
