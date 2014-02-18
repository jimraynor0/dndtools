package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command="regainpower", args = {CommandSegment.LIST})
public class RegainPowerCommand extends UndoableTopicCommand {

    private String[] args;

    public RegainPowerCommand(Object[] args) {
        this.args = new String[args.length];
        System.arraycopy(args, 0, this.args, 0, args.length);
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (getGame().findCharByNameOrAbbre(args[0]) == null) {
            StringBuilder power = new StringBuilder();
            for (String seg : args) {
                if (seg != args[0]) {
                    power.append(" ");
                }
                power.append(seg);
            }
            this.getGame().findCharByNameOrAbbre(caller)
                    .regainPower(power.toString());
        } else {
            StringBuilder power = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                if (i > 1) {
                    power.append(" ");
                }
                power.append(args[i]);
            }
            this.getGame().findCharByNameOrAbbre(args[0])
                    .regainPower(power.toString());
        }
    }
}
