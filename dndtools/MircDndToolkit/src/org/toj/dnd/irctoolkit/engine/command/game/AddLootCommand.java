package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.ArrayList;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.Item;
import org.toj.dnd.irctoolkit.game.PC;

@IrcCommand(command = "loot", args = { CommandSegment.LIST })
public class AddLootCommand extends UndoableTopicCommand {

    private List<Item> items = new ArrayList<Item>();
    private String lootString;

    public AddLootCommand(Object[] args) {
        lootString = composite(args);
        for (String itemStr : lootString.split("\\|")) {
            Item item = null;
            if (itemStr.contains("*")) {
                item = new Item(itemStr.split("\\*")[0], Integer.parseInt(itemStr.split("\\*")[1]));
            } else {
                item = new Item(itemStr, 1);
            }
            if (item != null) {
                items.add(item);
            }
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        for (Item i : items) {
            getGame().getItems().put(i.getName(), i);
        }
        LogCommand logCommand = new LogCommand(new Object[] {"团队获得物品", lootString});
        logCommand.setCaller(caller);
        ToolkitEngine.getEngine().queueCommand(logCommand);
    }
}
