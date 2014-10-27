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

@IrcCommand(command = "gift", args = { CommandSegment.STRING,
        CommandSegment.STRING, CommandSegment.LIST })
public class GiftItemCommand extends UndoableTopicCommand {

    private String owner;
    private String target;
    private List<Item> items = new LinkedList<Item>();

    public GiftItemCommand(Object[] args) {
        this.owner = (String) args[0];
        this.target = (String) args[1];
        Object[] itemList = new Object[args.length - 2];
        System.arraycopy(args, 2, itemList, 0, itemList.length);
        String lootString = composite(itemList);
        for (String itemStr : lootString.split("\\|")) {
            Item item = null;
            if (itemStr.contains("*")) {
                item = new Item(itemStr.split("\\*")[0],
                        Integer.parseInt(itemStr.split("\\*")[1]));
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
        if (owner == null || target == null) {
            sendMsg("很抱歉这个命令必须输入赠与者和接受者的名字哟。");
            return;
        }
        PC pc = getGame().findCharByNameOrAbbre(owner);
        if (pc == null) {
            sendMsg("很抱歉pc[" + owner + "]没有找到哟。");
        }
        if ("loot".equalsIgnoreCase(target)) {
            String takeLootString = "";
            for (Item item : items) {
                String result = pc.removeItem(item);
                if (result != null) {
                    sendMsg(result);
                    continue;
                }
                getGame().addItem(item);
                if (!takeLootString.isEmpty()) {
                    takeLootString += "; ";
                }
                takeLootString += item;
            }

            LogCommand logCommand = new LogCommand(new Object[] { "交还团队物品|"
                    + owner + "把" + takeLootString + "交给了团队" });
            logCommand.setCaller(caller);
            ToolkitEngine.getEngine().queueCommand(logCommand);
        } else {
            PC recv = getGame().findCharByNameOrAbbre(target);
            if (recv == null) {
                sendMsg("很抱歉pc[" + target + "]没有找到哟。");
            }
            if (pc != null) {
                String takeLootString = "";
                for (Item item : items) {
                    String result = pc.removeItem(item);
                    if (result != null) {
                        sendMsg(result);
                        continue;
                    }
                    recv.addItem(item);
                    if (!takeLootString.isEmpty()) {
                        takeLootString += "; ";
                    }
                    takeLootString += item;
                }

                LogCommand logCommand = new LogCommand(new Object[] { "物品转交|"
                        + owner + "把" + takeLootString + "交给了" + target });
                logCommand.setCaller(caller);
                ToolkitEngine.getEngine().queueCommand(logCommand);
            }
        }
    }
}
