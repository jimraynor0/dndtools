package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "prepare", args = { CommandSegment.LIST } )
public class PrepareSpellCommand extends UndoableTopicCommand {

    private String[] args;

    public PrepareSpellCommand(Object[] args) {
        this.args = new String[args.length];
        System.arraycopy(args, 0, this.args, 0, args.length);
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (args.length == 0) {
            return;
        } else if (args.length == 1) {
            getGame().addPower(args[0], caller);
        } else {
            if (getGame().findCharByNameOrAbbre(args[0]) == null) {
                StringBuilder power = new StringBuilder();
                for (String seg : args) {
                    if (seg != args[0]) {
                        power.append(" ");
                    }
                    power.append(seg);
                }
                getGame().addPower(power.toString(), caller);
            } else {
                StringBuilder power = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    if (i > 1) {
                        power.append(" ");
                    }
                    power.append(args[i]);
                }
                getGame().addPower(power.toString(), args[0]);
            }
        }
    }
}
