package org.toj.dnd.irctoolkit.engine.command.game.draca;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.game.draca.DracaGame;

public abstract class DracaGameCommand extends GameCommand {
    protected DracaGame getGame() {
        return (DracaGame) super.getGame();
    }

    protected String getPcZoneName(String pc, String zone) {
        return zone + "@" + pc;
    }
}
