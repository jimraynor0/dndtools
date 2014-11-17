package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.PC;
import org.toj.dnd.irctoolkit.game.Spell;

@IrcCommand(command = "listspell", args = { CommandSegment.NULLABLE_LIST })
public class ListSpellCommand extends UndoableTopicCommand {

    private List<String> owner;

    public ListSpellCommand(Object[] args) {
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
                for (String msg : buildSpellsString(pc.getName(),
                        pc.getSpells())) {
                    sendMsg(msg);
                }
            }
        }
    }

    protected List<String> buildSpellsString(String name,
            Map<String, List<Spell>> spells) {
        List<String> msg = new ArrayList<String>();
        if (spells.isEmpty()) {
            msg.add(name + "没有准备法术");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("-------- ").append(name).append(" --------")
                    .append("\r\n");
            msg.add(sb.toString());
            List<String> groups = new ArrayList<String>(spells.keySet());
            Collections.sort(groups);
            for (String group : groups) {
                sb = new StringBuilder(group).append(": ");
                for (Spell spell : spells.get(group)) {
                    sb.append(spell.toString()).append("; ");
                }
                sb.replace(sb.length() - 2, sb.length(), "");
                msg.add(sb.toString());
            }
        }
        return msg;
    }
}
