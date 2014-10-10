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

@IrcCommand(command = "droploot", args = { CommandSegment.LIST })
public class DropLootCommand extends UndoableTopicCommand {

    private List<Item> items = new ArrayList<Item>();

    public DropLootCommand(Object[] args) {
        String lootString = composite(args);
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
            getGame().removeItem(i);
        }
        LogCommand logCommand = new LogCommand(new Object[] {"团队丢弃物品|" + getLootString()});
        logCommand.setCaller(caller);
        ToolkitEngine.getEngine().queueCommand(logCommand);
    }

    private String getLootString() {
        StringBuilder sb = new StringBuilder();
        for (Item i : items) {
            sb.append(i).append("; ");
        }
        return sb.substring(0, sb.length() - 2).toString();
    }
}
