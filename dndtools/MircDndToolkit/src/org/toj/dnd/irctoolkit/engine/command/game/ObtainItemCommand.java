package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.Item;
import org.toj.dnd.irctoolkit.game.PC;

@IrcCommand(command = "obtain", args = { CommandSegment.NULLABLE_INT, CommandSegment.NULLABLE_STRING, CommandSegment.STRING })
public class ObtainItemCommand extends UndoableTopicCommand {

    private String owner;
    private String item = null;
    private int amount;

    public ObtainItemCommand(Object[] args) {
        this.amount = args[0] == null ? 1 : (Integer) args[0];
        this.owner = (String) args[1];
        this.item = (String) args[2];
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (owner == null) {
            owner = caller;
        }
        PC pc = getGame().findCharByNameOrAbbre(owner);
        if (pc != null) {
            Item current = pc.getItems().get(item);
            if (current != null) {
                current.addCharge(amount);
            } else {
                pc.getItems().put(item, new Item(item, amount));
            }
        }
    }
}
