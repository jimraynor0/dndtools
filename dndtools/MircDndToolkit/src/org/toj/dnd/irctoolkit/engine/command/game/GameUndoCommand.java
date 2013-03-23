package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.EmptyStackException;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

public class GameUndoCommand extends GameCommand {

    @Override
    public List<OutgoingMsg> execute() throws ToolkitCommandException {
        try {
            context.getHistory().undoIrcCmd(chan);
            Game last = getGame();
            sendTopic(last.generateTopic());
            if (topicRefreshNeeded()) {
                super.refreshTopic();
            }
            return this.msgs;
        } catch (EmptyStackException e) {
            throw new ToolkitCommandException("No history record available.");
        }
    }
}
