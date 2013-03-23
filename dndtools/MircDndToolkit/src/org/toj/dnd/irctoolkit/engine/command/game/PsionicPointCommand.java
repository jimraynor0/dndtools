package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class PsionicPointCommand extends UndoableTopicCommand {
    private String charName;
    private int usage = 1;

    public PsionicPointCommand(String[] args) {
        if (args.length == 1) {
            try {
                usage = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                charName = args[0];
            }
        }
        if (args.length == 2) {
            try {
                usage = Integer.parseInt(args[0]);
                charName = args[1];
            } catch (NumberFormatException e) {
                usage = Integer.parseInt(args[1]);
                charName = args[0];
            }
        }
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (this.charName == null) {
            this.charName = caller;
        }
        getGame().findCharByNameOrAbbre(charName).recordPp(usage);
    }
}
