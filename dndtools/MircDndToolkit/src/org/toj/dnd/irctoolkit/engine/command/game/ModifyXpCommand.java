package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.PC;

@IrcCommand(command = "xp", args = { CommandSegment.INT,
        CommandSegment.NULLABLE_LIST })
public class ModifyXpCommand extends UndoableTopicCommand {

    private String[] pcs;
    private int value;

    public ModifyXpCommand(Object[] args) {
        value = (Integer) args[0];
        if (args.length == 1) {
            pcs = new String[0];
        } else {
            pcs = new String[args.length - 1];
            System.arraycopy(args, 1, pcs, 0, pcs.length);
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        StringBuilder sb = new StringBuilder();
        if (value > 0) {
            sb.append("����xp|");
        } else {
            sb.append("����xp|");
        }
        if (pcs != null && pcs.length > 0) {
            boolean first = true;
            for (String pcName : pcs) {
                PC pc = getGame().findCharByNameOrAbbre(pcName);
                if (pc != null) {
                    pc.setXp(pc.getXp() + value);
                    if (!first) {
                        sb.append(", ");
                    } else {
                        first = false;
                    }
                    sb.append(pc.getName());
                }
            }
        } else {
            for (PC pc : getGame().getPcs().values()) {
                pc.setXp(pc.getXp() + value);
            }
            sb.append("����pc");
        }
        if (value > 0) {
            sb.append("���");
        } else {
            sb.append("����");
        }
        sb.append((int) Math.abs(value)).append("��xp");

        LogCommand logCommand = new LogCommand(new Object[] { sb.toString() });
        logCommand.setCaller(caller);
        ToolkitEngine.getEngine().queueCommand(logCommand);
    }
}
