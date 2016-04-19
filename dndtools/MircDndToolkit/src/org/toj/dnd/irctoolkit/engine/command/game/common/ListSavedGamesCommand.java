package org.toj.dnd.irctoolkit.engine.command.game.common;

import java.util.List;

import org.toj.dnd.irctoolkit.engine.command.GameCommand;
import org.toj.dnd.irctoolkit.engine.command.IrcCommand;
import org.toj.dnd.irctoolkit.io.file.GameStore;
import org.toj.dnd.irctoolkit.io.udp.OutgoingMsg;

@IrcCommand(command = "listgames", args = {}, summary = ".listgames - 列出所有游戏存档。")
public class ListSavedGamesCommand extends GameCommand {

    public ListSavedGamesCommand(Object[] args) {
    }

    @Override
    public List<OutgoingMsg> execute() {
        for (String game : GameStore.listSavedGames()) {
            sendMsg(game);
        }
        return this.msgs;
    }

    @Override
    public boolean requireGameContext() {
        return false;
    }
}
