package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "listloot", args = {})
public class ListLootCommand extends ListItemCommand {

    public ListLootCommand(Object[] args) {
        super(args);
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        sendMsg(buildItemsString("ме╤с", getGame().getItems()));
    }
}
