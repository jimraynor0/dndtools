package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.dnd3r.Item;
import org.toj.dnd.irctoolkit.game.dnd3r.PC;

@IrcCommand(command = "newitem", args = { CommandSegment.NULLABLE_INT,
    CommandSegment.NULLABLE_STRING, CommandSegment.STRING,
    CommandSegment.STRING })
public class NewItemCommand extends Dnd3rGameCommand {

    private String owner;
    private String item = null;
    private String desc = null;
    private int amount;

    public NewItemCommand(Object[] args) {
        this.amount = args[0] == null ? 1 : (Integer) args[0];
        this.owner = (String) args[1];
        this.item = (String) args[2];
        this.desc = (String) args[3];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (owner == null) {
            owner = caller;
        }
        PC pc = getGame().findCharByNameOrAbbre(owner);
        if (pc != null) {
            pc.getItems().put(item, new Item(item, desc, amount));
        }
    }
}
