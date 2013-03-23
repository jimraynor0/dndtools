package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class SurgeCommand extends UndoableTopicCommand {
    private String charName;
    private int usage = 1;

    public SurgeCommand() {
        super();
    }

    public SurgeCommand(int usage) {
        this.usage = usage;
    }

    public SurgeCommand(String charName) {
        this.charName = charName;
    }

    public SurgeCommand(String charName, int usage) {
        this.charName = charName;
        this.usage = usage;
    }

    public SurgeCommand(int usage, String charName) {
        this.charName = charName;
        this.usage = usage;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (this.charName == null) {
            this.charName = caller;
        }
        getGame().findCharByNameOrAbbre(charName).recordSurge(usage);
    }
}
