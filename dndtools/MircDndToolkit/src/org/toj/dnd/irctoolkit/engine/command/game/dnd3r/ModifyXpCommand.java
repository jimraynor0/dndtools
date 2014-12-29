package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import org.toj.dnd.irctoolkit.engine.ToolkitEngine;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.engine.command.game.common.LogCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.dnd3r.PC;

@IrcCommand(command = "xp", args = { CommandSegment.INT,
    CommandSegment.NULLABLE_LIST })
public class ModifyXpCommand extends Dnd3rGameCommand {

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
            sb.append("发放xp|");
        } else {
            sb.append("减少xp|");
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
            sb.append("所有pc");
        }
        if (value > 0) {
            sb.append("获得");
        } else {
            sb.append("减少");
        }
        sb.append(Math.abs(value)).append("点xp");

        LogCommand logCommand = new LogCommand(new Object[] { sb.toString() });
        logCommand.setCaller(caller);
        ToolkitEngine.getEngine().queueCommand(logCommand);
    }
}
