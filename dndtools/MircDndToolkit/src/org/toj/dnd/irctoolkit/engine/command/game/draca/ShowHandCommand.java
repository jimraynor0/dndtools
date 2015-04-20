package org.toj.dnd.irctoolkit.engine.command.game.draca;

import java.util.Collections;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;

@IrcCommand(command = "hand", args = {})
public class ShowHandCommand extends DracaGameCommand {

    public ShowHandCommand(Object[] args) {
    }

    @Override
    public void doProcess() throws ToolkitCommandException {
        List<String> hand = getGame().getPcHand(caller).getCards();
        Collections.sort(hand);
        whisper(caller, "你的手牌: " + hand);
    }
}
