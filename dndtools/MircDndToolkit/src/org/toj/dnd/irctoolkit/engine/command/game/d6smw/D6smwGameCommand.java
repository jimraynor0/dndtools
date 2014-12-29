package org.toj.dnd.irctoolkit.engine.command.game.d6smw;

import org.toj.dnd.irctoolkit.engine.command.UndoableTopicCommand;
import org.toj.dnd.irctoolkit.game.d6smw.D6smwGame;

public abstract class D6smwGameCommand extends UndoableTopicCommand {
    protected D6smwGame getGame() {
        return (D6smwGame) super.getGame();
    }
}
