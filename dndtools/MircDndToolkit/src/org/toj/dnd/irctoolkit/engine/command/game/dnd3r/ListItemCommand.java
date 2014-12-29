package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.dnd3r.Item;
import org.toj.dnd.irctoolkit.game.dnd3r.PC;

@IrcCommand(command = "listitem", args = { CommandSegment.NULLABLE_LIST })
public class ListItemCommand extends Dnd3rGameCommand {

    private List<String> owner;

    public ListItemCommand(Object[] args) {
        owner = new LinkedList<String>();
        for (Object o : args) {
            owner.add((String) o);
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (owner.isEmpty()) {
            owner.add(caller);
        }
        if ("ALL".equals(owner.get(0))) {
            owner.clear();
            owner.addAll(getGame().getPcs().keySet());
        }

        for (String name : owner) {
            PC pc = getGame().findCharByNameOrAbbre(name);
            if (pc != null) {
                sendMsg(buildItemsString(pc.getName(), pc.getItems()));
            }
        }
    }

    protected String buildItemsString(String name, Map<String, Item> items) {
        StringBuilder sb = new StringBuilder(name);
        if (items.isEmpty()) {
            sb.append("没有可用的物品。");
        } else {
            sb.append(": ");
            boolean first = true;
            for (String i : items.keySet()) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(items.get(i).getName()).append("(")
                    .append(items.get(i).getCharges()).append(")");
                first = false;
            }
        }
        return sb.toString();
    }
}
