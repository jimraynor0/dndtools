package org.toj.dnd.irctoolkit.engine.command.game;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

public class AddPcCommand extends UndoableTopicCommand {

    private String[] chars;

    public AddPcCommand(String[] chars) {
        this.chars = chars;
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        if (chars.length == 0) {
            getGame().addPc(caller);
        } else {
            for (String charName : chars) {
                getGame().addPc(charName);
            }
        }
    }
}
