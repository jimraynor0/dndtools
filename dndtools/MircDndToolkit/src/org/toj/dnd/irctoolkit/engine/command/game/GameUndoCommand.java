package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.EmptyStackException;
import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.exceptions.ToolkitCommandException;
import org.toj.dnd.irctoolkit.game.Game;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

@IrcCommand(command = "undo", args = {})
public class GameUndoCommand extends GameCommand {

    public GameUndoCommand(Object[] args) {
        super();
    }

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
