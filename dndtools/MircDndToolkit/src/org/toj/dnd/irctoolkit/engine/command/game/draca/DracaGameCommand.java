package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.game.draca.DracaGame;

public abstract class DracaGameCommand extends UndoableTopicCommand {
    protected DracaGame getGame() {
        return (DracaGame) super.getGame();
    }
}
