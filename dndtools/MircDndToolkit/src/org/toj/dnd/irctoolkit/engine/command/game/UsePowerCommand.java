package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand.CommandSegment;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.PowerDepleteException;

@IrcCommand(command="power", args = {CommandSegment.LIST})
public class UsePowerCommand extends UndoableTopicCommand {

    private String[] args;

    public UsePowerCommand(Object[] args) {
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
            try {
                this.getGame().findCharByNameOrAbbre(caller)
                        .usePower(power.toString());
            } catch (PowerDepleteException e) {
                super.sendMsg("哥们，这个power已经用完了呀");
            }
        } else {
            StringBuilder power = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                if (i > 1) {
                    power.append(" ");
                }
                power.append(args[i]);
            }
            try {
                this.getGame().findCharByNameOrAbbre(args[0])
                        .usePower(power.toString());
            } catch (PowerDepleteException e) {
                super.sendMsg("哥们，这个power已经用完了呀");
            }
        }
    }
}
