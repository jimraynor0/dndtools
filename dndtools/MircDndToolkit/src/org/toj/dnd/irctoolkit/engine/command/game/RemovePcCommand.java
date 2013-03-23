package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class RemovePcCommand extends UndoableTopicCommand {

    private String[] chars;

    public RemovePcCommand(String[] chars) {
        this.chars = chars;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (chars.length == 0) {
            getGame().removePc(caller);
        } else {
            for (String charName : chars) {
                getGame().removePc(charName);
            }
        }
    }
}
