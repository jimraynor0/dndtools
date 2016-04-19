package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import java.util.ArrayList;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.engine.command.game.common.LogCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.dnd3r.Item;

@IrcCommand(command = "loot", args = { CommandSegment.LIST },
        summary = ".loot 物品*数量<|物品*数量> - 将[数量]个[物品]加入团队loot列表。物品和数量之间用星号(*)分隔，物品和物品之间用竖线(|)分隔。")
public class AddLootCommand extends Dnd3rGameCommand {

    private List<Item> items = new ArrayList<Item>();

    public AddLootCommand(Object[] args) {
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
            getGame().addItem(i);
        }
        LogCommand logCommand = new LogCommand(new Object[] { "团队获得物品|" + getLootString() });
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
