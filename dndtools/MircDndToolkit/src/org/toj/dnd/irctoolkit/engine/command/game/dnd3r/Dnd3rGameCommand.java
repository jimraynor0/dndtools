package org.toj.dnd.irctoolkit.engine.command.game.dnd3r;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.game.dnd3r.Dnd3rGame;

public abstract class Dnd3rGameCommand extends UndoableTopicCommand {
    protected Dnd3rGame getGame() {
        return (Dnd3rGame) super.getGame();
    }
}
