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
            sendMsg("�ܱ�Ǹ�������������������ߺͽ����ߵ�����Ӵ��");
            return;
        }
        PC pc = getGame().findCharByNameOrAbbre(owner);
        if (pc == null) {
            sendMsg("�ܱ�Ǹpc[" + owner + "]û���ҵ�Ӵ��");
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

            LogCommand logCommand = new LogCommand(new Object[] { "�����Ŷ���Ʒ|"
                    + owner + "��" + takeLootString + "�������Ŷ�" });
            logCommand.setCaller(caller);
            ToolkitEngine.getEngine().queueCommand(logCommand);
        } else {
            PC recv = getGame().findCharByNameOrAbbre(target);
            if (recv == null) {
                sendMsg("�ܱ�Ǹpc[" + target + "]û���ҵ�Ӵ��");
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

                LogCommand logCommand = new LogCommand(new Object[] { "��Ʒת��|"
                        + owner + "��" + takeLootString + "������" + target });
                logCommand.setCaller(caller);
                ToolkitEngine.getEngine().queueCommand(logCommand);
            }
        }
    }
}
