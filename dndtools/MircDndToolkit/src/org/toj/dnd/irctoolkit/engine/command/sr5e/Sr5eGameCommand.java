package org.toj.dnd.irctoolkit.engine.command.sr5e;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.game.sr5e.Sr5eGame;

public abstract class Sr5eGameCommand extends UndoableTopicCommand {
    protected Sr5eGame getGame() {
        return (Sr5eGame) super.getGame();
    }
}
