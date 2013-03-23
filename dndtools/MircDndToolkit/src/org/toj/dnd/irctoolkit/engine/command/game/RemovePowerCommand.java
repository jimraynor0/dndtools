package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class RemovePowerCommand extends UndoableTopicCommand {

    private String[] args;

    public RemovePowerCommand(String[] args) {
        this.args = args;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (args.length == 0) {
            return;
        } else if (args.length == 1) {
            getGame().removePower(args[0], caller);
        } else {
            if (getGame().findCharByNameOrAbbre(args[0]) == null) {
                StringBuilder power = new StringBuilder();
                for (String seg : args) {
                    if (seg != args[0]) {
                        power.append(" ");
                    }
                    power.append(seg);
                }
                getGame().removePower(power.toString(), caller);
            } else {
                StringBuilder power = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    if (i > 1) {
                        power.append(" ");
                    }
                    power.append(args[i]);
                }
                getGame().removePower(power.toString(), args[0]);
            }
        }
    }
}
