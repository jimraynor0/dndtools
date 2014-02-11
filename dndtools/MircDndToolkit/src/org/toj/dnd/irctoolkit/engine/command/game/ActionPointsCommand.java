package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(patterns = { "ap" }, argsMin = 1, argsMax = 3)
public class ActionPointsCommand extends UndoableTopicCommand {
    private String charName;
    private int usage = 1;

    public ActionPointsCommand(String[] args) {
        this();
        if (args.length == 2) {
            if (isInteger(args[1])) {
                usage = Integer.parseInt(args[1]);
            } else {
                charName = args[1];
            }
        }
        if (args.length == 3) {
            if (isInteger(args[1])) {
                usage = Integer.parseInt(args[1]);
                charName = args[2];
            } else {
                charName = args[1];
                usage = Integer.parseInt(args[2]);
            }
        }
    }

    public ActionPointsCommand() {
        super();
    }

    public ActionPointsCommand(int usage) {
        this.usage = usage;
    }

    public ActionPointsCommand(String charName) {
        this.charName = charName;
    }

    public ActionPointsCommand(String charName, int usage) {
        this.charName = charName;
        this.usage = usage;
    }

    public ActionPointsCommand(int usage, String charName) {
        this.charName = charName;
        this.usage = usage;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (this.charName == null) {
            this.charName = caller;
        }
        getGame().findCharByNameOrAbbre(charName).recordAp(usage);
    }
}
