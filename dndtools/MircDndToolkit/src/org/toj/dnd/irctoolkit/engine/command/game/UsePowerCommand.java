package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.PowerDepleteException;

public class UsePowerCommand extends UndoableTopicCommand {

    private String[] args;

    public UsePowerCommand(String[] args) {
        this.args = args;
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
