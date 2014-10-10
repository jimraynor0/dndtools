package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.LinkedList;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.Item;
import org.toj.dnd.irctoolkit.game.PC;

@IrcCommand(command = "takeloot", args = { CommandSegment.STRING, CommandSegment.LIST })
public class ObtainItemCommand extends UndoableTopicCommand {

    private String owner;
    private List<Item> items = new LinkedList<Item>();

    public ObtainItemCommand(Object[] args) {
        this.owner = (String) args[0];
        Object[] itemList = new Object[args.length - 1];
        System.arraycopy(args, 1, itemList, 0, itemList.length);
        String lootString = composite(itemList);
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
        if (owner == null) {
            sendMsg("很抱歉这个命令必须输入PC的名字哟。");
            return;
        }
        PC pc = getGame().findCharByNameOrAbbre(owner);
        if (pc != null) {
            String takeLootString = "";
            for (Item item : items) {
                String result = getGame().removeItem(item);
                if (result != null) {
                    sendMsg(result);
                    continue;
                }
                pc.addItem(item);
                if (!takeLootString.isEmpty()) {
                    takeLootString += "; ";
                }
                takeLootString += item;
            }

            LogCommand logCommand = new LogCommand(new Object[] {"分配团队物品|" + owner + "从团队物品中拿走了" + takeLootString});
            logCommand.setCaller(caller);
            ToolkitEngine.getEngine().queueCommand(logCommand);
        } else {
            sendMsg("很抱歉pc[" + owner + "]没有找到哟。");
        }
    }
}
