package org.toj.dnd.irctoolkit.engine.command.map;

import org.toj.dnd.irctoolkit.engine.command.MapCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.filter.MapFilter;

public class AddOrUpdateFilterCommand extends MapCommand {

    private MapFilter filter;
    private int index;

    public AddOrUpdateFilterCommand(MapFilter filter, Class filterClass) {
        this.filter = filter;
        for (MapFilter f : context.getFilterList()) {
            if (filterClass.isInstance(f)) {
                this.index = context.getFilterList().indexOf(f);
            }
        }
    }

    public AddOrUpdateFilterCommand(MapFilter filter, int index) {
        this.filter = filter;
        this.index = index;
    }

    @Override
    protected void doExecute() throws ToolkitCommandException {
        if (index == -1) {
            context.getFilterList().add(filter);
        } else {
            context.getFilterList().set(index, filter);
        }
    }

    @Override
    protected boolean filterChanged() {
        return true;
    }
}
