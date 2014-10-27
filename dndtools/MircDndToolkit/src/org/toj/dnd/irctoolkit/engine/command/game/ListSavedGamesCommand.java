package org.toj.dnd.irctoolkit.engine.command.game;

import java.util.List;

import org.apache.log4j.Logger;
import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.io.file.GameStore;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

@IrcCommand(command = "listgames", args = {})
public class ListSavedGamesCommand extends GameCommand {

    private Logger log = Logger.getLogger(this.getClass());

    public ListSavedGamesCommand(Object[] args) {
    }

    @Override
    public List<OutgoingMsg> execute() {
        for (String game : GameStore.listSavedGames()) {
            sendMsg(game);
        }
        return this.msgs;
    }

    public boolean requireGameContext() {
        return false;
    }
}
